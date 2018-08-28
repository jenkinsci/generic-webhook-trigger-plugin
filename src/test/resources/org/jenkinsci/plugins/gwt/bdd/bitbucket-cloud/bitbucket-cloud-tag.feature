Feature: It should be possible to trigger for Bitbucket Cloud tag events.

  Scenario: Trigger a job when a tag is created.

    Given the following generic variables are configured:
      | variable             | expression                                      | expressionType  | defaultValue    | regexpFilter  |
      | repository_name      | $.repository.full_name                          | JSONPath        |                 |               |
      | changes_names        | $.push.changes[?(@.new.type == "tag")].new.name | JSONPath        |                 |               |

    When received post content is:
    """
    {
      "push": {
        "changes": [
          {
            "created": true,
            "new": {
              "name": "this-is-a-tag-1",
              "type": "tag",
              "target": {
                "hash": "1114e4bf83e1c832d7eb5433a36b17fe8b4a5111"
              }
            }
          }
        ]
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
      | variable               | value                                  |
      | repository_name        | tomasbjerre/test                       |
      | changes_names          | ["this-is-a-tag-1"]                    |
      | changes_names_0        | this-is-a-tag-1                        |


  Scenario: Trigger a job when several tags are created.

    Given the following generic variables are configured:
      | variable             | expression                                      | expressionType  | defaultValue    | regexpFilter  |
      | repository_name      | $.repository.full_name                          | JSONPath        |                 |               |
      | changes_names        | $.push.changes[?(@.new.type == "tag")].new.name | JSONPath        |                 |               |

    When received post content is:
    """
    {
      "push": {
        "changes": [
          {
            "created": true,
            "new": {
              "name": "this-is-a-tag-1",
              "type": "tag",
              "target": {
                "hash": "1114e4bf83e1c832d7eb5433a36b17fe8b4a5111"
              }
            }
          },
          {
            "created": true,
            "new": {
              "name": "this-is-a-tag-2",
              "type": "tag",
              "target": {
                "hash": "2224e4bf83e1c832d7eb5433a36b17fe8b4a5222"
              }
            }
          }
        ]
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
      | variable               | value                                  |
      | repository_name        | tomasbjerre/test                       |
      | changes_names          | ["this-is-a-tag-1","this-is-a-tag-2"]  |
      | changes_names_0        | this-is-a-tag-1                        |
      | changes_names_1        | this-is-a-tag-2                        |
