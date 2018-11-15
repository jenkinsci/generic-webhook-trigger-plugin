Feature: It should be possible to trigger for GitLab push events when specific files are changed.

  Scenario: A build should be triggered when a commit is pushed to specific folder.

    Given the following generic variables are configured:
      | variable        | expression                                     | expressionType  | defaultValue | regexpFilter  |
      | changed_files   | $.commits[*].['modified','added','removed'][*] | JSONPath        |              |               |

    Given filter is configured with text: $changed_files
    Given filter is configured with expression: .*"any/special/folder[^"]+?".*

    When received post content is:
    """
    {
      "commits": [
        {
          "added": ["added/file.txt"],
          "modified": ["modified/file.txt"],
          "removed": ["removed/file.txt"]
        },
        {
          "added": [],
          "modified": ["this/is/modified.txt"],
          "removed": []
        }
      ]
    }
    """
    Then the job is not triggered
    Then variables are resolved to:
      | variable         | value                                                                            |
      | changed_files    | ["modified/file.txt","added/file.txt","removed/file.txt","this/is/modified.txt"] |


    When received post content is:
    """
    {
      "commits": [
        {
          "added": [],
          "modified": ["any/special/folder/thefile.txt"],
          "removed": []
        }
      ]
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable         | value                              |
      | changed_files    | ["any/special/folder/thefile.txt"] |


    When received post content is:
    """
    {
      "commits": [
        {
          "added": [],
          "modified": ["any/not/special/folder/thefile.txt"],
          "removed": []
        }
      ]
    }
    """
    Then the job is not triggered
    Then variables are resolved to:
      | variable         | value                              |
      | changed_files    | ["any/not/special/folder/thefile.txt"] |
