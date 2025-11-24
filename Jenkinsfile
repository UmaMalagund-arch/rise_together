
pipeline {
    agent any

    environment {
        REGISTRY = "umamalagund9620"
        IMAGE_NAME = "rise_together"
        TAG = "latest"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn -B clean test'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'mvn -B clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh """
                        docker build -t ${REGISTRY}/${IMAGE_NAME}:${TAG} .
                    """
                }
            }
        }

        stage('Login to Docker Registry') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'USER',
                    passwordVariable: 'PASS'
                )]) {
                    sh "echo \$PASS | docker login -u \$USER --password-stdin"
                }
            }
        }

        stage('Push Image') {
            steps {
                sh "docker push ${REGISTRY}/${IMAGE_NAME}:${TAG}"
            }
        }

        stage('Deploy') {
            when {
                expression { return false } // enable when ready
            }
            steps {
                echo "Add your deployment commands here"
            }
        }
    }

    post {
        always {
            echo "Pipeline completed"
        }
    }
}
