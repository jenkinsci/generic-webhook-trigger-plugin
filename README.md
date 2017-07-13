# Generic Webhook Trigger Plugin

[![Build Status](https://ci.jenkins.io/job/Plugins/job/generic-webhook-trigger-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/generic-webhook-trigger-plugin)

This is a Jenkins plugin that can:

 1. Receive any HTTP request, JENKINS_URL/generic-webhook-trigger/invoke
 2. Extract values

  * From POST content with [JSONPath](https://github.com/json-path/JsonPath) or [XPath](https://www.w3schools.com/xml/xpath_syntax.asp)
  * From the query parameters
  * From the headers

 3. Contribute those values as variables to the build

There is an optional feature to trigger jobs only if a supplied regular expression matches the extracted variables.

This means it can trigger on any webhook, like:
* [Bitbucket Cloud](https://confluence.atlassian.com/bitbucket/manage-webhooks-735643732.html)
* [Bitbucket Server](https://marketplace.atlassian.com/plugins/com.nerdwin15.stash-stash-webhook-jenkins/server/overview)
* [GitHub](https://developer.github.com/webhooks/)
* [GitLab](https://docs.gitlab.com/ce/user/project/integrations/webhooks.html)
* [Gogs](https://gogs.io/docs/features/webhook)
* [Assembla](https://blog.assembla.com/AssemblaBlog/tabid/12618/bid/107614/Assembla-Bigplans-Integration-How-To.aspx)
* And many many more!

The original use case was to build merge/pull requests. You may use the Git Plugin as described in [this blog post](http://bjurr.com/continuous-integration-with-gitlab-and-jenkins/) to do that. There is also an example of this on the [Violation Comments to GitLab Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Violation+Comments+to+GitLab+Plugin) page.

You may want to report back to the invoking system. [HTTP Request Plugin](https://wiki.jenkins-ci.org/display/JENKINS/HTTP+Request+Plugin) is a very convenient plugin for that. 

If a node is selected, then all leafs in that node will be contributed. If a leaf is selected, then only that leaf will be contributed.

There are websites to help fiddle with the expressions. You may want to checkout [this website](https://jsonpath.curiousconcept.com/) to fiddle with JSONPath. And [this website](http://www.freeformatter.com/xpath-tester.html) to fiddle with XPath.

Available in Jenkins [here](https://wiki.jenkins-ci.org/display/JENKINS/Generic+Webhook+Trigger+Plugin).

# Troubleshooting

It's probably easiest to do with curl. Given that you have configured a Jenkins job to trigger on Generic Webhook, here are some examples of how to start the jobs.

```
curl -vs http://localhost:8080/generic-webhook-trigger/invoke 2>&1
```

This may start your job, if you have enabled "**Allow anonymous read access**" in global security config. If it does not, check the Jenkins log. It may say something like this.

```
INFO: Did not find any jobs to trigger! The user invoking /generic-webhook-trigger/invoke must have read permission to any jobs that should be triggered.
```

And to authenticate in the request you may try this.

```
curl -vs http://username:password@localhost:8080/generic-webhook-trigger/invoke 2>&1
```

# Screenshots

![Generic trigger](https://github.com/jenkinsci/generic-webhook-trigger-plugin/blob/master/sandbox/generic-trigger.png)

If you need the resolved values in pre build steps, like git clone, you need to add a parameter with the same name as the variable.

![Parameter](https://github.com/jenkinsci/generic-webhook-trigger-plugin/blob/master/sandbox/parameter-git-repo.png)

## Job DSL Plugin

This plugin can be used with the Job DSL Plugin.

```
job('Generic Job Example') {
 parameters {
  stringParam('VARIABLE_FROM_POST', '')
  stringParam('VARIABLE_FROM_REQUEST', '')
 }

 triggers {
  genericTrigger {
   genericVariables {
    genericVariable {
     key("VARIABLE_FROM_POST")
     value("\$.something")
     expressionType("JSONPath")
     regexpFilter("")
    }
   }
   genericRequestVariables {
    genericRequestVariable {
     key("VARIABLE_FROM_REQUEST")
     regexpFilter("")
    }
   }
   genericHeaderVariables {
    genericHeaderVariable {
     key("VARIABLE_FROM_HEADER")
     regexpFilter("")
    }
   }
   regexpFilterText("\$VARIABLE_FROM_POST")
   regexpFilterExpression("aRegExp")
  }
 }

 steps {
  shell('''
echo $VARIABLE_FROM_POST
echo $VARIABLE_FROM_REQUEST
echo $VARIABLE_FROM_HEADER_0
  ''')
 }
}
```

## Pipeline

This plugin can be used with the [Pipeline Multibranch Plugin](https://jenkins.io/doc/pipeline/steps/workflow-multibranch/#properties-set-job-properties):

```
node {
 properties([
  pipelineTriggers([
   [$class: 'GenericTrigger',
    genericVariables: [
     [expressionType: 'JSONPath', key: 'variable1', value: 'expression1'],
     [expressionType: 'JSONPath', key: 'variable2', value: 'expression2']
    ],
    regexpFilterText: '',
    regexpFilterExpression: ''
   ]
  ])
 ])

 stage("build") {
  sh '''
  echo Build
  '''
 }
}
```

# Plugin development
More details on Jenkins plugin development is available [here](https://wiki.jenkins-ci.org/display/JENKINS/Plugin+tutorial).

A release is created like this. You need to clone from jenkinsci-repo, with https and have username/password in settings.xml.
```
mvn release:prepare release:perform
```
