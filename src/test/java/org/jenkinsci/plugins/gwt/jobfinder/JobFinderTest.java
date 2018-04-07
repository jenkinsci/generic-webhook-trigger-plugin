package org.jenkinsci.plugins.gwt.jobfinder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.jenkinsci.plugins.gwt.FoundJob;
import org.jenkinsci.plugins.gwt.GenericTrigger;
import org.junit.Before;
import org.junit.Test;

import hudson.model.BuildAuthorizationToken;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import jenkins.model.ParameterizedJobMixIn.ParameterizedJob;

public class JobFinderTest {
  private final AtomicInteger atomicInteger = new AtomicInteger(0);
  private List<ParameterizedJob> allParameterizedJobs;
  private List<ParameterizedJob> allParameterizedJobsByImpersonation;
  private final ParameterizedJob job1WithNoToken = createJob("");
  private final ParameterizedJob job2WithNoToken = createJob("");
  private final ParameterizedJob job3WithTokenAbc = createJob("ABC");
  private final ParameterizedJob job4WithTokenAbc = createJob("ABC");
  private final ParameterizedJob job5WithTokenDef = createJob("DEF");
  private final ParameterizedJob job6WithTokenDef = createJob("DEF");

  @Before
  public void before() {
    allParameterizedJobs = new ArrayList<>();
    allParameterizedJobsByImpersonation = new ArrayList<>();
    final JobFinderImpersonater jobFinderImpersonater =
        new JobFinderImpersonater() {
          @Override
          public List<ParameterizedJob> getAllParameterizedJobs() {
            return allParameterizedJobs;
          }

          @Override
          public List<ParameterizedJob> getAllParameterizedJobsByImpersonation() {
            return allParameterizedJobsByImpersonation;
          }
        };
    allParameterizedJobs.add(job1WithNoToken);
    allParameterizedJobsByImpersonation.add(job2WithNoToken);
    allParameterizedJobs.add(job3WithTokenAbc);
    allParameterizedJobsByImpersonation.add(job4WithTokenAbc);
    allParameterizedJobs.add(job5WithTokenDef);
    allParameterizedJobsByImpersonation.add(job6WithTokenDef);

    JobFinder.setJobFinderImpersonater(jobFinderImpersonater);
  }

  @SuppressWarnings("deprecation")
  private ParameterizedJob createJob(final String token) {
    final ParameterizedJob mock = mock(ParameterizedJob.class);
    when(mock.getAuthToken()) //
        .thenReturn(new BuildAuthorizationToken(token));
    when(mock.getFullName()) //
        .thenReturn("name-" + atomicInteger.incrementAndGet());
    final Map<TriggerDescriptor, Trigger<?>> triggers = new HashMap<>();
    final TriggerDescriptor typeDescr = mock(TriggerDescriptor.class);
    final Trigger<?> genericTrigger = new GenericTrigger(null, null, null, null, null);
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
  public void testThatJobsWithoutTokenIsFoundWhenNoTokenSupplied() {
    final String givenToken = "";

    final List<String> actual = findAllJobs(givenToken);

    assertThat(actual) //
        .containsExactly(job1WithNoToken.getFullName(), job2WithNoToken.getFullName());
  }

  @Test
  public void testThatJobsWithoutTokenIsNotFoundWhenTokenSupplied() {
    final String givenToken = "some-token";

    final List<String> actual = findAllJobs(givenToken);

    assertThat(actual) //
        .isEmpty();
  }

  @Test
  public void testThatJobsWithTokenIsFoundWhenTokenSuppliedAndMatches() {
    final String givenToken = "ABC";

    final List<String> actual = findAllJobs(givenToken);

    assertThat(actual) //
        .containsExactly(job3WithTokenAbc.getFullName(), job4WithTokenAbc.getFullName());
  }

  @Test
  public void testThatJobsWithTokenIsFoundWhenOtherTokenSuppliedAndMatches() {
    final String givenToken = "DEF";

    final List<String> actual = findAllJobs(givenToken);

    assertThat(actual) //
        .containsExactly(job5WithTokenDef.getFullName(), job6WithTokenDef.getFullName());
  }

  @Test
  public void testThatJobsOnlyInImpersonateIsFound() {
    allParameterizedJobs.clear();
    allParameterizedJobsByImpersonation.clear();
    allParameterizedJobsByImpersonation.add(job1WithNoToken);
    final String givenToken = "";

    final List<String> actual = findAllJobs(givenToken);

    assertThat(actual) //
        .containsExactly(job1WithNoToken.getFullName());
  }

  @Test
  public void testThatJobsOnlyNotInImpersonateIsFound() {
    allParameterizedJobs.clear();
    allParameterizedJobs.add(job1WithNoToken);
    allParameterizedJobsByImpersonation.clear();
    final String givenToken = "";

    final List<String> actual = findAllJobs(givenToken);

    assertThat(actual) //
        .containsExactly(job1WithNoToken.getFullName());
  }

  @Test
  public void testThatJobsInBothImpersonateAndNotIsFound() {
    allParameterizedJobs.clear();
    allParameterizedJobs.add(job1WithNoToken);
    allParameterizedJobsByImpersonation.clear();
    allParameterizedJobsByImpersonation.add(job1WithNoToken);
    final String givenToken = "";

    final List<String> actual = findAllJobs(givenToken);

    assertThat(actual) //
        .containsExactly(job1WithNoToken.getFullName());
  }
}
