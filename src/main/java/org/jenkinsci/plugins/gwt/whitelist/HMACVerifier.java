package org.jenkinsci.plugins.gwt.whitelist;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.jenkinsci.plugins.gwt.global.WhitelistAlgorithm;

public class HMACVerifier {

  public static void hmacVerify(
      final Map<String, List<String>> headers,
      final String postContent,
      final String hmacHeader,
      final String hmacSecret,
      final String algorithm)
      throws WhitelistException {
    final String headerValue = getHeaderValue(hmacHeader, headers);
    final byte[] calculateHmacBytes = getCalculatedHmac(postContent, hmacSecret, algorithm);
    final String calculateHmacAsString = Base64.getEncoder().encodeToString(calculateHmacBytes);
    final String calculateHmacAsHex = bytesToHex(calculateHmacBytes);
    final String calculateHmacAsHexAndBase64 =
        new String(Base64.getEncoder().encode(calculateHmacAsHex.getBytes(UTF_8)), UTF_8);

    if (!headerValue.equalsIgnoreCase(calculateHmacAsHex)
        && !headerValue.equalsIgnoreCase(calculateHmacAsHexAndBase64)
        && !headerValue.equalsIgnoreCase(calculateHmacAsString)) {
      throw new WhitelistException(
          "HMAC verification failed with \""
              + hmacHeader
              + "\" as \""
              + headerValue
              + "\" and algorithm "
              + algorithm);
    }
  }

  private static byte[] getCalculatedHmac(
      final String postContent, final String hmacSecret, final String algorithm) {
    try {
      final byte[] byteKey = hmacSecret.getBytes(UTF_8.name());
      final Mac mac = Mac.getInstance(algorithm);
      final SecretKeySpec keySpec = new SecretKeySpec(byteKey, algorithm);
      mac.init(keySpec);
      return mac.doFinal(postContent.getBytes(UTF_8));
    } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  private static String bytesToHex(final byte[] bytes) {
    final char[] hexArray = "0123456789ABCDEF".toCharArray();
    final char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      final int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  private static String getHeaderValue(
      final String hmacHeader, final Map<String, List<String>> headers) throws WhitelistException {
    for (final Entry<String, List<String>> ck : headers.entrySet()) {
      final boolean sameHeader = ck.getKey().equalsIgnoreCase(hmacHeader);
      final boolean oneValue = ck.getValue().size() == 1;
      if (sameHeader && oneValue) {
        final String value = ck.getValue().get(0);
        for (final WhitelistAlgorithm algorithm : WhitelistAlgorithm.values()) {
          final String startString = algorithm.getAlgorithm() + "=";
          if (value.startsWith(startString)) {
            // To handle X-Hub-Signature: sha256=87e3e7...
            return value.substring(startString.length());
          }
          final String startStringHmac = "HMAC ";
          if (value.startsWith(startStringHmac)) {
            // To handle teams signature authorization: HMAC
            // w2g2swwmrsvRLZ5W68LfjaLrSR4fN0ErKGyfTPbLrBs=
            return value.substring(startStringHmac.length()).trim();
          }
        }
        return value;
      }
    }
    throw new WhitelistException(
        "Was unable to find header with name \"" + hmacHeader + "\" among " + headers);
  }
}
