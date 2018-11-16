Feature: It should be possible to trigger for GitLab push events and ignore branch creation.

  Scenario: Push events happens.

    Given the following generic variables are configured:
      | variable            | expression               | expressionType  | defaultValue | regexpFilter  |
      | object_kind         | $.object_kind            | JSONPath        |              |               |
      | after               | $.after                  | JSONPath        |              |               |
      | total_commits_count | $.total_commits_count    | JSONPath        |              |               |
      | ref                 | $.ref                    | JSONPath        |              |               |
      | git_ssh_url         | $.repository.git_ssh_url | JSONPath        |              |               |

    Given filter is configured with text: $object_kind $after $total_commits_count
    Given filter is configured with expression: ^push\s.{40}\s[^0].*


    When received post content is:
    """
    {
      "object_kind": "push",
      "after": "f34c80f418afb802094f1ba42ad0ec1a20c7a02s",
      "ref": "refs/heads/master",
      "total_commits_count": 0,
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is not triggered
    Then variables are resolved to:
      | variable            | value                                    |
      | object_kind         | push                                     |
      | after               | f34c80f418afb802094f1ba42ad0ec1a20c7a02s |
      | total_commits_count | 0                                        |
      | ref                 | refs/heads/master                        |
      | git_ssh_url         | git@example.com:mike/diaspora.git        |


    When received post content is:
    """
    {
      "object_kind": "push",
      "after": "f34c80f418afb802094f1ba42ad0ec1a20c7a02s",
      "ref": "refs/heads/master",
      "total_commits_count": 1,
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable            | value                                    |
      | object_kind         | push                                     |
      | after               | f34c80f418afb802094f1ba42ad0ec1a20c7a02s |
      | total_commits_count | 1                                        |
      | ref                 | refs/heads/master                        |
      | git_ssh_url         | git@example.com:mike/diaspora.git        |


    When received post content is:
    """
    {
      "object_kind": "push",
      "after": "f34c80f418afb802094f1ba42ad0ec1a20c7a02s",
      "ref": "refs/heads/master",
      "total_commits_count": 10,
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable            | value                                    |
      | object_kind         | push                                     |
      | after               | f34c80f418afb802094f1ba42ad0ec1a20c7a02s |
      | total_commits_count | 10                                       |
      | ref                 | refs/heads/master                        |
      | git_ssh_url         | git@example.com:mike/diaspora.git        |


    When received post content is:
    """
    {
      "object_kind": "push",
      "after": "f34c80f418afb802094f1ba42ad0ec1a20c7a02s",
      "ref": "refs/heads/master",
      "total_commits_count": 123,
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable            | value                                    |
      | object_kind         | push                                     |
      | after               | f34c80f418afb802094f1ba42ad0ec1a20c7a02s |
      | total_commits_count | 123                                      |
      | ref                 | refs/heads/master                        |
      | git_ssh_url         | git@example.com:mike/diaspora.git        |
