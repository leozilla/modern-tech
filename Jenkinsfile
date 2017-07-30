node {
  def project = 'test-service-a'
  def appName = 'test-service-a'
  def feSvcName = "${appName}-frontend"
  // def imageTag = "gcr.io/${project}/${appName}:${env.BRANCH_NAME}.${env.BUILD_NUMBER}"

  checkout scm

  stage 'Build image'
  sh("cd test-service-a-impl")
  sh("mvn install")
}
