pipeline {
    agent any

    tools {
        maven 'maven'
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
                echo "Building Spring Boot project..."
                sh 'cd rise_together && mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image..."
                    sh 'cd rise_together && docker build -t rise_together .'
                }
            }
        }

        stage('Push Image to Docker Hub') {
            steps {
                script {
                    echo "Pushing Docker image to DockerHub..."

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
                    echo "Deploying container..."

                    // stop old container if exists
                    sh "docker rm -f rise_together || true"

                    // run new one (change 9000 if needed)
                    sh "docker run -d --name rise_together -p 9000:8080 umamalagund9620/rise_together:latest"
                }
            }
        }
    }
}
