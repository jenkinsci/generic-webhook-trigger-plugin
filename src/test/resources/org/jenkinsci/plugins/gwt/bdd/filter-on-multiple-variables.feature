Feature: Combine variables

  Scenario: Having AND and OR relations between variables in filter.

    Given the following generic variables are configured:
      | variable | expression | expressionType | defaultValue | regexpFilter |
      | action   | $.action   | JSONPath       |              |              |
      | merged   | $.merged   | JSONPath       |              |              |

    Given filter is configured with text: $action $merged
    Given filter is configured with expression: ^opened false$|^reopened false$|^synchronize false$|^closed true$


    When received post content is:
      """
      {
      "action": "opened",
      "merged": "false",
      }
      """
    Then variables are resolved to:
      | variable | value  |
      | action   | opened |
      | merged   | false  |
    Then the job is triggered


    When received post content is:
      """
      {
      "action": "opened",
      "merged": "true",
      }
      """
    Then variables are resolved to:
      | variable | value  |
      | action   | opened |
      | merged   | true   |
    Then the job is not triggered
