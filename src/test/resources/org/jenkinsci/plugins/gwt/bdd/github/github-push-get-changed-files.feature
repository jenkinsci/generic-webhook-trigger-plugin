Feature: It should be possible to trigger for GitHub push events and get the changed files.

  Scenario: Trigger a job when commit is pushed and get all changed files.

    Given the following generic variables are configured:
      | variable            | expression                                     | expressionType  | defaultValue | regexpFilter  |
      | changed_files       | $.commits[*].['modified','added','removed'][*] | JSONPath        |              |               |

    When received post content is:
    """
    {
      "commits": [
        {
          "added": [
            "an_added_file.md"
          ],
          "removed": [
            "a_removed_file.md"
          ],
          "modified": [
            "a_modified_file.md"
          ]
        },
        {
          "added": [
          ],
          "removed": [
            "another_removed_file.md"
          ],
          "modified": [
          ]
        }
      ]
    }
    """

    Then variables are resolved to:
      | variable            | value                                                                                      |
      | changed_files       | ["a_modified_file.md","an_added_file.md","a_removed_file.md","another_removed_file.md"]    |
      | changed_files_0     | a_modified_file.md                                                                         |
      | changed_files_1     | an_added_file.md                                                                           |
      | changed_files_2     | a_removed_file.md                                                                          |
      | changed_files_3     | another_removed_file.md                                                                    |

