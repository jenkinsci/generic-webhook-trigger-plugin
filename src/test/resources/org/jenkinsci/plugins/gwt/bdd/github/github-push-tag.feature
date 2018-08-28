Feature: It should be possible to trigger for GitHub tag push events.

  Scenario: Trigger a job when tag is pushed

    Given the following generic variables are configured:
      | variable          | expression            | expressionType  | defaultValue | regexpFilter  |
      | ref               | $.ref                 | JSONPath        |              |               |
      | commit            | $.after               | JSONPath        |              |               |
      | ssh_url           | $.repository.ssh_url  | JSONPath        |              |               |

    Given filter is configured with text: $ref
    Given filter is configured with expression: ^(refs/tags/.+)$


    When received post content is:
    """
    {
       "ref":"refs/tags/this-is-a-tag",
       "after":"e961b09e4f432f3b1ad2690fd63820e9b8161128",
       "repository":{
          "git_url":"git://github.com/tomasbjerre/Tomas_Bjerre_AB.git",
          "ssh_url":"git@github.com:tomasbjerre/Tomas_Bjerre_AB.git",
          "clone_url":"https://github.com/tomasbjerre/Tomas_Bjerre_AB.git",
       },
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable    | value                                          |
      | ref         | refs/tags/this-is-a-tag                        |
      | commit      | e961b09e4f432f3b1ad2690fd63820e9b8161128       |
      | ssh_url     | git@github.com:tomasbjerre/Tomas_Bjerre_AB.git |


    When received post content is:
    """
    {
       "ref": "refs/heads/feature/jira-123-impl-stuff",
       "after":"e961b09e4f432f3b1ad2690fd63820e9b8161128",
       "repository":{
          "git_url":"git://github.com/tomasbjerre/Tomas_Bjerre_AB.git",
          "ssh_url":"git@github.com:tomasbjerre/Tomas_Bjerre_AB.git",
          "clone_url":"https://github.com/tomasbjerre/Tomas_Bjerre_AB.git",
       },
    }
    """
    Then the job is not triggered
