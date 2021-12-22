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
                       addShortText background: 'yellow', color: 'black', borderColor:'yellow', text:"APP_VERSION=${params.APP_VERSION}"
                       addShortText background: 'yellow', color: 'black', borderColor:'yellow', text:"BRANCH=${str}"
                    }
                }
            }
            
            stage('Code Quality') {
                steps {
                    sh 'echo Quality'
                }
            }
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
                       cd static
                       zip -r ${params.COMPONENT}-\${GIT_TAG}.zip *
                       cd ..
                       echo "uploading artifacts to Nexus"
                       curl -v -u ${NEXUS} --upload-file ${params.COMPONENT}-\${GIT_TAG}.zip http://172.31.10.172:8081/repository/${params.COMPONENT}/${params.COMPONENT}-\${GIT_TAG}.zip
                      """
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