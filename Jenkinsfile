pipeline {
    agent any
    tools {
        maven 'Maven'
        allure 'Allure'
    }
    stages {
        stage('Run Tests') {
            steps {
                echo 'Running tests...'
                bat 'mvn clean test -DsuiteXmlFile="src\\test\\resources\\configurations\\testng-chrome-local.xml"'
            }
            post {
                always {
                    allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
                }
            }
        }
    }
}
