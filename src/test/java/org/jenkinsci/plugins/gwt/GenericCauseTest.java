package org.jenkinsci.plugins.gwt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GenericCauseTest {

    @Test
    void sanitizeCauseString_link() {
        final String actual = new GenericCause(
                        null, null, false, false, "<b>Triggered by:</b> <a href=\"https://test.org/pr/1\">PR 1</a>")
                .getShortDescription();

        assertThat(actual)
                .isEqualTo("<b>Triggered by:</b> <a href=\"https://test.org/pr/1\" rel=\"nofollow\">PR 1</a>");
    }

    @Test
    void sanitizeCauseString_script() {
        final String actual = new GenericCause(
                        null, null, false, false, "<b>Triggered by:</b> PR 1<div><script>somethingBad()</script></div>")
                .getShortDescription();

        assertThat(actual).isEqualTo("<b>Triggered by:</b> PR 1");
    }
}
