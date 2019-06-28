Feature: It should be possible to trigger for Bitbucket Server on regs_changed.

  Scenario: Trigger a job when a commit is pushed.

    Given the following generic variables are configured:
      | variable | expression             | expressionType  | defaultValue | regexpFilter                        |
      | refsb    | $.changes[0].ref.id    | JSONPath        |              | ^(refs/heads/\|refs/remotes/origin/) |
      | refsbc   | $.pullRequest.toRef.id | JSONPath        |              | ^(refs/heads/\|refs/remotes/origin/) |

    Given filter is configured with text: $refsb$refsbc
    Given filter is configured with expression: ^(test)*?$


    When received post content is:
    """
	{
	  "eventKey": "repo:refs_changed",
	  "date": "2019-06-28T11:05:24+0000",
	  "actor": {
	    "name": "xxxxxxx",
	    "emailAddress": "xxxxxxxxxxxxxxxxxx",
	    "id": 563,
	    "displayName": "xxxxxxxxxxxxxxxxx",
	    "active": true,
	    "slug": "xxxxxxx",
	    "type": "NORMAL",
	    "links": {
	      "self": [
	        {
	          "href": "https:\/\/xxxxxxxxxxxxxxxxxxxxxx"
	        }
	      ]
	    }
	  },
	  "repository": {
	    "slug": "xxxxx-xxxx",
	    "id": 80,
	    "name": "xxx-xxxxx",
	    "scmId": "git",
	    "state": "AVAILABLE",
	    "statusMessage": "Available",
	    "forkable": true,
	    "project": {
	      "key": "xxx",
	      "id": 128,
	      "name": "xxxx",
	      "description": "A Bitbucket t.",
	      "public": false,
	      "type": "NORMAL",
	      "links": {
	        "self": [
	          {
	            "href": "https:\/\/xxxxxxxxxxxxxx"
	          }
	        ]
	      }
	    },
	    "public": false,
	    "links": {
	      "clone": [
	        {
	          "href": "xxxxxxxxxxxxxxxxxx",
	          "name": "http"
	        },
	        {
	          "href": "xxxxxxxxxxxxxxxxxx",
	          "name": "ssh"
	        }
	      ],
	      "self": [
	        {
	          "href": "xxxxxxxxxxx"
	        }
	      ]
	    }
	  },
	  "changes": [
	    {
	      "ref": {
	        "id": "refs\/heads\/test",
	        "displayId": "test",
	        "type": "BRANCH"
	      },
	      "refId": "refs\/heads\/test",
	      "fromHash": "xxxxxxxxxxxxxx",
	      "toHash": "xxxxxxxxxxxxxxxxx",
	      "type": "UPDATE"
	    }
	  ]
	}
    """
    
    Then the job is triggered
    
    Then filter text is rendered to: test 
    
    Then variables are resolved to:
      | variable | value                                   |
      | refsb    | test                                    |
      | refsbc   |                                         |

