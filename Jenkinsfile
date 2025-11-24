pipeline {
    agent any

    tools {
        maven 'maven'   // Jenkins Maven installation name
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Pulling project from GitHub..."
                git branch: 'main',
                    url: 'https://github.com/UmaMalagund-arch/rise_together.git'
            }
        }

        stage('Build Maven Project') {
            steps {
                echo "Building Spring Boot application..."
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image..."
                    sh "docker build -t rise_together ."
                }
            }
        }

        stage('Push Image to Docker Hub') {
            steps {
                script {
                    echo 'Pushing Docker image to DockerHub...'

                    withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {
                        sh "docker login -u $DOCKER_USER -p $DOCKER_PASS"
                        sh "docker tag rise_together $DOCKER_USER/rise_together:latest"
                        sh "docker push $DOCKER_USER/rise_together:latest"
                    }
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                script {
                    echo "Deploying on your server..."

                    sh "docker rm -f rise_together || true"

                    sh "docker run -d --name rise_together -p 9000:8080 umamalagund9620/rise_together:latest"
                }
            }
        }
    }
}

