Feature: It should be possible to trigger for Bitbucket Server pull request events.

  Scenario: Trigger a job when pull request is opened.

    Given the following generic variables are configured:
      | variable                       | expression                                   | expressionType  | defaultValue | regexpFilter  |
      | eventKey                       | $.eventKey                                   | JSONPath        |              |               |
      | pr_id                          | $.pullRequest.id                             | JSONPath        |              |               |
      | pr_state                       | $.pullRequest.state                          | JSONPath        |              |               |
      | pr_title                       | $.pullRequest.title                          | JSONPath        |              |               |
      | pr_from_ref                    | $.pullRequest.fromRef.id                     | JSONPath        |              |               |
      | pr_from_commit                 | $.pullRequest.fromRef.latestCommit           | JSONPath        |              |               |
      | pr_from_repository_slug        | $.pullRequest.fromRef.repository.slug        | JSONPath        |              |               |
      | pr_from_repository_project_key | $.pullRequest.fromRef.repository.project.key | JSONPath        |              |               |
      | pr_to_ref                      | $.pullRequest.toRef.id                       | JSONPath        |              |               |
      | pr_to_commit                   | $.pullRequest.toRef.latestCommit             | JSONPath        |              |               |
      | pr_to_repository_slug          | $.pullRequest.toRef.repository.slug          | JSONPath        |              |               |
      | pr_to_repository_project_key   | $.pullRequest.toRef.repository.project.key   | JSONPath        |              |               |

    Given filter is configured with text: $eventKey
    Given filter is configured with expression: ^pr:opened$


    When received post content is:
    """
    {
      "eventKey":"pr:opened",
      "date":"2017-09-19T09:58:11+1000",
      "actor":{
        "name":"admin",
        "emailAddress":"admin@example.com",
        "id":1,
        "displayName":"Administrator",
        "active":true,
        "slug":"admin",
        "type":"NORMAL"
      },
      "pullRequest":{
        "id":1,
        "version":0,
        "title":"a new file added",
        "state":"OPEN",
        "open":true,
        "closed":false,
        "createdDate":1505779091796,
        "updatedDate":1505779091796,
        "fromRef":{
          "id":"refs/heads/a-branch",
          "displayId":"a-branch",
          "latestCommit":"ef8755f06ee4b28c96a847a95cb8ec8ed6ddd1ca",
          "repository":{
            "slug":"repository",
            "id":84,
            "name":"repository",
            "scmId":"git",
            "state":"AVAILABLE",
            "statusMessage":"Available",
            "forkable":true,
            "project":{
              "key":"PROJ",
              "id":84,
              "name":"project",
              "public":false,
              "type":"NORMAL"
            },
            "public":false
          }
        },
        "toRef":{
          "id":"refs/heads/master",
          "displayId":"master",
          "latestCommit":"178864a7d521b6f5e720b386b2c2b0ef8563e0dc",
          "repository":{
            "slug":"repository",
            "id":84,
            "name":"repository",
            "scmId":"git",
            "state":"AVAILABLE",
            "statusMessage":"Available",
            "forkable":true,
            "project":{
              "key":"PROJ",
              "id":84,
              "name":"project",
              "public":false,
              "type":"NORMAL"
            },
            "public":false
          }
        },
        "locked":false,
        "author":{
          "user":{
            "name":"admin",
            "emailAddress":"admin@example.com",
            "id":1,
            "displayName":"Administrator",
            "active":true,
            "slug":"admin",
            "type":"NORMAL"
          },
          "role":"AUTHOR",
          "approved":false,
          "status":"UNAPPROVED"
        },
        "reviewers":[

        ],
        "participants":[

        ],
        "links":{
          "self":[
            null
          ]
        }
      }
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable                       | value                                    |
      | eventKey                       | pr:opened                                |
      | pr_id                          | 1                                        |
      | pr_state                       | OPEN                                     |
      | pr_title                       | a new file added                         |
      | pr_from_ref                    | refs/heads/a-branch                      |
      | pr_from_commit                 | ef8755f06ee4b28c96a847a95cb8ec8ed6ddd1ca |
      | pr_from_repository_slug        | repository                               |
      | pr_from_repository_project_key | PROJ                                     |
      | pr_to_ref                      | refs/heads/master                        |
      | pr_to_commit                   | 178864a7d521b6f5e720b386b2c2b0ef8563e0dc |
      | pr_to_repository_slug          | repository                               |
      | pr_to_repository_project_key   | PROJ                                     |
