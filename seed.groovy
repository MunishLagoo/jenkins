//pipelineJob('CI-Pipelines/frontend') {
//  configure { flowdefinition ->
//    flowdefinition << delegate.'definition'(class:'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition',plugin:'workflow-cps') {
//      'scm'(class:'hudson.plugins.git.GitSCM',plugin:'git') {
//        'userRemoteConfigs' {
//          'hudson.plugins.git.UserRemoteConfig' {
//            'url'('https://DevOps-Batches@dev.azure.com/DevOps-Batches/DevOps60/_git/frontend')
//          }
//        }
//        'branches' {
//          'hudson.plugins.git.BranchSpec' {
//            'name'('*/main')
//          }
//        }
//      }
//      'scriptPath'('Jenkinsfile')
//      'lightweight'(true)
//    }
//  }
//}

folder('CI-Pipelines') {
  displayName('CI-Pipelines')
  description('CI-Pipelines')
}

def component = ["catalogue","user","cart","shipping","payment","frontend"]

def count = (component.size() - 1)

for (int i in 0..count) {
    def j = component[i]

pipelineJob("CI-Pipelines/${j}") {
 configure { flowdefinition ->
   flowdefinition << delegate.'definition'(class:'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition',plugin:'workflow-cps') {
     'scm'(class:'hudson.plugins.git.GitSCM',plugin:'git') {
       'userRemoteConfigs' {
         'hudson.plugins.git.UserRemoteConfig' {
             'url'("https://github.com/MunishLagoo/${j}.git")
        //    'url'('https://DevOps-Batches@dev.azure.com/DevOps-Batches/DevOps60/_git/frontend')
         'refspec'('\'+refs/tags/*\':\'refs/remotes/origin/tags/*\'')
         }
       }
       'branches' {
           'hudson.plugins.git.BranchSpec' {
           'name'('*/tags/*')
           }
         'hudson.plugins.git.BranchSpec' {
           'name'('*/main')
         }
       }
     } 
     'scriptPath'('Jenkinsfile')
     'lightweight'(true)
   }
 }
//pipeline 
}
//for loop
}

folder('Mutable') {
  displayName('Mutable')
  description('Mutable')
}

pipelineJob('Mutable/App-Deploy') {
 configure { flowdefinition ->
   flowdefinition << delegate.'definition'(class:'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition',plugin:'workflow-cps') {
     'scm'(class:'hudson.plugins.git.GitSCM',plugin:'git') {
       'userRemoteConfigs' {
         'hudson.plugins.git.UserRemoteConfig' {
           'url'("https://github.com/MunishLagoo/jenkins.git")
           //'url'('https://DevOps-Batches@dev.azure.com/DevOps-Batches/DevOps60/_git/frontend')
         }
       }
       'branches' {
         'hudson.plugins.git.BranchSpec' {
           'name'('*/main')
         }
       }
     }
     'scriptPath'('Jenkinsfile-mutable-app-deploy')
     'lightweight'(true)
   }
 }
}

pipelineJob('Mutable/Infra-Create') {
 configure { flowdefinition ->
   flowdefinition << delegate.'definition'(class:'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition',plugin:'workflow-cps') {
     'scm'(class:'hudson.plugins.git.GitSCM',plugin:'git') {
       'userRemoteConfigs' {
         'hudson.plugins.git.UserRemoteConfig' {
           'url'("https://github.com/MunishLagoo/jenkins.git")
           //'url'('https://DevOps-Batches@dev.azure.com/DevOps-Batches/DevOps60/_git/frontend')
         }
       }
       'branches' {
         'hudson.plugins.git.BranchSpec' {
           'name'('*/main')
         }
       }
     }
     'scriptPath'('Jenkinsfile-create-mutable-infra')
     'lightweight'(true)
   }
 }
}


pipelineJob('Mutable/Infra-Destroy') {
 configure { flowdefinition ->
   flowdefinition << delegate.'definition'(class:'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition',plugin:'workflow-cps') {
     'scm'(class:'hudson.plugins.git.GitSCM',plugin:'git') {
       'userRemoteConfigs' {
         'hudson.plugins.git.UserRemoteConfig' {
           'url'("https://github.com/MunishLagoo/jenkins.git")
           //'url'('https://DevOps-Batches@dev.azure.com/DevOps-Batches/DevOps60/_git/frontend')
         }
       }
       'branches' {
         'hudson.plugins.git.BranchSpec' {
           'name'('*/main')
         }
       }
     }
     'scriptPath'('Jenkinsfile-destroy-mutable-infra')
     'lightweight'(true)
   }
 }
}

pipelineJob('Helm-Deploy') {
 configure { flowdefinition ->
   flowdefinition << delegate.'definition'(class:'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition',plugin:'workflow-cps') {
     'scm'(class:'hudson.plugins.git.GitSCM',plugin:'git') {
       'userRemoteConfigs' {
         'hudson.plugins.git.UserRemoteConfig' {
           'url'("https://github.com/MunishLagoo/jenkins.git")
           //'url'('https://DevOps-Batches@dev.azure.com/DevOps-Batches/DevOps60/_git/frontend')
         }
       }
       'branches' {
         'hudson.plugins.git.BranchSpec' {
           'name'('*/main')
         }
       }
     }
     'scriptPath'('Jenkinsfile-helm-deploy')
     'lightweight'(true)
   }
 }
}


pipelineJob('k8s-DB-Deploy') {
 configure { flowdefinition ->
   flowdefinition << delegate.'definition'(class:'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition',plugin:'workflow-cps') {
     'scm'(class:'hudson.plugins.git.GitSCM',plugin:'git') {
       'userRemoteConfigs' {
         'hudson.plugins.git.UserRemoteConfig' {
           'url'("https://github.com/MunishLagoo/jenkins.git")
           //'url'('https://DevOps-Batches@dev.azure.com/DevOps-Batches/DevOps60/_git/frontend')
         }
       }
       'branches' {
         'hudson.plugins.git.BranchSpec' {
           'name'('*/main')
         }
       }
     }
     'scriptPath'('Jenkinsfile-all-components')
     'lightweight'(true)
   }
 }
}

