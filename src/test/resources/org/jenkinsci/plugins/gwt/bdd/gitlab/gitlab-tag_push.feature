Feature: It should be possible to trigger for GitLab tag events.

  Scenario: A build should be triggered when tag is created, not when it is removed.

    Given the following generic variables are configured:
      | variable        | expression               | expressionType  | defaultValue | regexpFilter  |
      | object_kind     | $.object_kind            | JSONPath        |              |               |
      | before          | $.before                 | JSONPath        |              |               |
      | after           | $.after                  | JSONPath        |              |               |
      | ref             | $.ref                    | JSONPath        |              |               |
      | git_ssh_url     | $.repository.git_ssh_url | JSONPath        |              |               |

    Given filter is configured with text: $object_kind $before $after
    Given filter is configured with expression: ^tag_push\s0{40}\s.{40}$

    When received post content is:
    """
    {
      "object_kind": "tag_push",
      "before": "0000000000000000000000000000000000000000",
      "after": "82b3d5ae55f7080f1e6022629cdb57bfae7cccc7",
      "ref": "refs/tags/v1.0.0",
      "repository":{
        "git_ssh_url":"git@example.com:jsmith/example.git",
      }
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable         | value                                    |
      | object_kind      | tag_push                                 |
      | before           | 0000000000000000000000000000000000000000 |
      | after            | 82b3d5ae55f7080f1e6022629cdb57bfae7cccc7 |
      | ref              | refs/tags/v1.0.0                         |
      | git_ssh_url      | git@example.com:jsmith/example.git       |


    When received post content is:
    """
    {
      "object_kind": "tag_push",
      "before": "82b3d5ae55f7080f1e6022629cdb57bfae7cccc7",
      "after": "0000000000000000000000000000000000000000",
      "ref": "refs/tags/v1.0.0",
      "repository":{
        "git_ssh_url":"git@example.com:jsmith/example.git",
      }
    }
    """
    Then the job is not triggered
