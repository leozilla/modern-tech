node {
  def project = 'test-service-a'
  def appName = 'test-service-a'
  def dockerRegistry = 'localhost:5000'
  def imageName = "${dockerRegistry}/${project}"

  checkout scm

  pom = readMavenPom file: 'pom.xml'
  def projectVersion = pom.version

  def imageTag = "${projectVersion}.${env.BUILD_NUMBER}"

  stage 'Clean Install'
  sh("mvn clean install")

  stage 'Build+Push image'
  sh("cd test-service-a-impl && mvn dockerfile:build dockerfile:push -Ddockerfile.repository=${imageName} -Ddockerfile.tag=${imageTag}")

  stage 'Deploy'
  sh("sed -i.bak 's#localhost:5000/test-service-a#${imageName}:${imageTag}#' ./k8s/dev/*.yaml")
  sh("kubectl --namespace=develop apply -f k8s/dev/")
  sh("kubectl --namespace=develop apply -f k8s/services/")
}
