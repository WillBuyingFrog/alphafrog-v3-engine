pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build --build-arg http_proxy=http://127.0.0.1:7890 --build-arg https_proxy=http://127.0.0.1:7890 -t alphafrog-v3:latest .'
                }
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker stop alphafrog-v3 || true'
                sh 'docker rm alphafrog-v3 || true'
                sh '''
                    docker run -d \
                    -v /root/alphafrog/application.yml:/app/config/application.yml \
                    -p 8090:8090 \
                    alphafrog-v3
                    --spring.config.location=file:/app/config/application.yml
                   '''
            }
        }
    }
}