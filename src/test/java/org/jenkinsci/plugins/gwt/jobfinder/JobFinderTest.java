package org.jenkinsci.plugins.gwt.jobfinder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.BuildAuthorizationToken;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import jenkins.model.ParameterizedJobMixIn.ParameterizedJob;
import org.jenkinsci.plugins.gwt.FoundJob;
import org.jenkinsci.plugins.gwt.GenericTrigger;
import org.junit.Before;
import org.junit.Test;

public class JobFinderTest {
  private final AtomicInteger atomicInteger = new AtomicInteger(0);
  private List<ParameterizedJob> allParameterizedJobsByImpersonation;
  private final ParameterizedJob job1WithNoToken = createJob("", "");
  private final ParameterizedJob job2WithNoToken = createJob("", "");
  private final ParameterizedJob job3WithAuthTokenAbc = createJob("ABC", "");
  private final ParameterizedJob job4WithAuthTokenDef = createJob("DEF", "");
  private final ParameterizedJob job5WithGenericTokenAbc = createJob("", "ABC");
  private final ParameterizedJob job6WithGenericTokenDef = createJob("", "DEF");
  private Boolean didImpersonate;

  @Before
  public void before() {
    didImpersonate = null;
    allParameterizedJobsByImpersonation = new ArrayList<>();
    final JobFinderImpersonater jobFinderImpersonater =
        new JobFinderImpersonater() {
          @Override
          public List<ParameterizedJob> getAllParameterizedJobs(boolean impersonate) {
            didImpersonate = impersonate;
            return allParameterizedJobsByImpersonation;
          }
        };
    allParameterizedJobsByImpersonation.add(job1WithNoToken);
    allParameterizedJobsByImpersonation.add(job2WithNoToken);
    allParameterizedJobsByImpersonation.add(job3WithAuthTokenAbc);
    allParameterizedJobsByImpersonation.add(job4WithAuthTokenDef);
    allParameterizedJobsByImpersonation.add(job5WithGenericTokenAbc);
    allParameterizedJobsByImpersonation.add(job6WithGenericTokenDef);

    JobFinder.setJobFinderImpersonater(jobFinderImpersonater);
  }

  @SuppressWarnings("deprecation")
  private ParameterizedJob createJob(final String authToken, final String genericToken) {
    final ParameterizedJob mock = mock(ParameterizedJob.class);
    when(mock.getAuthToken()) //
        .thenReturn(new BuildAuthorizationToken(authToken));
    when(mock.getFullName()) //
        .thenReturn("name-" + atomicInteger.incrementAndGet());
    final Map<TriggerDescriptor, Trigger<?>> triggers = new HashMap<>();
    final TriggerDescriptor typeDescr = mock(TriggerDescriptor.class);
    final GenericTrigger genericTrigger = new GenericTrigger(null, null, null, null, null);
    genericTrigger.setToken(genericToken);
    triggers.put(typeDescr, genericTrigger);
    when(mock.getTriggers()) //
        .thenReturn(triggers);

    return mock;
  }

  private List<String> findAllJobs(final String givenToken) {
    final List<FoundJob> foundJobs = JobFinder.findAllJobsWithTrigger(givenToken);
    final List<String> names = new ArrayList<>();
    for (final FoundJob found : foundJobs) {
      names.add(found.getFullName());
    }
    Collections.sort(names);
    return names;
  }

  @Test
  public void testThatJobsWithoutTokenIsNotFoundWhenTokenSupplied() {
    final String givenToken = "some-token";

    final List<String> actual = findAllJobs(givenToken);

    assertThat(actual) //
        .isEmpty();
    assertThat(didImpersonate) //
        .isTrue();
  }

  @Test
  public void testThatJobsWithTokenIsFoundWhenTokenSuppliedAndMatchesABC() {
    final String givenToken = "ABC";

    final List<String> actual = findAllJobs(givenToken);

    assertThat(actual) //
        .containsExactly(job3WithAuthTokenAbc.getFullName(), job5WithGenericTokenAbc.getFullName());
    assertThat(didImpersonate) //
        .isTrue();
  }

  @Test
  public void testThatJobsWithTokenIsFoundWhenTokenSuppliedAndMatchesDEF() {
    final String givenToken = "DEF";

    final List<String> actual = findAllJobs(givenToken);

    assertThat(actual) //
        .containsExactly(job4WithAuthTokenDef.getFullName(), job6WithGenericTokenDef.getFullName());
    assertThat(didImpersonate) //
        .isTrue();
  }

  @Test
  public void testThatJobsWithoutTokenIsFoundWhenTokenNotSupplied() {
    final String givenToken = "";

    final List<String> actual = findAllJobs(givenToken);

    assertThat(actual) //
        .containsExactly(job1WithNoToken.getFullName(), job2WithNoToken.getFullName());
    assertThat(didImpersonate) //
        .isFalse();
  }

  @Test
  public void testThatNoJobsAreFoundWhenTokenSuppliedButDoesNotMatch() {
    final String givenToken = "QWE";

    final List<String> actual = findAllJobs(givenToken);

    assertThat(actual) //
        .isEmpty();
    assertThat(didImpersonate) //
        .isTrue();
  }
}
