# Generic Webhook Trigger Plugin

[![Build Status](https://ci.jenkins.io/job/Plugins/job/generic-webhook-trigger-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/generic-webhook-trigger-plugin)

This is a Jenkins plugin that can:

1.  Receive any HTTP request, `JENKINS_URL/generic-webhook-trigger/invoke`
2.  Extract values

- From `POST` content with [JSONPath](https://github.com/json-path/JsonPath) or [XPath](https://www.w3schools.com/xml/xpath_syntax.asp)
- From the `query` parameters
- From the `headers`

3.  Trigger a build with those values contribute as variables

There is an optional feature to trigger jobs only if a supplied regular expression matches the extracted variables. Here is an example, let's say the post content looks like this:

```javascript
{
  "before": "1848f1236ae15769e6b31e9c4477d8150b018453",
  "after": "5cab18338eaa83240ab86c7b775a9b27b51ef11d",
  "ref": "refs/heads/develop"
}
```

Then you can have a variable, resolved from post content, named `ref` of type `JSONPath` and with expression like `$.ref` . The optional filter text can be set to `$ref` and the filter regexp set to [^(refs/heads/develop|refs/heads/feature/.+)\$](<https://jex.im/regulex/#!embed=false&flags=&re=%5E(refs%2Fheads%2Fdevelop%7Crefs%2Fheads%2Ffeature%2F.%2B)%24>) to trigger builds only for develop and feature-branches.

There are more [examples of use cases here](src/test/resources/org/jenkinsci/plugins/gwt/bdd).

Video showing an example usage:

[![Generic Webhook Trigger Usage Example](https://img.youtube.com/vi/8mrJNkofxq4/0.jpg)](https://www.youtube.com/watch?v=8mrJNkofxq4)

It can trigger on any webhook, like:

- [Bitbucket Cloud](https://confluence.atlassian.com/bitbucket/manage-webhooks-735643732.html)
- [Bitbucket Server](https://confluence.atlassian.com/bitbucketserver/managing-webhooks-in-bitbucket-server-938025878.html)
- [GitHub](https://developer.github.com/webhooks/)
- [GitLab](https://docs.gitlab.com/ce/user/project/integrations/webhooks.html)
- [Gogs](https://gogs.io/docs/features/webhook) and [Gitea](https://docs.gitea.io/en-us/webhooks/)
- [Assembla](https://blog.assembla.com/AssemblaBlog/tabid/12618/bid/107614/Assembla-Bigplans-Integration-How-To.aspx)
- [Jira](https://developer.atlassian.com/server/jira/platform/webhooks/)
- And many many more!

The original use case was to build merge/pull requests. You may use the Git Plugin as described in [this blog post](http://bjurr.com/continuous-integration-with-gitlab-and-jenkins/) to do that. There is also an example of this on the [Violation Comments to GitLab Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Violation+Comments+to+GitLab+Plugin) page.

You may want to report back to the invoking system. [HTTP Request Plugin](https://wiki.jenkins-ci.org/display/JENKINS/HTTP+Request+Plugin) is a very convenient plugin for that.

If a node is selected, then all leafs in that node will be contributed. If a leaf is selected, then only that leaf will be contributed.

## Trigger only specific job

When using the plugin in several jobs, you will have the same URL trigger all jobs. If you want to trigger only a certain job you can:

- Use the `token`-parameter have different tokens for different jobs. Using only the token means only jobs with that exact token will be visible for that request. This will increase performance and reduce responses of each invocation.
- Or, add some request parameter (or header, or post content) and use the **regexp filter** to trigger only if that parameter has a specific value.

### Token parameter

There is a special `token` parameter. When supplied, the invocation will only trigger jobs with that exact token. The token also allows invocations without any other authentication credentials.

![Parameter](/sandbox/configure-token.png)

The token can be supplied as a:

- Request parameter:
  
  `curl -vs http://localhost:8080/jenkins/generic-webhook-trigger/invoke?token=abc123 2>&1`
- Token header:
  
  `curl -vs -H "token: abc123" http://localhost:8080/jenkins/generic-webhook-trigger/invoke 2>&1`
  - It will also detect `X-Gitlab-Token`.
- _Authorization_ header of type _Bearer_ :
  
  `curl -vs -H "Authorization: Bearer abc123" http://localhost:8080/jenkins/generic-webhook-trigger/invoke 2>&1`

## Trigger exactly one build

Jenkins will batch builds of a job if those builds have same parameters. If this plugin gets invoked by many webhooks at the same time it may trigger only one build and it will have many *Generic Cause* as causes. This has been reported in many issues [#64](/../../issues/64) [#116](/../../issues/116) [#126](/../../issues/126) [#162](/../../issues/162) [#171](/../../issues/171).

You can solve this by making the one job parameterized. Resolve one of the parameters with something unique from the webhook. This will make each trigger unique and Jenkins will not batch the builds into one build.

The section on *Default values* explains the parameters.


## Whitelist hosts

Whitelist can be configured in Jenkins global configuration page. The whitelist will block any request to the plugin that is not configured in this list. The host can be **empty** to allow any, **static IP**, **CIDR** or **ranges**:

- _1.2.3.4_
- _2.2.3.0/24_
- _3.2.1.1-3.2.1.10_
- _2001:0db8:85a3:0000:0000:8a2e:0370:7334_
- _2002:0db8:85a3:0000:0000:8a2e:0370:7334/127_
- _2001:0db8:85a3:0000:0000:8a2e:0370:7334-2001:0db8:85a3:0000:0000:8a2e:0370:7335_

The hosts can optionally also be verified with [HMAC](https://en.wikipedia.org/wiki/HMAC).

![Whitelist](/sandbox/whitelist.png)

## Troubleshooting

If you want to fiddle with the plugin, you may use this repo: https://github.com/tomasbjerre/jenkins-configuration-as-code-sandbox

If you are fiddling with expressions, you may want to checkout:

- [This JSONPath site](http://jsonpath.herokuapp.com/)
- [This XPath site](http://www.freeformatter.com/xpath-tester.html)
- [This regexp site](https://jex.im/regulex/) Also syntax [here](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html).

It's probably easiest to do with `curl`. Given that you have configured a Jenkins job to trigger on Generic Webhook, here are some examples of how to start the jobs.

```bash
curl -vs http://localhost:8080/jenkins/generic-webhook-trigger/invoke 2>&1
```

This should start your job, if the job has no `token` configured and no security enabled. If you have security enabled you may need to authenticate:

```bash
curl -vs http://theusername:thepasssword@localhost:8080/jenkins/generic-webhook-trigger/invoke 2>&1
```

If your job has a `token` you don't need to supply other credentials. You can specify the `token` like this:

```bash
curl -vs http://localhost:8080/jenkins/generic-webhook-trigger/invoke?token=TOKEN_HERE 2>&1
```

If you want to trigger with `token` and some post content, `curl` can dot that like this.

```bash
curl -v -H "Content-Type: application/json" -X POST -d '{ "app":{ "name":"some value" }}' http://localhost:8080/jenkins/generic-webhook-trigger/invoke?token=TOKEN_HERE
```

## Screenshots

![Generic trigger](/sandbox/generic-trigger.png)

### Default values

The plugin can be configured with default values. Like below:

![Default Value](/sandbox/default-value.png)

But if you execute the job manually (or replay a pipeline), this default value will not be used. Because the plugin will not be invoked at all. You can solve this by checking the "This job is parameterized" and add a parameter with the same name as the one you configured in the plugin. Like this:

![Default Value](/sandbox/default-value-parameter.png)

Now this default value will be used both when you trigger the job manually, replaying pipeline, and when you trigger it with the plugin!

### Pre build step

If you need the resolved values in pre build steps, like git clone, you need to add a parameter with the same name as the variable.

![Parameter](/sandbox/parameter-git-repo.png)

## Job DSL Plugin

This plugin can be used with the Job DSL Plugin. There is also an example int he [Violation Comments to GitLab Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Violation+Comments+to+GitLab+Plugin) wiki page.

Job DSL supports injecting credenials when processing the DSL. You can use that if you want the `token` to be set from credentials.

```groovy
pipelineJob('Generic Job Example') {
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
   tokenCredentialId('')
   printContributedVariables(true)
   printPostContent(true)
   silentResponse(false)
   regexpFilterText("\$VARIABLE_FROM_POST")
   regexpFilterExpression("aRegExp")
  }
 }

 definition {
  cps {
   // Or just refer to a Jenkinsfile containing the pipeline
   script('''
    node {
     stage('Some Stage') {
      println "VARIABLE_FROM_POST: " + VARIABLE_FROM_POST
     }
    }
   ''')
   sandbox()
  }
 }
}
```

## Pipeline

When configuring from pipeline (not multibranch pipeline), that pipeline needs to run once, to apply the plugin trigger config, and after that this plugin will be able to trigger the job. This is how Jenkins works, not something implemented in this plugin.

This means that if you create a pipeline like this:

![Parameter](/sandbox/pipeline-pre-run.png)

You need to run it once to have the properties applied. You can verify that the properties has been applied by opening the configuration view (or view configuration if using multibranch pipeline) of the job. You will see that the "Generic Webhook Trigger" is checked and will now have values from your pipeline. Like this:

![Parameter](/sandbox/pipeline-post-run.png)

You can avoid having to run twice, by using Job DSL and have Job DSL create pipeline jobs with the plugin configured in that DSL.

This plugin can be used in the jobs created by the [Pipeline Multibranch Plugin](https://jenkins.io/doc/pipeline/steps/workflow-multibranch/#properties-set-job-properties). If you are looking for a way to trigger a scan in the [Pipeline Multibranch Plugin](https://jenkins.io/doc/pipeline/steps/workflow-multibranch/) you can use the [Multibranch Scan Webhook Trigger Plugin](https://github.com/jenkinsci/multibranch-scan-webhook-trigger-plugin).

You can use the credentials plugin to provide the `token` from credentials.

```groovy
withCredentials([string(credentialsId: 'mycredentialsid', variable: 'credentialsVariable')]) {
 properties([
  pipelineTriggers([
   [$class: 'GenericTrigger',
    ...
    token: credentialsVariable,
    ...
   ]
  ])
 ])
}
```

Perhaps you want a different `token` for each job.

```groovy
 properties([
  pipelineTriggers([
   [$class: 'GenericTrigger',
    ...
    token: env.JOB_NAME,
    ...
   ]
  ])
 ])
```

Or have a credentials string prefixed with the job name.

```groovy
withCredentials([string(credentialsId: 'mycredentialsid', variable: 'credentialsVariable')]) {
 properties([
  pipelineTriggers([
   [$class: 'GenericTrigger',
    ...
    token: env.JOB_NAME + credentialsVariable,
    ...
   ]
  ])
 ])
}
```

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
    tokenCredentialId: '',

    printContributedVariables: true,
    printPostContent: true,

    silentResponse: false,

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
     tokenCredentialId: '',

     printContributedVariables: true,
     printPostContent: true,

     silentResponse: false,

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
