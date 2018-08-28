Feature: It should be possible to trigger for GitHub push events to specific branches.

  Scenario: Trigger a job when commit is pushed to develop or any feature branch

    Given the following generic variables are configured:
      | variable            | expression                        | expressionType  | defaultValue | regexpFilter  |
      | ref                 | $.ref                             | JSONPath        |              |               |

    Given filter is configured with text: $ref
    Given filter is configured with expression: ^(refs/heads/develop|refs/heads/feature/.+)$

    When received post content is:
    """
    {
      "ref": "refs/heads/develop",
    }
    """
    Then the job is triggered

    When received post content is:
    """
    {
      "ref": "refs/heads/feature/jira-123-impl-stuff",
    }
    """
    Then the job is triggered

    When received post content is:
    """
    {
      "ref": "refs/heads/master",
    }
    """
    Then the job is not triggered
