Feature: It should be possible to trigger for GitHub pull request events.

  Scenario: Trigger a job when pull request is opened, reopened and synchronized.

    Given the following generic variables are configured:
      | variable        | expression                       | expressionType  | defaultValue | regexpFilter  |
      | action          | $.action                         | JSONPath        |              |               |
      | pr_id           | $.pull_request.id                | JSONPath        |              |               |
      | pr_state        | $.pull_request.state             | JSONPath        |              |               |
      | pr_title        | $.pull_request.title             | JSONPath        |              |               |
      | pr_from_ref     | $.pull_request.head.ref          | JSONPath        |              |               |
      | pr_from_sha     | $.pull_request.head.sha          | JSONPath        |              |               |
      | pr_from_git_url | $.pull_request.head.repo.git_url | JSONPath        |              |               |
      | pr_to_ref       | $.pull_request.base.ref          | JSONPath        |              |               |
      | pr_to_sha       | $.pull_request.base.sha          | JSONPath        |              |               |
      | pr_to_git_url   | $.pull_request.base.repo.git_url | JSONPath        |              |               |
      | repo_git_url    | $.repository.git_url             | JSONPath        |              |               |

    Given filter is configured with text: $action
    Given filter is configured with expression: ^(opened|reopened|synchronize)$


    When received post content is:
    """
        {
          "action": "opened",
          "pull_request": {
            "id": 212577079,
            "state": "open",
            "title": "Feature/testing webhooks",
            "head": {
              "ref": "feature/testing-webhooks",
              "sha": "187ccb978925d6ccdf948a58bccaf878cce046b9",
              "repo": {
                "git_url": "git://github.com/tomasbjerre/violations-test.git",
                "ssh_url": "git@github.com:tomasbjerre/violations-test.git",
                "clone_url": "https://github.com/tomasbjerre/violations-test.git"
              }
            },
            "base": {
              "ref": "feature/addingcrap-2",
              "sha": "30fda2e3e97c773e587dfae3066d4a904516e7d8",
              "repo": {
                "git_url": "git://github.com/tomasbjerre/violations-test.git",
                "ssh_url": "git@github.com:tomasbjerre/violations-test.git",
                "clone_url": "https://github.com/tomasbjerre/violations-test.git"
              }
            }
          },
          "repository": {
            "git_url": "git://github.com/tomasbjerre/violations-test.git",
            "ssh_url": "git@github.com:tomasbjerre/violations-test.git",
            "clone_url": "https://github.com/tomasbjerre/violations-test.git"
          },
        }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable        | value                                            |
      | action          | opened                                           |
      | pr_id           | 212577079                                        |
      | pr_state        | open                                             |
      | pr_title        | Feature/testing webhooks                         |
      | pr_from_ref     | feature/testing-webhooks                         |
      | pr_from_sha     | 187ccb978925d6ccdf948a58bccaf878cce046b9         |
      | pr_from_git_url | git://github.com/tomasbjerre/violations-test.git |
      | pr_to_ref       | feature/addingcrap-2                             |
      | pr_to_sha       | 30fda2e3e97c773e587dfae3066d4a904516e7d8         |
      | pr_to_git_url   | git://github.com/tomasbjerre/violations-test.git |
      | repo_git_url    | git://github.com/tomasbjerre/violations-test.git |


    When received post content is:
    """
        {
          "action": "reopened",
          "pull_request": {
            "id": 212577079,
            "state": "open",
            "title": "Feature/testing webhooks",
            "head": {
              "ref": "feature/testing-webhooks",
              "sha": "187ccb978925d6ccdf948a58bccaf878cce046b9",
              "repo": {
                "git_url": "git://github.com/tomasbjerre/violations-test.git",
                "ssh_url": "git@github.com:tomasbjerre/violations-test.git",
                "clone_url": "https://github.com/tomasbjerre/violations-test.git",
              }
            },
            "base": {
              "ref": "feature/addingcrap-2",
              "sha": "30fda2e3e97c773e587dfae3066d4a904516e7d8",
              "repo": {
                "git_url": "git://github.com/tomasbjerre/violations-test.git",
                "ssh_url": "git@github.com:tomasbjerre/violations-test.git",
                "clone_url": "https://github.com/tomasbjerre/violations-test.git",
              }
            }
          },
          "repository": {
            "git_url": "git://github.com/tomasbjerre/violations-test.git",
            "ssh_url": "git@github.com:tomasbjerre/violations-test.git",
            "clone_url": "https://github.com/tomasbjerre/violations-test.git",
          },
        }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable        | value                                            |
      | action          | reopened                                         |
      | pr_id           | 212577079                                        |
      | pr_state        | open                                             |
      | pr_title        | Feature/testing webhooks                         |
      | pr_from_ref     | feature/testing-webhooks                         |
      | pr_from_sha     | 187ccb978925d6ccdf948a58bccaf878cce046b9         |
      | pr_from_git_url | git://github.com/tomasbjerre/violations-test.git |
      | pr_to_ref       | feature/addingcrap-2                             |
      | pr_to_sha       | 30fda2e3e97c773e587dfae3066d4a904516e7d8         |
      | pr_to_git_url   | git://github.com/tomasbjerre/violations-test.git |
      | repo_git_url    | git://github.com/tomasbjerre/violations-test.git |


    When received post content is:
    """
        {
          "action": "synchronize",
          "pull_request": {
            "id": 212577079,
            "state": "open",
            "title": "Feature/testing webhooks",
            "head": {
              "ref": "feature/testing-webhooks",
              "sha": "979a59cccc7c3526fb3fc0ca71753d04271d278b",
              "repo": {
                "git_url": "git://github.com/tomasbjerre/violations-test.git",
                "ssh_url": "git@github.com:tomasbjerre/violations-test.git",
                "clone_url": "https://github.com/tomasbjerre/violations-test.git",
              }
            },
            "base": {
              "ref": "feature/addingcrap-2",
              "sha": "30fda2e3e97c773e587dfae3066d4a904516e7d8",
              "repo": {
                "git_url": "git://github.com/tomasbjerre/violations-test.git",
                "ssh_url": "git@github.com:tomasbjerre/violations-test.git",
                "clone_url": "https://github.com/tomasbjerre/violations-test.git",
              }
            }
          },
          "before": "187ccb978925d6ccdf948a58bccaf878cce046b9",
          "after": "979a59cccc7c3526fb3fc0ca71753d04271d278b",
          "repository": {
            "git_url": "git://github.com/tomasbjerre/violations-test.git",
            "ssh_url": "git@github.com:tomasbjerre/violations-test.git",
            "clone_url": "https://github.com/tomasbjerre/violations-test.git",
          }
        }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable        | value                                            |
      | action          | synchronize                                      |
      | pr_id           | 212577079                                        |
      | pr_state        | open                                             |
      | pr_title        | Feature/testing webhooks                         |
      | pr_from_ref     | feature/testing-webhooks                         |
      | pr_from_sha     | 979a59cccc7c3526fb3fc0ca71753d04271d278b         |
      | pr_from_git_url | git://github.com/tomasbjerre/violations-test.git |
      | pr_to_ref       | feature/addingcrap-2                             |
      | pr_to_sha       | 30fda2e3e97c773e587dfae3066d4a904516e7d8         |
      | pr_to_git_url   | git://github.com/tomasbjerre/violations-test.git |
      | repo_git_url    | git://github.com/tomasbjerre/violations-test.git |
