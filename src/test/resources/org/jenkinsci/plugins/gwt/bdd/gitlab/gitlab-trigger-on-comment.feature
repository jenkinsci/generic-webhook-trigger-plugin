Feature: It should be possible to trigger for GitLab merge request note events.

  Scenario: A build should be triggered when a comment (note) is made on a merge request.

    Given the following generic variables are configured:
      | variable        | expression                        |
      | object_kind     | $.object_kind                     |
      | project_id      | $.project.id                      |
      | mr_id           | $.merge_request.id                |
      | mr_iid          | $.merge_request.iid               |
      | noteable_type   | $.object_attributes.noteable_type |
      | note            | $.object_attributes.note          |
      | ref             | $.ref                             |
      | git_ssh_url     | $.repository.git_ssh_url          |
      | after           | $.after                           |

    Given filter is configured with text: $object_kind $noteable_type $note
    Given filter is configured with expression: ^note\sMergeRequest\splease rebuild this$


    When received post content is:
    """
    {
      "object_kind": "note",
      "after": "82b3d5ae55f7080f1e6022629cdb57bfae7cccc7",
      "ref": "refs/heads/master",
      "project":{
       "id": 5
      },
      "object_attributes": {
       "id": 1244,
       "note": "please rebuild this",
       "noteable_type": "Commit"
      },
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is not triggered
    Then variables are resolved to:
      | variable         | value                                    |
      | object_kind      | note                                     |
      | project_id       | 5                                        |
      | mr_id            |                                          |
      | mr_iid           |                                          |
      | noteable_type    | Commit                                   |
      | note             | please rebuild this                      |
      | ref              | refs/heads/master                        |
      | git_ssh_url      | git@example.com:mike/diaspora.git        |
      | after            | 82b3d5ae55f7080f1e6022629cdb57bfae7cccc7 |


    When received post content is:
    """
    {
      "object_kind": "note",
      "after": "82b3d5ae55f7080f1e6022629cdb57bfae7cccc7",
      "ref": "refs/heads/master",
      "project":{
       "id": 5
      },
      "object_attributes": {
       "id": 1244,
       "note": "please rebuild this",
       "noteable_type": "MergeRequest"
      },
      "merge_request": {
       "id": 7,
       "iid": 1,
      },
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable         | value                                    |
      | object_kind      | note                                     |
      | project_id       | 5                                        |
      | mr_id            | 7                                        |
      | mr_iid           | 1                                        |
      | noteable_type    | MergeRequest                             |
      | note             | please rebuild this                      |
      | ref              | refs/heads/master                        |
      | git_ssh_url      | git@example.com:mike/diaspora.git        |
      | after            | 82b3d5ae55f7080f1e6022629cdb57bfae7cccc7 |


    When received post content is:
    """
    {
      "object_kind": "note",
      "after": "82b3d5ae55f7080f1e6022629cdb57bfae7cccc7",
      "ref": "refs/heads/master",
      "project":{
       "id": 5
      },
      "object_attributes": {
       "id": 1244,
       "note": "please build this",
       "noteable_type": "MergeRequest"
      },
      "merge_request": {
       "id": 7,
       "iid": 1,
      },
      "repository":{
        "git_ssh_url":"git@example.com:mike/diaspora.git",
      }
    }
    """
    Then the job is not triggered
    Then variables are resolved to:
      | variable         | value                                    |
      | object_kind      | note                                     |
      | project_id       | 5                                        |
      | mr_id            | 7                                        |
      | mr_iid           | 1                                        |
      | noteable_type    | MergeRequest                             |
      | note             | please build this                        |
      | ref              | refs/heads/master                        |
      | git_ssh_url      | git@example.com:mike/diaspora.git        |
      | after            | 82b3d5ae55f7080f1e6022629cdb57bfae7cccc7 |