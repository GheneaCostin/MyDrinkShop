pipeline {
    agent {
        label 'eticheta_agentului_cu_java_17' 
    }
    tools {
        maven 'Maven-3.9.0' 
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/GheneaCostin/MyDrinkShop.git'
            }
        }
        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }
    }
}
