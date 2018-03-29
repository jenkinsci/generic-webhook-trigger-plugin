Feature: It should be possible to avoid triggering jobs if a filter string is not matching a filter regexp.

  Scenario: Job should only be triggered if user is not baxterthehacker

    Given filter is configured with:
      | text                 | expression              |
      | baxterthehacker      | ^((?!baxterthehacker))  |
    Then the job is not triggered

    Given filter is configured with:
      | text                 | expression              |
      | Tomas                | ^((?!baxterthehacker))  |
    Then the job is triggered


  Scenario: Job should only be triggered if user is baxterthehacker

    Given filter is configured with:
      | text                 | expression         |
      | baxterthehacker      | ^baxterthehacker$  |
    Then the job is triggered

    Given filter is configured with:
      | text                 | expression         |
      | Tomas                | ^baxterthehacker$  |
    Then the job is not triggered


  Scenario: Job should only be triggered if user jenkins pushed to master

    Given filter is configured with:
      | text                             | expression                   |
      | refs/heads/master jenkins        | ^refs/heads/master jenkins$  |
    Then the job is triggered

    Given filter is configured with:
      | text                             | expression                   |
      | refs/heads/develop jenkins       | ^refs/heads/master jenkins$  |
    Then the job is not triggered


  Scenario: Job should only be triggered if user not jenkins pushed to develop

    Given filter is configured with:
      | text                             | expression                         |
      | refs/heads/develop jenkins       | ^refs/heads/develop ((?!jenkins))  |
    Then the job is not triggered

    Given filter is configured with:
      | text                             | expression                         |
      | refs/heads/develop tomas         | ^refs/heads/develop ((?!jenkins))  |
    Then the job is triggered
    