node {
  def project = 'test-service-a'
  def appName = 'test-service-a'

  checkout scm

  stage 'Compile'
  sh("mvn compile")

  stage 'Test'
  sh("mvn test")

  stage 'Build+Push image'
  sh("cd test-service-a-impl && mvn dockerfile:build dockerfile:push -Ddockerfile.repository=localhost:5000/${project}")

  stage 'Deploy'
  sh("kubectl --namespace=develop apply -f k8s/dev")
  sh("kubectl --namespace=develop apply -f k8s/services")
}
