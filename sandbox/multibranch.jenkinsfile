/*
 * curl -X POST -H "Content-Type: application/json" -H "headerWithNumber: nbr123" -H "headerWithString: a b c" -d '{ "before": "1848f12", "after": "5cab1", "ref": "refs/heads/develop" }' -vs http://admin:admin@localhost:8080/jenkins/generic-webhook-trigger/invoke?requestWithNumber=nbr%20123\&requestWithString=a%20string
 */
node {
 properties([
  pipelineTriggers([
   [$class: 'GenericTrigger',
    genericVariables: [
     [key: 'reference', value: '$.ref'],
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
     [key: 'headerWithString', regexpFilter: ''],
     [key: 'X-GitHub-Event', regexpFilter: '']
    ],
    printContributedVariables: true,
    printPostContent: true,
    regexpFilterText: '',
    regexpFilterExpression: ''
   ]
  ])
 ])

 stage("build") {
  sh '''
  echo Variables from shell:
  echo reference $reference
  echo before $before
  echo requestWithNumber $requestWithNumber
  echo requestWithString $requestWithString
  echo headerWithNumber $headerWithNumber
  echo headerWithString $headerWithString
  echo X_GitHub_Event $X_GitHub_Event
  '''
 }
}
