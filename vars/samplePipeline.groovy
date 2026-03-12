def call(Map configMap){
    pipeline {
    agent {
        label 'Agent-1'
    }

    environment {
        course = 'jenkins'
        greeting =  configMap.get('greeting')
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    parameters {
        string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
        text(name: 'BIOGRAPHY', defaultValue: '', description: 'Enter some information about the person')
        booleanParam(name: 'TOGGLE', defaultValue: true, description: 'Toggle this value')
    }

    stages {

        stage('Build') {
            steps {
                script {
                    sh """
                    echo "Hello Build"
                    sleep 10
                    env
                    """
                    echo "Hello ${params.PERSON}"
                   
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    echo 'Building'
                }
            }
        }

        stage('Deploy') {
            input {
                message "Should we continue?"
                ok "Yes, we should."
                submitter "alice,bob"
                parameters {
                    string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
                }
            }
            steps {
                script {
                    echo "Hello, ${params.PERSON}, nice to meet you."
                    echo 'Building'
                }
            }
        }
    }

    post {
        always {
            echo 'I will always say Hello again!'
            deleteDir()
        }
        success {
            echo 'Hello Success'
        }
        failure {
            echo 'Hello Failure'
        }
    }
}

}
