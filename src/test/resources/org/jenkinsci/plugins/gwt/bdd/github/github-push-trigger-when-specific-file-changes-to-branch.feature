Feature: It should be possible to trigger for GitHub push events when a specific file is changed in a commit.

  Scenario: Trigger a job when commit is pushed and specific file is changed.

    Given the following generic variables are configured:
      | variable            | expression                                     | expressionType  | defaultValue | regexpFilter  |
      | changed_files       | $.commits[*].['modified','added','removed'][*] | JSONPath        |              |               |
      | ref                 | $.ref                                          | JSONPath        |              |               |

    Given filter is configured with:
      | text                 | expression                                               |
      | $ref $changed_files  | refs/heads/testing_generic .*"localDev/gateway/[^"]+?".* |


    When received post content is:
    """
    {
       "ref":"refs/heads/testing_generic",
       "commits":[
          {
             "added":[
             ],
             "removed":[
             ],
             "modified":[
                "localDev/gateway/a"
             ]
          }
       ]
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable            | value                      |
      | changed_files       | ["localDev/gateway/a"]     |
      | ref                 | refs/heads/testing_generic |


    When received post content is:
    """
    {
       "ref":"refs/heads/testing_generic",
       "commits":[
          {
             "added":[
                "localDev/fgdf/a"
             ],
             "removed":[
                "localDev/fgdf/b"
             ],
             "modified":[
                "localDev/gateway/a",
                "localDev/gateway/b"
             ]
          }
       ]
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable            | value                                                                           |
      | changed_files       | ["localDev/gateway/a","localDev/gateway/b","localDev/fgdf/a","localDev/fgdf/b"] |
      | ref                 | refs/heads/testing_generic                                                      |


    When received post content is:
    """
    {
       "ref":"refs/heads/testing_generic",
       "commits":[
          {
             "added":[

             ],
             "removed":[
                "localDev/fgdf/b"
             ],
             "modified":[
                "localDev/asdf/a"
             ]
          }
       ]
    }
    """
    Then the job is not triggered

