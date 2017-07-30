node {
  def project = 'test-service-a'
  def appName = 'test-service-a'
  def dockerRegistry = 'localhost:5000'
  def imageTag = ${dockerRegistry}/${project}:${env.BUILD_NUMBER}

  checkout scm

  stage 'Compile'
  sh("mvn compile")

  stage 'Test'
  sh("mvn test")

  stage 'Build+Push image'
  sh("cd test-service-a-impl && mvn dockerfile:build dockerfile:push -Ddockerfile.repository=${imageTag}")

  stage 'Deploy'
  sh("kubectl --namespace=develop apply -f k8s/dev")
  sh("kubectl --namespace=develop apply -f k8s/services")
}
