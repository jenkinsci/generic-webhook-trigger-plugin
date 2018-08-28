Feature: It should be possible to trigger for GitHub push events and filter out specific user.

  Scenario: Trigger a job when commit is pushed and the pusher is not named "build".

    Given the following generic variables are configured:
      | variable   | expression    | expressionType  | defaultValue | regexpFilter  |
      | user       | $.pusher.name | JSONPath        |              |               |

    Given filter is configured with:
      | text   | expression   |
      | $user  | ^((?!build)) |

    When received post content is:
    """
    {
      "pusher": {
        "name": "baxterthehacker",
      }
    }
    """
    Then the job is triggered

    When received post content is:
    """
    {
      "pusher": {
        "name": "build",
      }
    }
    """
    Then the job is not triggered


  Scenario: Trigger a job when commit is pushed and the pusher is not named "build" and the branch is develop or feature.

    Given the following generic variables are configured:
      | variable   | expression    | expressionType  | defaultValue | regexpFilter  |
      | user       | $.pusher.name | JSONPath        |              |               |
      | ref        | $.ref         | JSONPath        |              |               |

    Given filter is configured with:
      | text        | expression                                                     |
      | $ref $user  | ^(refs/heads/develop\|refs/heads/feature/[^\s]+?)\s((?!build)) |

    When received post content is:
    """
    {
      "ref": "refs/heads/develop",
      "pusher": {
        "name": "baxterthehacker",
      }
    }
    """
    Then the job is triggered

    When received post content is:
    """
    {
      "ref": "refs/heads/feature/jira-123-stuff",
      "pusher": {
        "name": "baxterthehacker",
      }
    }
    """
    Then the job is triggered

    When received post content is:
    """
    {
      "ref": "refs/heads/feature/jira-123-stuff",
      "pusher": {
        "name": "build",
      }
    }
    """
    Then the job is not triggered

    When received post content is:
    """
    {
      "ref": "refs/heads/jira-123-stuff",
      "pusher": {
        "name": "baxterthehacker",
      }
    }
    """
    Then the job is not triggered

