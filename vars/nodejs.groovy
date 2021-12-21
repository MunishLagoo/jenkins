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
            stage('Download NodeJS Dependencies') {
                steps {
                    sh """
                    echo "+++ Before"
                    ls -l
                    npm install
                    echo "+++ after"
                    ls -l
                    """
                }
            }
            stage('Code Quality') {
                steps {
                 sh """
                    sonar-scanner -Dsonar.projectKey=${params.COMPONENT} -Dsonar.sources=.  -Dsonar.host.url=http://172.31.88.250:9000  -Dsonar.login=00caade48d44c443c8535371e6ffdd640497f71e
                    """
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