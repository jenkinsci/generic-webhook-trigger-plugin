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
  private final ParameterizedJob job1WithNoToken = this.createJob("", "");
  private final ParameterizedJob job2WithNoToken = this.createJob("", "");
  private final ParameterizedJob job3WithAuthTokenAbc = this.createJob("ABC", "");
  private final ParameterizedJob job4WithAuthTokenDef = this.createJob("DEF", "");
  private final ParameterizedJob job5WithGenericTokenAbc = this.createJob("", "ABC");
  private final ParameterizedJob job6WithGenericTokenDef = this.createJob("", "DEF");
  private Boolean didImpersonate;

  @Before
  public void before() {
    this.didImpersonate = null;
    this.allParameterizedJobsByImpersonation = new ArrayList<>();
    final JobFinderImpersonater jobFinderImpersonater =
        new JobFinderImpersonater() {
          @Override
          public List<ParameterizedJob> getAllParameterizedJobs(
              final boolean impersonate, final boolean usecache) {
            JobFinderTest.this.didImpersonate = impersonate;
            return JobFinderTest.this.allParameterizedJobsByImpersonation;
          }
        };
    this.allParameterizedJobsByImpersonation.add(this.job1WithNoToken);
    this.allParameterizedJobsByImpersonation.add(this.job2WithNoToken);
    this.allParameterizedJobsByImpersonation.add(this.job3WithAuthTokenAbc);
    this.allParameterizedJobsByImpersonation.add(this.job4WithAuthTokenDef);
    this.allParameterizedJobsByImpersonation.add(this.job5WithGenericTokenAbc);
    this.allParameterizedJobsByImpersonation.add(this.job6WithGenericTokenDef);

    JobFinder.setJobFinderImpersonater(jobFinderImpersonater);
  }

  @SuppressWarnings("deprecation")
  private ParameterizedJob createJob(final String authToken, final String genericToken) {
    final ParameterizedJob mock = mock(ParameterizedJob.class);
    when(mock.getAuthToken()) //
        .thenReturn(new BuildAuthorizationToken(authToken));
    when(mock.getFullName()) //
        .thenReturn("name-" + this.atomicInteger.incrementAndGet());
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
    final List<FoundJob> foundJobs = JobFinder.findAllJobsWithTrigger(givenToken, false);
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

    final List<String> actual = this.findAllJobs(givenToken);

    assertThat(actual) //
        .isEmpty();
    assertThat(this.didImpersonate) //
        .isTrue();
  }

  @Test
  public void testThatJobsWithTokenIsFoundWhenTokenSuppliedAndMatchesABC() {
    final String givenToken = "ABC";

    final List<String> actual = this.findAllJobs(givenToken);

    assertThat(actual) //
        .containsExactly(
            this.job3WithAuthTokenAbc.getFullName(), this.job5WithGenericTokenAbc.getFullName());
    assertThat(this.didImpersonate) //
        .isTrue();
  }

  @Test
  public void testThatJobsWithTokenIsFoundWhenTokenSuppliedAndMatchesDEF() {
    final String givenToken = "DEF";

    final List<String> actual = this.findAllJobs(givenToken);

    assertThat(actual) //
        .containsExactly(
            this.job4WithAuthTokenDef.getFullName(), this.job6WithGenericTokenDef.getFullName());
    assertThat(this.didImpersonate) //
        .isTrue();
  }

  @Test
  public void testThatJobsWithoutTokenIsFoundWhenTokenNotSupplied() {
    final String givenToken = "";

    final List<String> actual = this.findAllJobs(givenToken);

    assertThat(actual) //
        .containsExactly(this.job1WithNoToken.getFullName(), this.job2WithNoToken.getFullName());
    assertThat(this.didImpersonate) //
        .isFalse();
  }

  @Test
  public void testThatNoJobsAreFoundWhenTokenSuppliedButDoesNotMatch() {
    final String givenToken = "QWE";

    final List<String> actual = this.findAllJobs(givenToken);

    assertThat(actual) //
        .isEmpty();
    assertThat(this.didImpersonate) //
        .isTrue();
  }
}
