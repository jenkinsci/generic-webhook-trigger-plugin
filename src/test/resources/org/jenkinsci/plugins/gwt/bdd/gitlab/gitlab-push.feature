Feature: It should be possible to trigger for GitLab push events.

  Scenario: A build should be triggered when a commit is pushed.

    Given the following generic variables are configured:
      | variable        | expression               | expressionType  | defaultValue | regexpFilter  |
      | object_kind     | $.object_kind            | JSONPath        |              |               |
      | after           | $.after                  | JSONPath        |              |               |
      | ref             | $.ref                    | JSONPath        |              |               |
      | git_ssh_url     | $.repository.git_ssh_url | JSONPath        |              |               |

    Given filter is configured with text: $object_kind $after
    Given filter is configured with expression: ^push\s.{40}$

    When received post content is:
    """
    {
      "object_kind": "push",
      "after": "82b3d5ae55f7080f1e6022629cdb57bfae7cccc7",
      "ref": "refs/heads/master",
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable         | value                                    |
      | object_kind      | push                                     |
      | after            | 82b3d5ae55f7080f1e6022629cdb57bfae7cccc7 |
      | ref              | refs/heads/master                        |
      | git_ssh_url      | git@example.com:mike/diaspora.git        |


    When received post content is:
    """
    {
      "object_kind": "note",
      "after": "82b3d5ae55f7080f1e6022629cdb57bfae7cccc7",
      "ref": "refs/heads/master",
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is not triggered


  Scenario: A build should be triggered when a commit is pushed to develop or feature, not to master.

    Given the following generic variables are configured:
      | variable        | expression               | expressionType  | defaultValue | regexpFilter  |
      | object_kind     | $.object_kind            | JSONPath        |              |               |
      | after           | $.after                  | JSONPath        |              |               |
      | ref             | $.ref                    | JSONPath        |              |               |
      | git_ssh_url     | $.repository.git_ssh_url | JSONPath        |              |               |

    Given filter is configured with text: $object_kind $after $ref
    Given filter is configured with expression: ^push\s.{40}\srefs/heads/(develop|feature/.*)$

    When received post content is:
    """
    {
      "object_kind": "push",
      "after": "82b3d5ae55f7080f1e6022629cdb57bfae7cccc7",
      "ref": "refs/heads/develop",
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable         | value                                    |
      | object_kind      | push                                     |
      | after            | 82b3d5ae55f7080f1e6022629cdb57bfae7cccc7 |
      | ref              | refs/heads/develop                       |
      | git_ssh_url      | git@example.com:mike/diaspora.git        |

    When received post content is:
    """
    {
      "object_kind": "push",
      "after": "82b3d5ae55f7080f1e6022629cdb57bfae7cccc7",
      "ref": "refs/heads/feature/jira-123",
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is triggered

    When received post content is:
    """
    {
      "object_kind": "push",
      "after": "82b3d5ae55f7080f1e6022629cdb57bfae7cccc7",
      "ref": "refs/heads/master",
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is not triggered
