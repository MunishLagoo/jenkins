def call(Map params = [:]) {
    def args :LinkedHashMap = [
        COMPONENT: ''
        LABEL : 'WORKSTATION'
    ] 
    args<< params
    pipeline {
        agent {label params.LABEL}
        stages {
            stage('Compile') {
                steps {
                    sh "echo COMPONENT = ${params.COMPONENT}"
                    sh "echo EX_COMP = ${EX_COMP}"
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
                steps {
                    sh 'echo Test-Cases'
                }
            }
        }
    }
}