Feature: It should be possible to trigger for GitLab merge request events.

  Scenario: A build should be triggered when references change in a merge request. Not when comments are made.

    Given the following generic variables are configured:
      | variable        | expression                               | expressionType  | defaultValue | regexpFilter  |
      | MR_FROM_URL     | $.object_attributes.source.git_http_url  | JSONPath        |              |               |
      | MR_FROM_BRANCH  | $.object_attributes.source_branch        | JSONPath        |              |               |
      | MR_TO_URL       | $.object_attributes.target.git_http_url  | JSONPath        |              |               |
      | MR_TO_BRANCH    | $.object_attributes.target_branch        | JSONPath        |              |               |
      | MR_PROJECT_ID   | $.object_attributes.target_project_id    | JSONPath        |              |               |
      | MR_IID          | $.object_attributes.iid                  | JSONPath        |              |               |
      | MR_OLD_REV      | $.object_attributes.oldrev               | JSONPath        |              |               |
      | MR_ACTION       | $.object_attributes.action               | JSONPath        |              |               |
      | MR_TITLE        | $.object_attributes.title                | JSONPath        |              |               |
      | MR_STATE        | $.object_attributes.state                | JSONPath        |              |               |
      | MR_OBJECT_KIND  | $.object_kind                            | JSONPath        |              |               |

    Given filter is configured with text: $MR_OBJECT_KIND $MR_ACTION $MR_OLD_REV
    Given filter is configured with expression: ^merge_request\s(update\s.{40}$|open.*)

    When received post content is:
    """
    {  
       "object_kind":"merge_request",
       "object_attributes":{  
          "id":11234,
          "iid":1,
          "target_branch":"master",
          "source_branch":"feature/addingcrap",
          "title":"some crap 2",
          "state":"opened",
          "target_project_id":1,
          "action":"update",
          "oldrev":"7e23c9a980197fe49fae67fb23687c857ff42f86",
          "source":{  
             "git_ssh_url":"git@846c453ea620:root/violations-test.git",
             "git_http_url":"http://846c453ea620/root/violations-test.git",
          },
          "target":{  
             "git_ssh_url":"git@846c453ea620:root/violations-test.git",
             "git_http_url":"http://846c453ea620/root/violations-test.git",
          }
       }
    }
    """

    Then the job is triggered

    Then variables are resolved to:
      | variable         | value                                        |
      | MR_FROM_URL      | http://846c453ea620/root/violations-test.git |
      | MR_FROM_BRANCH   | feature/addingcrap                           |
      | MR_TO_URL        | http://846c453ea620/root/violations-test.git |
      | MR_TO_BRANCH     | master                                       |
      | MR_PROJECT_ID    | 1                                            |
      | MR_IID           | 1                                            |
      | MR_OLD_REV       | 7e23c9a980197fe49fae67fb23687c857ff42f86     |
      | MR_ACTION        | update                                       |
      | MR_TITLE         | some crap 2                                  |
      | MR_STATE         | opened                                       |
      | MR_OBJECT_KIND   | merge_request                                |


    When received post content is:
    """
    {  
       "object_kind":"note",
       "object_attributes":{  
          "iid":1,
          "source":{  
             "git_ssh_url":"git@846c453ea620:root/violations-test.git",
             "git_http_url":"http://846c453ea620/root/violations-test.git",
          },
          "target":{  
             "git_ssh_url":"git@846c453ea620:root/violations-test.git",
             "git_http_url":"http://846c453ea620/root/violations-test.git",
          }
       }
    }
    """

    Then the job is not triggered

    Then variables are resolved to:
      | variable         | value                                        |
      | MR_FROM_URL      | http://846c453ea620/root/violations-test.git |
      | MR_FROM_BRANCH   |                                              |
      | MR_TO_URL        | http://846c453ea620/root/violations-test.git |
      | MR_TO_BRANCH     |                                              |
      | MR_PROJECT_ID    |                                              |
      | MR_IID           | 1                                            |
      | MR_OLD_REV       |                                              |
      | MR_ACTION        |                                              |
      | MR_TITLE         |                                              |
      | MR_STATE         |                                              |
      | MR_OBJECT_KIND   | note                                         |
      
