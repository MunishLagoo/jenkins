def call(Map params = [:]) {
    def args = [
        COMPONENT: '',
        LABEL : 'WORKSTATION'
    ] 
    args<< params
    pipeline {
        agent {label params.LABEL}
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
                    sh 'echo Test-Cases'
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