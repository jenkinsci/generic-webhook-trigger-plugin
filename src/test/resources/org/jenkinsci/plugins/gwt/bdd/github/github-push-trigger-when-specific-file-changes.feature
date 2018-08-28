Feature: It should be possible to trigger for GitHub push events when a specific file is changed in a commit.

  Scenario: Trigger a job when commit is pushed and specific file is changed.

    Given the following generic variables are configured:
      | variable            | expression                                     | expressionType  | defaultValue | regexpFilter  |
      | changed_files       | $.commits[*].['modified','added','removed'][*] | JSONPath        |              |               |

    Given filter is configured with:
      | text            | expression                |
      | $changed_files  | "this_file_is_special.md" |


    When received post content is:
    """
    {
      "commits": [
        {
          "added": [
            "this_file_is_special.md"
          ],
          "removed": [
          ],
          "modified": [
          ]
        }
      ]
    }
    """
    Then the job is triggered


    When received post content is:
    """
    {
      "commits": [
        {
          "added": [
            "not_special.md",
            "something.md"
          ],
          "removed": [
            "this_file_is_special.md"
          ],
          "modified": [
            "it_is_modified.md",
            "more_mod.md"
          ]
        }
      ]
    }
    """
    Then the job is triggered


    When received post content is:
    """
    {
      "commits": [
        {
          "added": [
            "not_special.md",
            "something.md"
          ],
          "removed": [
            "this_file.md"
          ],
          "modified": [
            "it_is_modified.md",
            "more_mod.md"
          ]
        }
      ]
    }
    """
    Then the job is not triggered


    When received post content is:
    """
    {
      "commits": [
        {
          "added": [
          ],
          "removed": [
          ],
          "modified": [
            "some_other_file.md"
          ]
        }
      ]
    }
    """
    Then the job is not triggered


  Scenario: Trigger a job when commit is pushed and specific folder is changed.

    Given the following generic variables are configured:
      | variable            | expression                                     | expressionType  | defaultValue | regexpFilter  |
      | changed_files       | $.commits[*].['modified','added','removed'][*] | JSONPath        |              |               |

    Given filter is configured with:
      | text            | expression                |
      | $changed_files  | "folder/subfolder/[^"]+?" |


    When received post content is:
    """
    {
      "commits": [
        {
          "added": [
            "folder/subfolder/this_file_is_special.md"
          ],
          "removed": [
          ],
          "modified": [
          ]
        }
      ]
    }
    """
    Then the job is triggered


    When received post content is:
    """
    {
      "commits": [
        {
          "added": [
            "not_special.md",
            "something.md"
          ],
          "removed": [
            "folder/subfolder/this_file_is_special.md"
          ],
          "modified": [
            "it_is_modified.md",
            "more_mod.md"
          ]
        }
      ]
    }
    """
    Then the job is triggered


    When received post content is:
    """
    {
      "commits": [
        {
          "added": [
            "not_special.md",
            "something.md"
          ],
          "removed": [
            "subfolder/more_mod.md"
          ],
          "modified": [
            "folder/subfolder2/it_is_modified.md"
          ]
        }
      ]
    }
    """
    Then the job is not triggered

