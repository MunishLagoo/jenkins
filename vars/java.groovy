def call(Map params = [:]) {
    def args = [
        COMPONENT: '',
        LABEL : 'WORKSTATION'
    ] 
    args<< params
    pipeline {
        agent {label params.LABEL}
        environment {
            NEXUS = credentials("NEXUS")
        }
        stages {
            stage('Labeling Build') {
                steps {
                    script {
                      str = GIT_BRANCH.split('/').last()
                       addShortText background: 'yellow', color: 'black', borderColor:'yellow', text:"COMPONENT=${params.COMPONENT}"
            //           addShortText background: 'yellow', color: 'black', borderColor:'yellow', text:"APP_VERSION=${params.APP_VERSION}"
                       addShortText background: 'yellow', color: 'black', borderColor:'yellow', text:"BRANCH=${str}"
                    }
                }
            }
            stage('Maven Compile') {
                steps {
                    sh """
                    echo "+++ Before"
                    ls -l
                    mvn package
                    echo "+++ after"
                    ls -l
                    """
                }
            }
          stage('Code Quality') {
                steps {
                    sh 'echo code quality'
                //  sh """
                //     sonar-scanner -Dsonar.projectKey=${params.COMPONENT} -Dsonar.java.binaries=target/.  -Dsonar.host.url=http://172.31.88.250:9000  -Dsonar.login=00caade48d44c443c8535371e6ffdd640497f71e
                //     """
                }
            }

            //   stage('Check Code Quality gate') {
            //     steps {
            //      sh """
            //         sleep 5
            //         sonar-quality-gate.sh admin admin123 172.31.88.250 ${params.COMPONENT}
            //         """
            //     }
            // }

            stage('Test Cases') {
                steps {
                    sh 'echo Test-Cases'
                }
            }
             stage('Upload Artifacts') {
                 when {
                     expression {
                         sh ([returnStdout: true, script:'echo ${GIT_BRANCH} | grep tags || true'])
                         }
                 }
                steps {                    
                    sh """
                       GIT_TAG=`echo ${GIT_BRANCH} | awk -F / '{print \$NF}'`
                       echo \${GIT_TAG} > version
                       cp target/${params.COMPONENT}-1.0.jar ${params.COMPONENT}.jar
                       zip -r ${params.COMPONENT}-\${GIT_TAG}.zip ${params.COMPONENT}.jar version
                       echo "uploading artifacts to Nexus"
                       curl -f -v -u ${NEXUS} --upload-file ${params.COMPONENT}-\${GIT_TAG}.zip http://172.31.10.172:8081/repository/${params.COMPONENT}/${params.COMPONENT}-\${GIT_TAG}.zip
                      """
                }
            }

            stage ('App Deployment-Dev') {
              steps {
                script{
                    GIT_TAG = GIT_BRANCH.split('/').last()
                 }
                build job: 'Mutable/App-Deploy', parameters: [
                  string(name: 'ENV', value: 'dev'), 
                  string(name: 'APP_VERSION', value: "${GIT_TAG}"), 
                  string(name: 'COMPONENT', value: "${params.COMPONENT}")
                 ]
                }
            }
        }
      post {
          always {
              cleanWs()
          }
      }  
    }
}