package org.jenkinsci.plugins.gwt.whitelist;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMACVerifier {

  public static boolean hmacVerify(
      final Map<String, List<String>> headers,
      final String postContent,
      final String hmacHeader,
      final String hmacSecret,
      final String algorithm) {
    final String headerValue = getHeaderValue(hmacHeader, headers);
    final String calculateHmac = getCalculatedHmac(postContent, hmacSecret, algorithm);
    return headerValue.equalsIgnoreCase(calculateHmac);
  }

  private static String getCalculatedHmac(
      final String postContent, final String hmacSecret, final String algorithm) {
    try {
      final byte[] byteKey = hmacSecret.getBytes("UTF-8");
      final Mac sha512_HMAC = Mac.getInstance(algorithm);
      final SecretKeySpec keySpec = new SecretKeySpec(byteKey, algorithm);
      sha512_HMAC.init(keySpec);
      final byte[] mac_data = sha512_HMAC.doFinal(postContent.getBytes(UTF_8));
      return bytesToHex(mac_data);
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
      final String hmacHeader, final Map<String, List<String>> headers) {
    for (final Entry<String, List<String>> ck : headers.entrySet()) {
      final boolean sameHeader = ck.getKey().equalsIgnoreCase(hmacHeader);
      final boolean oneValue = ck.getValue().size() == 1;
      if (sameHeader && oneValue) {
        final String value = ck.getValue().get(0);
        if (value.contains("=")) {
          // To handle X-Hub-Signature: sha256=87e3e7...
          return value.split("=")[1];
        }
        return value;
      }
    }
    throw new RuntimeException(
        "Was unable to find header with name \"" + hmacHeader + "\" among " + headers);
  }
}
