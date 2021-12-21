def call(Map params = [:]) {
    def args = [
        COMPONENT: '',
        LABEL : 'WORKSTATION'
    ] 
    args<< params
    pipeline {
        agent {label params.LABEL}
        stages {
            // stage('Labeling Build') {
            //     steps {
            //         script {
            //           str = GIT_BRANCH.split('/').last()
            //           addshortText background: 'yellow', color: 'black', borderColor:'yellow', text:"COMPONENT=${params.COMPONENT}"
            //           //addshortText background: 'yellow', color: 'black', borderColor:'yellow', text:"APP_VERSION=${params.APP_VERSION}"
            //           addshortText background: 'yellow', color: 'black', borderColor:'yellow', text:"BRANCH=${str}"
            //         }
            //     }
            // }
            stage('Compile') {
                steps {
                    sh "echo COMPONENT = ${params.COMPONENT}"
                    //sh "echo EX_COMP = ${EX_COMP}"
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
                         sh ([returnstdout: true, script:'echo ${GIT_BRANCH}| grep tags || true'])
                         }
                 }
                steps {
                    sh 'echo Test-Cases'
                }
            }
        }
    }
}