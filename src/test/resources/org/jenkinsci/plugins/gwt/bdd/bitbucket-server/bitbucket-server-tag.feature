Feature: It should be possible to trigger for Bitbucket Server tag events.

  Scenario: Trigger a job when a tag is created.

    Given the following generic variables are configured:
      | variable               | expression                               | expressionType  | defaultValue | regexpFilter  |
      | eventKey               | $.eventKey                               | JSONPath        |              |               |
      | repository_slug        | $.repository.slug                        | JSONPath        |              |               |
      | repository_project_key | $.repository.project.key                 | JSONPath        |              |               |
      | changes_ref_ids        | $.changes[?(@.ref.type == "TAG")].ref.id | JSONPath        |              |               |

    Given filter is configured with text: $eventKey
    Given filter is configured with expression: ^repo:refs_changed$


    When received post content is:
    """
    {
       "eventKey":"repo:refs_changed",
       "date":"2018-09-04T10:10:33+0200",
       "actor":{
          "name":"admin",
          "emailAddress":"admin@example.com",
          "id":1,
          "displayName":"Administrator",
          "active":true,
          "slug":"admin",
          "type":"NORMAL"
       },
       "repository":{
          "slug":"violations-test",
          "id":12,
          "name":"violations-test",
          "scmId":"git",
          "state":"AVAILABLE",
          "statusMessage":"Available",
          "forkable":true,
          "project":{
             "key":"PROJECT_1",
             "id":1,
             "name":"Project 1",
             "description":"Default configuration project #1",
             "public":false,
             "type":"NORMAL"
          },
          "public":false
       },
       "changes":[
          {
             "ref":{
                "id":"refs/tags/hej1",
                "displayId":"hej1",
                "type":"TAG"
             },
             "refId":"refs/tags/hej1",
             "fromHash":"0000000000000000000000000000000000000000",
             "toHash":"d424e4bf83e1c832d7eb5433a36b17fe8b4a5265",
             "type":"ADD"
          }
       ]
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable                 | value                                   |
      | eventKey                 | repo:refs_changed                       |
      | repository_slug          | violations-test                         |
      | repository_project_key   | PROJECT_1                               |
      | changes_ref_ids          | ["refs/tags/hej1"]                      |
      | changes_ref_ids_0        | refs/tags/hej1                          |


  Scenario: Trigger a job when several tags are created.

    Given the following generic variables are configured:
      | variable               | expression                               | expressionType  | defaultValue | regexpFilter  |
      | eventKey               | $.eventKey                               | JSONPath        |              |               |
      | repository_slug        | $.repository.slug                        | JSONPath        |              |               |
      | repository_project_key | $.repository.project.key                 | JSONPath        |              |               |
      | changes_ref_ids        | $.changes[?(@.ref.type == "TAG")].ref.id | JSONPath        |              |               |

    Given filter is configured with text: $eventKey
    Given filter is configured with expression: ^repo:refs_changed$


    When received post content is:
    """
    {
       "eventKey":"repo:refs_changed",
       "date":"2018-09-04T10:10:33+0200",
       "actor":{
          "name":"admin",
          "emailAddress":"admin@example.com",
          "id":1,
          "displayName":"Administrator",
          "active":true,
          "slug":"admin",
          "type":"NORMAL"
       },
       "repository":{
          "slug":"violations-test",
          "id":12,
          "name":"violations-test",
          "scmId":"git",
          "state":"AVAILABLE",
          "statusMessage":"Available",
          "forkable":true,
          "project":{
             "key":"PROJECT_1",
             "id":1,
             "name":"Project 1",
             "description":"Default configuration project #1",
             "public":false,
             "type":"NORMAL"
          },
          "public":false
       },
       "changes":[
          {
             "ref":{
                "id":"refs/tags/hej1",
                "displayId":"hej1",
                "type":"TAG"
             },
             "refId":"refs/tags/hej1",
             "fromHash":"0000000000000000000000000000000000000000",
             "toHash":"d424e4bf83e1c832d7eb5433a36b17fe8b4a5265",
             "type":"ADD"
          },
          {
             "ref":{
                "id":"refs/tags/hej2",
                "displayId":"hej2",
                "type":"TAG"
             },
             "refId":"refs/tags/hej2",
             "fromHash":"0000000000000000000000000000000000000000",
             "toHash":"1234e4bf83e1c832d7eb5433a36b17fe8b4a1414",
             "type":"ADD"
          }
       ]
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable                 | value                                   |
      | eventKey                 | repo:refs_changed                       |
      | repository_slug          | violations-test                         |
      | repository_project_key   | PROJECT_1                               |
      | changes_ref_ids          | ["refs/tags/hej1","refs/tags/hej2"]     |
      | changes_ref_ids_0        | refs/tags/hej1                          |
      | changes_ref_ids_1        | refs/tags/hej2                          |

