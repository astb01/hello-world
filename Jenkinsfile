pipeline {
  agent any

  options {
    buildDiscarder(logRotator(daysToKeepStr: '1', numToKeepStr: '3'))
  }

  tools {
    maven 'mvn-3.6.1'
    jdk 'openjdk11'
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
        sh 'printenv'
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
        sh "docker build -t ${env.DOCKER_REPO_USER}/${env.DOCKER_REPO_NAME}:latest -t ${env.DOCKER_REPO_USER}/${env.DOCKER_REPO_NAME}:${env.VERSION_NUMBER} ."
      }
    }

    stage('Test Docker Image') {
      steps {
        sh "pwd"
        sh "container-structure-test test --image ${env.DOCKER_REPO_USER}/${env.DOCKER_REPO_NAME}:latest --config ${env.CONTAINER_TESTS_DIR}/config.json"
      }
    }

    stage('Push to Docker') {
        when {
            environment name: 'GIT_BRANCH', value: 'origin/master'
        }
        steps {
            withCredentials([usernamePassword(credentialsId: 'docker-hub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                  sh "echo ${env.dockerHubPassword} | docker login -u ${env.dockerHubUser} --password-stdin"
                  sh "docker push ${env.DOCKER_REPO_USER}/${env.DOCKER_REPO_NAME}:latest"
                  sh "docker push ${env.DOCKER_REPO_USER}/${env.DOCKER_REPO_NAME}:${env.VERSION_NUMBER}"
            }
        }
    }
  }

  post {
    always {
        sh "docker system prune --force"
    }
  }
}