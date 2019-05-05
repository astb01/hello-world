pipeline {
  agent any

  options {
    buildDiscarder(logRotator(daysToKeepStr: '1', numToKeepStr: '3'))
  }

  tools {
    maven 'mvn-3.6.1'
  }

  environment {
    DOCKER_REPO_USER = 'astb01'
    DOCKER_REPO_NAME = 'helloworld'
    CI = 'true'
    VERSION_NUMBER = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
    CONTAINER_TESTS_DIR = "${env.WORKSPACE}/src/test/container"
  }

  stages {
    stage('Maven Set up') {
      steps {
        sh 'mvn clean install'
      }
    }

    stage('Lint Dockerfile') {
      steps {
        sh "docker run --rm -i hadolint/hadolint hadolint --ignore DL4006 - < Dockerfile"
      }
    }

    stage('Docker Build') {
      steps {
        sh "docker build --tag ${env.DOCKER_REPO_USER}:${env.DOCKER_REPO_NAME}:latest --tag ${env.DOCKER_REPO_USER}:${env.DOCKER_REPO_NAME}:${env.VERSION_NUMBER} ."
      }
    }

    stage('Test Docker Image') {
      steps {
        sh "pwd"
        sh "container-structure-test test --image ${env.DOCKER_REPO_USER}:${env.DOCKER_REPO_NAME}:latest --config ${env.CONTAINER_TESTS_DIR}/config.json"
      }
    }
  }

  post {
    always {
        sh "docker system prune --force"
    }
    success {
        when {
          branch 'master'
        }
        withCredentials([usernamePassword(credentialsId: 'docker-hub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
          sh "echo ${env.dockerHubPassword} | docker login --u --password-stdin"
          sh "docker push ${env.DOCKER_REPO_USER}:${env.DOCKER_REPO_NAME}:latest"
          sh "docker push ${env.DOCKER_REPO_USER}:${env.DOCKER_REPO_NAME}:${env.VERSION_NUMBER}"
        }
    }
  }
}