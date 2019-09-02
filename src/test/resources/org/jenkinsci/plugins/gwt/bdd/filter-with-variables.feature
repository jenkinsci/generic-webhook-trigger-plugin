Feature: It should be possible to use variables resolved from post content.

  Scenario: Filter feature is used with variables from json

    Given the following generic variables are configured:
      | variable            | expression                        | expressionType  | defaultValue | regexpFilter  |
      | committer_name      | $.head_commit.committer.name      | JSONPath        |              |               |
      | committer_username  | $.head_commit.committer.username  | JSONPath        |              |               |
      | ref                 | $.ref                             | JSONPath        |              |               |

    When received post content is:
    """
    {
      "ref": "refs/heads/develop",
      "head_commit": {
        "committer": {
          "name": "baxterthehacker",
          "username": "Baxter the Hacker"
        }
      }
    }
    """


    Then variables are resolved to:
      | variable            | value              |
      | committer_name      | baxterthehacker    |
      | committer_username  | Baxter the Hacker  |
      | ref                 | refs/heads/develop |


    Given filter is configured with:
      | text                         | expression                         |
      | $ref $committer_name         | ^refs/heads/develop ((?!jenkins))  |
    Then the job is triggered

    Given filter is configured with:
      | text                             | expression                        |
      | $ref $committer_name             | ^refs/heads/master ((?!jenkins))  |
    Then the job is not triggered

    Given filter is configured with:
      | text                             | expression                         |
      | $ref jenkins                     | ^refs/heads/develop ((?!jenkins))  |
    Then the job is not triggered

    Given filter is configured with:
      | text                             | expression                         |
      | $ref Tomas                       | ^refs/heads/develop ((?!jenkins))  |
    Then the job is triggered

    Given filter is configured with:
      | text                 | expression       |
      | $committer_name      | baxterthehacker  |
    Then the job is triggered

    Given filter is configured with:
      | text                 | expression       |
      | $committer_username  | baxterthehacker  |
    Then the job is not triggered

    Given filter is configured with:
      | text                 | expression       |
      | $committer_name      | tomas            |
    Then the job is not triggered


  Scenario: Matched content contains newlines

    Given the following generic variables are configured:
      | variable         | expression | expressionType  | defaultValue | regexpFilter  |
      | messageFiltered  | $.message  | JSONPath        |              | [\r\n]        |

    When received post content is:
    """
    {
      "message": "first abc\nabc second def\n abc third"
    }
    """

    Then variables are resolved to:
      | variable            | value              |
      | messageFiltered     | first abcabc second def abc third  |

    Given filter is configured with:
      | text             | expression        |
      | $messageFiltered | ^(?!.*(first)).* |
    Then the job is not triggered

    Given filter is configured with:
      | text             | expression        |
      | $messageFiltered | ^(?!.*(second)).* |
    Then the job is not triggered

    Given filter is configured with:
      | text             | expression        |
      | $messageFiltered | ^(?!.*(third)).* |
    Then the job is not triggered

    Given filter is configured with:
      | text             | expression        |
      | $messageFiltered | ^(?!.*(fourth)).* |
    Then the job is triggered
