Feature: It should be possible to trigger for GitLab push events and ignore branch creation.

  Scenario: Push events happens.

    Given the following generic variables are configured:
      | variable        | expression               | expressionType  | defaultValue | regexpFilter  |
      | object_kind     | $.object_kind            | JSONPath        |              |               |
      | before          | $.before                 | JSONPath        |              |               |
      | after           | $.after                  | JSONPath        |              |               |
      | ref             | $.ref                    | JSONPath        |              |               |
      | git_ssh_url     | $.repository.git_ssh_url | JSONPath        |              |               |

    Given filter is configured with text: $object_kind $before $after
    Given filter is configured with expression: ^push\s(?!0{40}).{40}\s(?!0{40}).{40}$


    When received post content is:
    """
    {
      "object_kind": "push",
      "before": "0000000000000000000000000000000000000000",
      "after": "f34c80f418afb802094f1ba42ad0ec1a20c7a02s",
      "ref": "refs/heads/master",
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is not triggered
    Then variables are resolved to:
      | variable         | value                                    |
      | object_kind      | push                                     |
      | before           | 0000000000000000000000000000000000000000 |
      | after            | f34c80f418afb802094f1ba42ad0ec1a20c7a02s |
      | ref              | refs/heads/master                        |
      | git_ssh_url      | git@example.com:mike/diaspora.git        |


    When received post content is:
    """
    {
      "object_kind": "push",
      "before": "f34c80f418afb802094f1ba42ad0ec1a20c7a02s",
      "after": "0000000000000000000000000000000000000000",
      "ref": "refs/heads/master",
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is not triggered
    Then variables are resolved to:
      | variable         | value                                    |
      | object_kind      | push                                     |
      | before           | f34c80f418afb802094f1ba42ad0ec1a20c7a02s |
      | after            | 0000000000000000000000000000000000000000 |
      | ref              | refs/heads/master                        |
      | git_ssh_url      | git@example.com:mike/diaspora.git        |


    When received post content is:
    """
    {
      "object_kind": "push",
      "before": "0023123123123123123123123123123123101010",
      "after": "f34c80f418afb802094f1ba42ad0ec1a20c7a02s",
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
      | before           | 0023123123123123123123123123123123101010 |
      | after            | f34c80f418afb802094f1ba42ad0ec1a20c7a02s |
      | ref              | refs/heads/master                        |
      | git_ssh_url      | git@example.com:mike/diaspora.git        |
