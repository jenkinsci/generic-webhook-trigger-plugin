# Generic Webhook Trigger Plugin

[![Build Status](https://ci.jenkins.io/job/Plugins/job/generic-webhook-trigger-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/generic-webhook-trigger-plugin)

This is a Jenkins plugin that can:

 1. Receive any HTTP request, JENKINS_URL/generic-webhook-trigger/invoke
 2. Extract values

  * From POST content with [JSONPath](https://github.com/json-path/JsonPath) or [XPath](https://www.w3schools.com/xml/xpath_syntax.asp)
  * From the query parameters
  * From the headers

 3. Trigger a build with those values contribute as variables

There is an optional feature to trigger jobs only if a supplied regular expression matches the extracted variables. Here is an example, let's say the post content looks like this:
```javascript
{
  "before": "1848f1236ae15769e6b31e9c4477d8150b018453",
  "after": "5cab18338eaa83240ab86c7b775a9b27b51ef11d",
  "ref": "refs/heads/develop"
}
```

Then you can have a variable, resolved from post content, named `ref` of type `JSONPath` and with expression like `$.ref` . The optional filter text can be set to `$ref` and the filter regexp set to [^(refs/heads/develop|refs/heads/feature/.+)$](https://jex.im/regulex/#!embed=false&flags=&re=%5E(refs%2Fheads%2Fdevelop%7Crefs%2Fheads%2Ffeature%2F.%2B)%24) to trigger builds only for develop and feature-branches.

There are more [examples of use cases here](src/test/resources/org/jenkinsci/plugins/gwt/bdd).

It can trigger on any webhook, like:
* [Bitbucket Cloud](https://confluence.atlassian.com/bitbucket/manage-webhooks-735643732.html)
* [Bitbucket Server](https://confluence.atlassian.com/bitbucketserver/managing-webhooks-in-bitbucket-server-938025878.html)
* [GitHub](https://developer.github.com/webhooks/)
* [GitLab](https://docs.gitlab.com/ce/user/project/integrations/webhooks.html)
* [Gogs](https://gogs.io/docs/features/webhook) and [Gitea](https://docs.gitea.io/en-us/webhooks/)
* [Assembla](https://blog.assembla.com/AssemblaBlog/tabid/12618/bid/107614/Assembla-Bigplans-Integration-How-To.aspx)
* [Jira](https://developer.atlassian.com/server/jira/platform/webhooks/)
* And many many more!

The original use case was to build merge/pull requests. You may use the Git Plugin as described in [this blog post](http://bjurr.com/continuous-integration-with-gitlab-and-jenkins/) to do that. There is also an example of this on the [Violation Comments to GitLab Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Violation+Comments+to+GitLab+Plugin) page.

You may want to report back to the invoking system. [HTTP Request Plugin](https://wiki.jenkins-ci.org/display/JENKINS/HTTP+Request+Plugin) is a very convenient plugin for that. 

If a node is selected, then all leafs in that node will be contributed. If a leaf is selected, then only that leaf will be contributed.


## Trigger only specific job

When using the plugin in several jobs, you will have the same URL trigger all jobs. If you want to trigger only a certain job you can:

* Use the `token`-parameter have different tokens for different jobs. Using only the token means only jobs with that exact token will be visible for that request. This will increase performance and reduce responses of each invocation.
* Or, add some request parameter (or header, or post content) and use the **regexp filter** to trigger only if that parameter has a specific value.


### Token parameter

There is a special `token` parameter. When supplied, the invocation will only trigger jobs with that exact token.

![Parameter](https://github.com/jenkinsci/generic-webhook-trigger-plugin/blob/master/sandbox/configure-token.png)

The token can be supplied as a:

* Request parameter: `curl -vs http://localhost:8080/jenkins/generic-webhook-trigger/invoke?token=abc123 2>&1`
* Token header: `curl -vs -H "token: abc123" http://localhost:8080/jenkins/generic-webhook-trigger/invoke 2>&1`
* *Authorization* header of type *Bearer* : `curl -vs -H "Authorization: Bearer abc123" http://localhost:8080/jenkins/generic-webhook-trigger/invoke 2>&1`


## Troubleshooting

If you are fiddling with expressions, you may want to checkout:

* [This JSONPath site](https://jsonpath.curiousconcept.com/)
* [This XPath site](http://www.freeformatter.com/xpath-tester.html)
* [This regexp site](https://jex.im/regulex/)

It's probably easiest to do with curl. Given that you have configured a Jenkins job to trigger on Generic Webhook, here are some examples of how to start the jobs.

```bash
curl -vs http://localhost:8080/jenkins/generic-webhook-trigger/invoke 2>&1
```

This should start your job, if the job has no `token` configured. If your job has a `token` you can specify that like this.

```bash
curl -vs http://localhost:8080/jenkins/generic-webhook-trigger/invoke?token=TOKEN_HERE 2>&1
```

If you want to trigger with some post content, `curl` can dot that like this.

```bash
curl -v -H "Content-Type: application/json" -X POST -d '{ "app":{ "name":"GitHub API", "url":"http://developer.github.com/v3/oauth/" }}' http://localhost:8080/jenkins/generic-webhook-trigger/invoke?token=TOKEN_HERE
```

## Screenshots

![Generic trigger](https://github.com/jenkinsci/generic-webhook-trigger-plugin/blob/master/sandbox/generic-trigger.png)

If you need the resolved values in pre build steps, like git clone, you need to add a parameter with the same name as the variable.

![Parameter](https://github.com/jenkinsci/generic-webhook-trigger-plugin/blob/master/sandbox/parameter-git-repo.png)

## Job DSL Plugin

This plugin can be used with the Job DSL Plugin. There is also an example int he [Violation Comments to GitLab Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Violation+Comments+to+GitLab+Plugin) wiki page.

```groovy
job('Generic Job Example') {
 parameters {
  stringParam('VARIABLE_FROM_POST', '')
 }

 triggers {
  genericTrigger {
   genericVariables {
    genericVariable {
     key("VARIABLE_FROM_POST")
     value("\$.something")
     expressionType("JSONPath") //Optional, defaults to JSONPath
     regexpFilter("") //Optional, defaults to empty string
     defaultValue("") //Optional, defaults to empty string
    }
   }
   genericRequestVariables {
    genericRequestVariable {
     key("requestParameterName")
     regexpFilter("")
    }
   }
   genericHeaderVariables {
    genericHeaderVariable {
     key("requestHeaderName")
     regexpFilter("")
    }
   }
   token('abc123')
   printContributedVariables(true)
   printPostContent(true)
   regexpFilterText("\$VARIABLE_FROM_POST")
   regexpFilterExpression("aRegExp")
  }
 }

 steps {
  shell('''
echo $VARIABLE_FROM_POST
echo $requestParameterName
echo $requestHeaderName
  ''')
 }
}
```

## Pipeline Multibranch

This plugin can be used with the [Pipeline Multibranch Plugin](https://jenkins.io/doc/pipeline/steps/workflow-multibranch/#properties-set-job-properties). Here is an example:

With a scripted Jenkinsfile like this:

```groovy
node {
 properties([
  pipelineTriggers([
   [$class: 'GenericTrigger',
    genericVariables: [
     [key: 'ref', value: '$.ref'],
     [
      key: 'before',
      value: '$.before',
      expressionType: 'JSONPath', //Optional, defaults to JSONPath
      regexpFilter: '', //Optional, defaults to empty string
      defaultValue: '' //Optional, defaults to empty string
     ]
    ],
    genericRequestVariables: [
     [key: 'requestWithNumber', regexpFilter: '[^0-9]'],
     [key: 'requestWithString', regexpFilter: '']
    ],
    genericHeaderVariables: [
     [key: 'headerWithNumber', regexpFilter: '[^0-9]'],
     [key: 'headerWithString', regexpFilter: '']
    ],
     
    causeString: 'Triggered on $ref',
    
    token: 'abc123',
    
    printContributedVariables: true,
    printPostContent: true,
    
    regexpFilterText: '$ref',
    regexpFilterExpression: 'refs/heads/' + BRANCH_NAME
   ]
  ])
 ])

 stage("build") {
  sh '''
  echo Variables from shell:
  echo ref $ref
  echo before $before
  echo requestWithNumber $requestWithNumber
  echo requestWithString $requestWithString
  echo headerWithNumber $headerWithNumber
  echo headerWithString $headerWithString
  '''
 }
}
```

With a declarative Jenkinsfile like this:

```groovy
pipeline {
  agent any
  triggers {
    GenericTrigger(
     genericVariables: [
      [key: 'ref', value: '$.ref']
     ],
     
     causeString: 'Triggered on $ref',
     
     token: 'abc123',
     
     printContributedVariables: true,
     printPostContent: true,
    
     regexpFilterText: '$ref',
     regexpFilterExpression: 'refs/heads/' + BRANCH_NAME
    )
  }
  stages {
    stage('Some step') {
      steps {
        sh "echo $ref"
      }
    }
  }
}
```

It can be triggered with something like:

```bash
curl -X POST -H "Content-Type: application/json" -H "headerWithNumber: nbr123" -H "headerWithString: a b c" -d '{ "before": "1848f12", "after": "5cab1", "ref": "refs/heads/develop" }' -vs http://admin:admin@localhost:8080/jenkins/generic-webhook-trigger/invoke?requestWithNumber=nbr%20123\&requestWithString=a%20string
```

And the job will have this in the log:

```
Contributing variables:

    headerWithString_0 = a b c
    requestWithNumber_0 = 123
    ref = refs/heads/develop
    headerWithNumber = 123
    requestWithNumber = 123
    before = 1848f12
    requestWithString_0 = a string
    headerWithNumber_0 = 123
    headerWithString = a b c
    requestWithString = a string
```


## Plugin development
More details on Jenkins plugin development is available [here](https://wiki.jenkins-ci.org/display/JENKINS/Plugin+tutorial).

A release is created like this. You need to clone from jenkinsci-repo, with https and have username/password in settings.xml.
```
mvn release:prepare release:perform
```
