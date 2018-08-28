Feature: It should be possible to trigger for Bitbucket Cloud pull request events.

  Scenario: Trigger a job when pull request is created or updated.

    Given the following generic variables are configured:
      | variable           | expression                                     | expressionType  | defaultValue | regexpFilter  |
      | pr_id              | $.pullrequest.id                               | JSONPath        |              |               |
      | pr_title           | $.pullrequest.title                            | JSONPath        |              |               |
      | pr_from_branch     | $.pullrequest.source.branch.name               | JSONPath        |              |               |
      | pr_from_commit     | $.pullrequest.source.commit.hash               | JSONPath        |              |               |
      | pr_from_repository | $.pullrequest.source.repository.full_name      | JSONPath        |              |               |
      | pr_to_branch       | $.pullrequest.destination.branch.name          | JSONPath        |              |               |
      | pr_to_commit       | $.pullrequest.destination.commit.hash          | JSONPath        |              |               |
      | pr_to_repository   | $.pullrequest.destination.repository.full_name | JSONPath        |              |               |
      | repository         | $.repository.full_name                         | JSONPath        |              |               |


    When received post content is:
    """
    {
      "pullrequest": {
        "title": "Feature/addingcrap",
        "destination": {
          "commit": {
            "hash": "187ccb978925"
          },
          "branch": {
            "name": "master"
          },
          "repository": {
            "full_name": "tomasbjerre/test"
          }
        },
        "source": {
          "commit": {
            "hash": "2a7ae003d5db"
          },
          "branch": {
            "name": "feature/addingcrap"
          },
          "repository": {
            "full_name": "tomasbjerre/test"
          }
        },
        "state": "OPEN",
        "id": 1
      },
      "repository": {
        "scm": "git",
        "name": "test",
        "full_name": "tomasbjerre/test"
      }
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable           | value              |
      | pr_id              | 1                  |
      | pr_title           | Feature/addingcrap |
      | pr_from_branch     | feature/addingcrap |
      | pr_from_commit     | 2a7ae003d5db       |
      | pr_from_repository | tomasbjerre/test   |
      | pr_to_branch       | master             |
      | pr_to_commit       | 187ccb978925       |
      | pr_to_repository   | tomasbjerre/test   |
      | repository         | tomasbjerre/test   |
