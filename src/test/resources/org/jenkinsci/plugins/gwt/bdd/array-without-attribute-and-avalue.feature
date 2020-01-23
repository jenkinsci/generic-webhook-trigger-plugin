Feature: SHould be possible to trigger only when list contains specific element

  Scenario: Only trigger if list is empty or does not contain attribute value

    Given the following generic variables are configured:
      | variable | expression                                      |
      | docs     | $.labels[?(@.name == "documentation")].length() |
    Given filter is configured with text: $docs
    Given filter is configured with expression: \[\]


    When received post content is:
      """
      { 
         "labels":[ 
            { 
               "id":144,
               "node_id":"MD",
               "url":"https://api.github.com/repos/",
               "name":"bug",
               "color":"d73",
               "default":true,
               "description":"Something isn't working"
            },
            { 
               "id":147,
               "node_id":"MDU",
               "url":"https://api.github.com/repos/",
               "name":"documentation",
               "color":"007a",
               "default":true,
               "description":"Improvements or additions to documentation"
            }
         ]
      }
      """
    Then variables are resolved to:
      | variable | value  |
      | docs     | [7]  |
    Then the job is not triggered


    When received post content is:
      """
      { 
         "labels":[ 
            { 
               "id":144,
               "node_id":"MD",
               "url":"https://api.github.com/repos/",
               "name":"bug",
               "color":"d73",
               "default":true,
               "description":"Something isn't working"
            }
         ]
      }
      """
    Then variables are resolved to:
      | variable | value |
      | docs     | []    |
    Then the job is triggered


    When received post content is:
      """
      { 
         "labels":[
         ]
      }
      """
    Then variables are resolved to:
      | variable | value |
      | docs     | []     |
    Then the job is triggered
