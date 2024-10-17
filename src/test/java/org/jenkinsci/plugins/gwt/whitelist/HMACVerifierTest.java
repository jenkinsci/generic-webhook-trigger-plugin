package org.jenkinsci.plugins.gwt.whitelist;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jenkinsci.plugins.gwt.whitelist.HMACVerifier.hmacVerify;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jenkinsci.plugins.gwt.global.WhitelistAlgorithm;
import org.junit.Test;

public class HMACVerifierTest {

    @Test
    public void testThatHmacCanBeVerifiedAndValidHex() throws Exception {
        final String postContent = this.getPostContent();
        final String hmacSecret = "this is secret";
        final String algorithm = WhitelistAlgorithm.HMAC_SHA256.getFullName();
        final String hmacHeader = "X-Hub-Signature";
        final String headerValue = "sha256=87e3e7b7e4567f528342a75b6d88c619f272c68a4d0d565c68d596a830213164";
        final Map<String, List<String>> headers = this.getHeaders(hmacHeader, headerValue);

        final boolean actual = this.testHmacVerify(headers, postContent, hmacHeader, hmacSecret, algorithm);
        assertThat(actual).isTrue();
    }

    @Test
    public void testThatHmacCanBeVerifiedAndValid() throws Exception {
        final String postContent = this.getPostContent();
        final String hmacSecret = "this is secret";
        final String algorithm = WhitelistAlgorithm.HMAC_SHA256.getFullName();
        final String hmacHeader = "X-Hub-Signature";
        final String headerValue = "sha256=h+Pnt+RWf1KDQqdbbYjGGfJyxopNDVZcaNWWqDAhMWQ=";
        final Map<String, List<String>> headers = this.getHeaders(hmacHeader, headerValue);

        final boolean actual = this.testHmacVerify(headers, postContent, hmacHeader, hmacSecret, algorithm);
        assertThat(actual).isTrue();
    }

    @Test
    public void testThatHmacCanBeVerifiedAndValidHmacPrefixHeader() throws Exception {
        final String postContent = this.getPostContent();
        final String hmacSecret = "this is secret";
        final String algorithm = WhitelistAlgorithm.HMAC_SHA256.getFullName();
        final String hmacHeader = "X-Hub-Signature";
        final String headerValue = "HMAC h+Pnt+RWf1KDQqdbbYjGGfJyxopNDVZcaNWWqDAhMWQ=";
        final Map<String, List<String>> headers = this.getHeaders(hmacHeader, headerValue);

        final boolean actual = this.testHmacVerify(headers, postContent, hmacHeader, hmacSecret, algorithm);
        assertThat(actual).isTrue();
    }

    @Test
    public void testThatHmacCanBeBase64() throws Exception {
        final String postContent = "whatever";
        final String hmacSecret = "this is secret";
        final String algorithm = WhitelistAlgorithm.HMAC_SHA256.getFullName();
        final String hmacHeader = "hmac";
        final String headerValue =
                "NzEyMTJGODU0RTIzQzU3NUQ3QjFBQUQ0QzM0NjcwRkYwOEVCRjcyMUMzODM3NjY4NjEzRTk2Qzg3RjZFRThCMg==";
        final Map<String, List<String>> headers = this.getHeaders(hmacHeader, headerValue);

        final boolean actual = this.testHmacVerify(headers, postContent, hmacHeader, hmacSecret, algorithm);

        assertThat(actual).isTrue();
    }

    @Test
    public void testThatHmacCanBeVerifiedAndValidWIthoutAlgorithmInHeader() throws Exception {
        final String postContent = this.getPostContent();
        final String hmacSecret = "this is secret";
        final String algorithm = WhitelistAlgorithm.HMAC_SHA256.getFullName();
        final String hmacHeader = "X-Hub-Signature";
        final String headerValue = "87e3e7b7e4567f528342a75b6d88c619f272c68a4d0d565c68d596a830213164";
        final Map<String, List<String>> headers = this.getHeaders(hmacHeader, headerValue);

        final boolean actual = this.testHmacVerify(headers, postContent, hmacHeader, hmacSecret, algorithm);

        assertThat(actual).isTrue();
    }

    @Test
    public void testThatHmacCanBeVerifiedAndInvalid() throws Exception {
        final String postContent = this.getPostContent();
        final String hmacSecret = "this is secret";
        final String algorithm = WhitelistAlgorithm.HMAC_SHA256.getFullName();
        final String hmacHeader = "X-Hub-Signature";
        final String headerValue = "sha256=97e3e7b7e4567f528342a75b6d88c619f272c68a4d0d565c68d596a830213164";
        final Map<String, List<String>> headers = this.getHeaders(hmacHeader, headerValue);

        final boolean actual = this.testHmacVerify(headers, postContent, hmacHeader, hmacSecret, algorithm);

        assertThat(actual).isFalse();
    }

    private Map<String, List<String>> getHeaders(final String hmacHeader, final String value) {
        final Map<String, List<String>> headers = new HashMap<>();
        headers.put(hmacHeader, Arrays.asList(value));
        return headers;
    }

    private String getPostContent() throws IOException, URISyntaxException {
        final String postContent = new String(
                Files.readAllBytes(Paths.get(this.getClass()
                        .getResource("/hmac/hmac-bitbucket-server-payload.json")
                        .toURI())),
                UTF_8);
        return postContent;
    }

    private boolean testHmacVerify(
            final Map<String, List<String>> headers,
            final String postContent,
            final String hmacHeader,
            final String hmacSecret,
            final String algorithm)
            throws WhitelistException {
        try {
            hmacVerify(headers, postContent, hmacHeader, hmacSecret, algorithm);
            return true;
        } catch (final WhitelistException e) {
            return false;
        }
    }
}
