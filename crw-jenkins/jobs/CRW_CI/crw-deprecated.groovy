def JOB_BRANCHES = ["2.8":"", "2.9":"", "2.x":""] // special case, no upstream branches
def JOB_DISABLED = ["2.8":true, "2.9":false, "2.x":true]
for (JB in JOB_BRANCHES) {
    SOURCE_BRANCH=JB.value // note: not used
    JOB_BRANCH=""+JB.key
    MIDSTM_BRANCH="crw-" + JOB_BRANCH.replaceAll(".x","") + "-rhel-8"
    UPSTM_NAME="codeready-workspaces-deprecated"
    SOURCE_REPO="redhat-developer/" + UPSTM_NAME

    jobPath="${FOLDER_PATH}/${ITEM_NAME}_" + JOB_BRANCH
    pipelineJob(jobPath){
        disabled(JOB_DISABLED[JB.key]) // on reload of job, disable to avoid churn
        description('''
Lang server dependency builder
<ul>
<li>Upstream: <a href=https://github.com/''' + SOURCE_REPO + '''/tree/''' + MIDSTM_BRANCH + '''/>''' + UPSTM_NAME + '''</a></li>
<li>Midstream: <a href=https://github.com/redhat-developer/codeready-workspaces-images/tree/''' + MIDSTM_BRANCH + '''>codeready-workspaces-images</a> (used by various container builds, including plugin sidecars and stacks)</li>
</ul>

<p>When done, downstream builds can be triggered using these artifacts using 
<a href=../sync-to-downstream_''' + JOB_BRANCH + '''/>sync-to-downstream_''' + JOB_BRANCH + '''</a>
        ''')

        properties {

            ownership {
                primaryOwnerId("nboldt")
            }

            githubProjectUrl("https://github.com/" + SOURCE_REPO)

            // disabled because no changes in the branch / run this manually 
            // pipelineTriggers {
            //     triggers{
            //         pollSCM{
            //             scmpoll_spec("H H/24 * * *") // every 24hrs
            //         }
            //     }
            // }

            disableResumeJobProperty()
            disableConcurrentBuildsJobProperty()
            quietPeriod(30) // no more than one build every 30s
        }

        logRotator {
            daysToKeep(5)
            numToKeep(5)
            artifactDaysToKeep(2)
            artifactNumToKeep(1)
        }

        parameters{
            stringParam("MIDSTM_BRANCH", MIDSTM_BRANCH)
            booleanParam("FORCE_BUILD", false, "If true, trigger a rebuild even if no changes were pushed to pkgs.devel")
        }

        // Trigger builds remotely (e.g., from scripts), using Authentication Token = CI_BUILD
        authenticationToken('CI_BUILD')

        definition {
            cps{
                sandbox(true)
                script(readFileFromWorkspace('jobs/CRW_CI/crw-deprecated_'+JOB_BRANCH+'.jenkinsfile'))
            }
        }
    }
}