package org.jenkinsci.plugins.gwt.jobfinder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jenkinsci.plugins.gwt.global.JobFinderConfig;
import org.jenkinsci.plugins.gwt.global.JobFinderConfigPathItem;
import org.jenkinsci.plugins.gwt.resolvers.JsonFlattener;
import org.junit.Test;

public class PreciseJobFinderTest {

  private List<JobFinderConfigPathItem> initJobFinderConfigPathItem(String[] pathList) {
    List<JobFinderConfigPathItem> jobFinderConfigPathItemList = new ArrayList<>();
    for (final String path : pathList) {
      jobFinderConfigPathItemList.add(new JobFinderConfigPathItem(path));
    }
    return jobFinderConfigPathItemList;
  }

  private void assertFullName(String[] pathList, String postContent, String expectedPath) {
    final List<JobFinderConfigPathItem> jobFinderConfigPathItemList =
        initJobFinderConfigPathItem(pathList);
    String regexFilter = "";
    String postContentKey = postContent.getClass().getName();
    JsonObject resolved = JobFinder.getJsonObject(postContent);
    JsonFlattener flattenJson = new JsonFlattener();
    final Map<String, String> postMap =
        flattenJson.flattenJson(postContentKey, regexFilter, resolved);
    final List<String> fullNameList =
        new JobFinderConfig(jobFinderConfigPathItemList).getFullNameList(postMap);
    assertThat(fullNameList).isNotEmpty();
    for (final String fullName : fullNameList) {
      assertEquals(expectedPath, fullName);
    }
  }

  private String getContent(final String resourceName) {
    try {
      return Resources.toString(
          Resources.getResource(resourceName).toURI().toURL(), Charsets.UTF_8);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testGetFullNameListGitHubUseConstantPath() {
    final String resourceName = "github-push-event.json";
    final String actualPathExpression = "jenkins";
    final String expectedPath = "jenkins";
    final String postContent = this.getContent(resourceName);
    final String[] pathList = new String[] {actualPathExpression};
    assertFullName(pathList, postContent, expectedPath);
  }

  @Test
  public void testGetFullNameListGitHubUseRecursionPath() {
    final String resourceName = "github-push-event.json";
    final String actualPathExpression = "/path1/path2/jenkins";
    final String expectedPath = "/path1/path2/jenkins";
    final String postContent = this.getContent(resourceName);
    final String[] pathList = new String[] {actualPathExpression};
    assertFullName(pathList, postContent, expectedPath);
  }

  @Test
  public void testGetFullNameListGitHubUsePathExpression() {
    final String resourceName = "github-push-event.json";
    final String actualPathExpression = "/dd/${repository.owner.name}/${repository.name}";
    final String expectedPath = "/dd/baxterthehacker/public-repo";
    final String postContent = this.getContent(resourceName);
    final String[] pathList = new String[] {actualPathExpression};
    assertFullName(pathList, postContent, expectedPath);
  }

  @Test
  public void testGetFullNameListGitLabCommentUseConstantPath() {
    final String resourceName = "gitlab-mergerequest-comment.json";
    final String actualPathExpression = "jenkins";
    final String expectedPath = "jenkins";
    final String postContent = this.getContent(resourceName);
    final String[] pathList = new String[] {actualPathExpression};
    assertFullName(pathList, postContent, expectedPath);
  }

  @Test
  public void testGetFullNameListGitLabCommentUseRecursionPath() {
    final String resourceName = "gitlab-mergerequest-comment.json";
    final String actualPathExpression = "/path1/path2/jenkins";
    final String expectedPath = "/path1/path2/jenkins";
    final String postContent = this.getContent(resourceName);
    final String[] pathList = new String[] {actualPathExpression};
    assertFullName(pathList, postContent, expectedPath);
  }

  @Test
  public void testGetFullNameListGitLabCommentUsePathExpression() {
    final String resourceName = "gitlab-mergerequest-comment.json";
    final String actualPathExpression = "/xx/${project.name}";
    final String expectedPath = "/xx/violations-test";
    final String postContent = this.getContent(resourceName);
    final String[] pathList = new String[] {actualPathExpression};
    assertFullName(pathList, postContent, expectedPath);
  }

  @Test
  public void testGetFullNameListGitLabPushUseConstantPath() {
    final String resourceName = "gitlab-mergerequest-push.json";
    final String actualPathExpression = "jenkins";
    final String expectedPath = "jenkins";
    final String postContent = this.getContent(resourceName);
    final String[] pathList = new String[] {actualPathExpression};
    assertFullName(pathList, postContent, expectedPath);
  }

  @Test
  public void testGetFullNameListGitLabPushUseRecursionPath() {
    final String resourceName = "gitlab-mergerequest-push.json";
    final String actualPathExpression = "/path1/path2/jenkins";
    final String expectedPath = "/path1/path2/jenkins";
    final String postContent = this.getContent(resourceName);
    final String[] pathList = new String[] {actualPathExpression};
    assertFullName(pathList, postContent, expectedPath);
  }

  @Test
  public void testGetFullNameListGitLabPushUsePathExpression() {
    final String resourceName = "gitlab-mergerequest-push.json";
    final String actualPathExpression = "/yy/${project.name}";
    final String expectedPath = "/yy/violations-test";
    final String postContent = this.getContent(resourceName);
    final String[] pathList = new String[] {actualPathExpression};
    assertFullName(pathList, postContent, expectedPath);
  }
}
