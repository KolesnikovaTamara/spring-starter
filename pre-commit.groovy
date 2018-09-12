node{
    env.JAVA_HOME = "/usr/java/jdk1.8.0_151"
    def mvnHome = tool 'apache-maven-3.5.2'
    stage("Checkout"){
        checkout scm
    }
    stage("Build") {
        sh "${mvnHome}/bin/mvn clean package -DskipTests"
    }
    stage("SonarQube"){
        withSonarQubeEnv('sonarqube-server') {
            withCredentials([[$class: 'StringBinding', credentialsId: 'githun-token', variable: 'GITHUB_TOKEN']]) {
                sh """${mvnHome}/bin/mvn sonar:sonar \
                     -Dsonar.analysis.mode=preview \
                     -Dsonar.github.pullRequest=1 \
                     -Dsonar.github.repository=KolesnikovaTamara/spring-starter \
                     -Dsonar.github.oauth=$GITHUB_TOKEN"""
            }
        }
    }
}
