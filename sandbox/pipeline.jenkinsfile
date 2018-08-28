pipeline {
  agent any
  triggers {
    GenericTrigger(
     genericVariables: [
      [key: 'ref', value: '$.ref']
     ],
     
     causeString: 'Triggered on $ref',
     
     printContributedVariables: true,
     printPostContent: true,
    
     regexpFilterText: '$ref',
     regexpFilterExpression: 'refs/heads/master'
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
