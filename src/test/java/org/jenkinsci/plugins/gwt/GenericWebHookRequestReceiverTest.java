package org.jenkinsci.plugins.gwt;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.kohsuke.stapler.StaplerRequest;

public class GenericWebHookRequestReceiverTest {

  @Test
  public void testThatNoTokenGivesNull() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();
    final Map<String, List<String>> headers = newHashMap();
    final Map<String, String[]> parameterMap = newHashMap();

    final String actual = sut.getGivenToken(headers, parameterMap);

    assertThat(actual) //
        .isNull();
  }

  @Test
  public void testThatNoQuietPeriodGivesNegOne() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();
    final Map<String, List<String>> headers = newHashMap();
    final Map<String, String[]> parameterMap = newHashMap();

    final int actual = sut.getGivenQuietPeriod(headers, parameterMap);

    assertThat(actual) //
        .isEqualTo(-1);
  }

  @Test
  public void testThatParameterTokenGivesThatToken() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();
    final Map<String, List<String>> headers = newHashMap();
    final Map<String, String[]> parameterMap =
        of( //
            "token", new String[] {"tokenParam"});

    final String actual = sut.getGivenToken(headers, parameterMap);

    assertThat(actual) //
        .isEqualTo("tokenParam");
  }

  @Test
  public void testThatParameterQuietPeriodGivesThatQueitPeriod() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();
    final Map<String, List<String>> headers = newHashMap();
    final Map<String, String[]> parameterMap =
        of( //
            "jobQuietPeriod", new String[] {"1"});

    final int actual = sut.getGivenQuietPeriod(headers, parameterMap);

    assertThat(actual) //
        .isEqualTo(1);
  }

  @Test
  public void testThatHeaderTokenGivesThatToken() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();
    final Map<String, List<String>> headers =
        of( //
            "token", (List<String>) newArrayList("tokenHeader"));
    final Map<String, String[]> parameterMap = newHashMap();

    final String actual = sut.getGivenToken(headers, parameterMap);

    assertThat(actual) //
        .isEqualTo("tokenHeader");
  }

  @Test
  public void testThatGitLabTokenHeaderTokenGivesThatToken() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();
    final Map<String, List<String>> headers =
        of( //
            "x-gitlab-token", (List<String>) newArrayList("gitlabtoken"));
    final Map<String, String[]> parameterMap = newHashMap();

    final String actual = sut.getGivenToken(headers, parameterMap);

    assertThat(actual) //
        .isEqualTo("gitlabtoken");
  }

  @Test
  public void testThatHeaderQuietPeriodGivesThatQueitPeriod() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();
    final Map<String, List<String>> headers =
        of( //
            "jobQuietPeriod", (List<String>) newArrayList("1"));
    final Map<String, String[]> parameterMap = newHashMap();

    final int actual = sut.getGivenQuietPeriod(headers, parameterMap);

    assertThat(actual) //
        .isEqualTo(1);
  }

  @Test
  public void testThatNonsensicalQuietPeriodGivesNegOne() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();
    final Map<String, List<String>> headers = newHashMap();
    final Map<String, String[]> parameterMap =
        of( //
            "jobQuietPeriod", new String[] {"fooBar"});

    final int actual = sut.getGivenQuietPeriod(headers, parameterMap);

    assertThat(actual) //
        .isEqualTo(-1);
  }

  @Test
  public void testThatHeaderAuthorizationBearerTokenGivesThatToken() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();
    final Map<String, List<String>> headers =
        of( //
            "authorization", (List<String>) newArrayList("Bearer baererTokenValue"));
    final Map<String, String[]> parameterMap = newHashMap();

    final String actual = sut.getGivenToken(headers, parameterMap);

    assertThat(actual) //
        .isEqualTo("baererTokenValue");
  }

  @Test
  public void testThatHeadersCanBeTransformedToList() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();

    final StaplerRequest request = mock(StaplerRequest.class);
    when(request.getHeaderNames()) //
        .thenReturn( //
            new ArrayEnumeration(new String[] {"headerName1"}));
    when(request.getHeaders("headerName1")) //
        .thenReturn( //
            new ArrayEnumeration(new String[] {"headerValue1"}));

    final Map<String, List<String>> actual = sut.getHeaders(request);

    final Map<String, List<String>> expected = new HashMap<>();
    expected.put("headername1", newArrayList("headerValue1"));

    assertThat(actual) //
        .isEqualTo(expected);
  }

  @Test
  public void testThatHeadersCanBeTransformedToListSeveralValues() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();

    final StaplerRequest request = mock(StaplerRequest.class);
    when(request.getHeaderNames()) //
        .thenReturn( //
            new ArrayEnumeration(new String[] {"headerName1"}));
    when(request.getHeaders("headerName1")) //
        .thenReturn( //
            new ArrayEnumeration(new String[] {"headerValue1", "headerValue2"}));

    final Map<String, List<String>> actual = sut.getHeaders(request);

    final Map<String, List<String>> expected = new HashMap<>();
    expected.put("headername1", newArrayList("headerValue1", "headerValue2"));

    assertThat(actual) //
        .isEqualTo(expected);
  }

  @Test
  public void testThatHeadersCanBeTransformedToListSeveralNames() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();

    final StaplerRequest request = mock(StaplerRequest.class);
    when(request.getHeaderNames()) //
        .thenReturn( //
            new ArrayEnumeration(new String[] {"headerName1", "headerName2"}));
    when(request.getHeaders("headerName1")) //
        .thenReturn( //
            new ArrayEnumeration(new String[] {"headerValue1"}));
    when(request.getHeaders("headerName2")) //
        .thenReturn( //
            new ArrayEnumeration(new String[] {"headerValue2"}));

    final Map<String, List<String>> actual = sut.getHeaders(request);

    final Map<String, List<String>> expected = new HashMap<>();
    expected.put("headername1", newArrayList("headerValue1"));
    expected.put("headername2", newArrayList("headerValue2"));

    assertThat(actual) //
        .isEqualTo(expected);
  }

  @Test
  public void testThatMessageIsCreatedFromExceptionWithNoStacktrace() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();

    final Throwable t = new IndexOutOfBoundsException();
    final StackTraceElement[] stackTrace = new StackTraceElement[0];
    t.setStackTrace(stackTrace);
    final String actual = sut.createMessageFromException(t);

    assertThat(actual) //
        .isEqualTo(
            "Exception occurred (class java.lang.IndexOutOfBoundsException: null), full stack trace in Jenkins server log. ");
  }

  @Test
  public void testThatMessageIsCreatedFromExceptionWithStacktrace() {
    final GenericWebHookRequestReceiver sut = new GenericWebHookRequestReceiver();

    final Throwable t = new IndexOutOfBoundsException();
    final String actual = sut.createMessageFromException(t);

    assertThat(actual) //
        .startsWith(
            "Exception occurred (class java.lang.IndexOutOfBoundsException: null), full stack trace in Jenkins server log. Thrown in: org.jenkinsci.plugins.gwt.GenericWebHookRequestReceiverTest:");
  }
}
