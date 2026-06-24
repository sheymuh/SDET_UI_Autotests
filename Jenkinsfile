pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage('Start Grid') {
            steps {
                echo 'Starting Selenium Grid...'
                bat 'src\\test\\resources\\scripts\\start-grid-standalone.bat'
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running tests...'
                bat 'mvn clean test -DsuiteXmlFile="src\\test\\resources\\configurations\\testng-grid-test.xml"'
            }
        }
    }

    post {
        always {
            echo 'Stopping Selenium Grid...'
            bat 'src\\test\\resources\\scripts\\stop-grid.bat'
        }
    }
}