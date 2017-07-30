node {
  def project = 'test-service-a'
  def appName = 'test-service-a'
  def feSvcName = "${appName}-frontend"
  // def imageTag = "gcr.io/${project}/${appName}:${env.BRANCH_NAME}.${env.BUILD_NUMBER}"

  checkout scm

  stage 'Compile'
  sh("mvn compile")

  stage 'Test'
  sh("mvn test")

  stage 'Build+Push image'
  sh("cd test-service-a-impl")
  sh("mvn dockerfile:build dockerfile:push")

  stage 'Deploy'
  sh("kubectl --namespace=develop apply -f k8s/dev")
  sh("kubectl --namespace=develop apply -f k8s/services")
}
