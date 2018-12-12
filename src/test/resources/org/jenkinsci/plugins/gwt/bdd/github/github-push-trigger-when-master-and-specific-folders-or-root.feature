Feature: It should be possible to trigger for GitHub push events when specific folders, in the root folder, is changed. Or if the files in root folder is changed.

  Scenario: Pushes are made to the repo.

    Given the following generic variables are configured:
      | variable      | expression                                     | expressionType  |
      | changed_files | $.commits[*].['modified','added','removed'][*] | JSONPath        |
      | ref           | $.ref                                          | JSONPath        |
    Given variable ref has regexpFilter: ^(refs/heads/|refs/remotes/origin/)

    Given filter is configured with text: $ref $changed_files
    Given filter is configured with expression: master\s((.*"(books/|common/|sqs/|security/|common-test/|db/)[^"]+?".)|(."[^/"]+".*))


    When received post content is:
    """
    {
       "ref":"refs/heads/master",
       "commits":[
          {
             "added":[
             ],
             "removed":[
             ],
             "modified":[
                "a_file_in_root.txt"
             ]
          }
       ]
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable            | value                      |
      | changed_files       | ["a_file_in_root.txt"]     |
      | ref                 | master                     |


    When received post content is:
    """
    {
       "ref":"refs/heads/master",
       "commits":[
          {
             "added":[
             ],
             "removed":[
             ],
             "modified":[
                "someotherfolder/a_file_in_root.txt"
             ]
          }
       ]
    }
    """
    Then the job is not triggered
    Then variables are resolved to:
      | variable            | value                                      |
      | changed_files       | ["someotherfolder/a_file_in_root.txt"]     |
      | ref                 | master                                     |


    When received post content is:
    """
    {
       "ref":"refs/heads/feature",
       "commits":[
          {
             "added":[
             ],
             "removed":[
             ],
             "modified":[
                "a_file_in_root.txt"
             ]
          }
       ]
    }
    """
    Then the job is not triggered
    Then variables are resolved to:
      | variable            | value                                      |
      | changed_files       | ["a_file_in_root.txt"]                     |
      | ref                 | feature                                    |


    When received post content is:
    """
    {
       "ref":"refs/heads/master",
       "commits":[
          {
             "added":[
             ],
             "removed":[
             ],
             "modified":[
                "books/a_file_in_root.txt"
             ]
          }
       ]
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable            | value                                      |
      | changed_files       | ["books/a_file_in_root.txt"]               |
      | ref                 | master                                     |


    When received post content is:
    """
    {
       "ref":"refs/heads/master",
       "commits":[
          {
             "added":[
             ],
             "removed":[
             ],
             "modified":[
                "books/subfolder/and/more/a_file_in_root.txt"
             ]
          }
       ]
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable            | value                                           |
      | changed_files       | ["books/subfolder/and/more/a_file_in_root.txt"] |
      | ref                 | master                                          |


    When received post content is:
    """
    {
       "ref":"refs/heads/master",
       "commits":[
          {
             "added":[
             ],
             "removed":[
             ],
             "modified":[
                "books/subfolder/and/more/a_file_in_root.txt",
                "another/folder/file.txt"
             ]
          }
       ]
    }
    """
    Then the job is triggered
    Then variables are resolved to:
      | variable            | value                                                                     |
      | changed_files       | ["books/subfolder/and/more/a_file_in_root.txt","another/folder/file.txt"] |
      | ref                 | master                                                                    |