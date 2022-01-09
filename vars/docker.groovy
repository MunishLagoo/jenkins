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
         stage('Code Quality') {
                steps {
                    sh 'echo code quality'
                //  sh """
                //     sonar-scanner -Dsonar.projectKey=${params.COMPONENT} -Dsonar.sources=.  -Dsonar.host.url=http://172.31.88.250:9000  -Dsonar.login=00caade48d44c443c8535371e6ffdd640497f71e
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
             stage('Docker Build') {
                 when {
                     expression {
                         sh ([returnStdout: true, script:'echo ${GIT_BRANCH} | grep tags || true'])
                         }
                 }
                steps {                    
                    sh """
                       GIT_TAG=`echo ${GIT_BRANCH} | awk -F / '{print \$NF}'`
                       echo \${GIT_TAG} > version
                       aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 608320470413.dkr.ecr.us-east-1.amazonaws.com
                       docker build -t 608320470413.dkr.ecr.us-east-1.amazonaws.com/${params.COMPONENT}:\${GIT_TAG} .
                       docker push 608320470413.dkr.ecr.us-east-1.amazonaws.com/${params.COMPONENT}:\${GIT_TAG}
                       docker tag 608320470413.dkr.ecr.us-east-1.amazonaws.com/${params.COMPONENT}:\${GIT_TAG} 608320470413.dkr.ecr.us-east-1.amazonaws.com/${params.COMPONENT}:latest
                       docker push 608320470413.dkr.ecr.us-east-1.amazonaws.com/${params.COMPONENT}:latest
                       docker rmi 608320470413.dkr.ecr.us-east-1.amazonaws.com/${params.COMPONENT}:\${GIT_TAG}
                       docker rmi 608320470413.dkr.ecr.us-east-1.amazonaws.com/${params.COMPONENT}:latest
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