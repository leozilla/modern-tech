node {
  def project = 'test-service-a'
  def appName = 'test-service-a'
  def feSvcName = "${appName}-frontend"
  // def imageTag = "gcr.io/${project}/${appName}:${env.BRANCH_NAME}.${env.BUILD_NUMBER}"

  checkout scm

  sh("cd test-service-a-impl")

  stage 'Compile'
  sh("mvn compile")

  stage 'Test'
  sh("mvn test")

  stage 'Build image'
  sh("mvn dockerfile:build")
}
