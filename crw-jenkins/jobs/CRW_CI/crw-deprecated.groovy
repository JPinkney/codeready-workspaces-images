def JOB_BRANCHES = ["2.6":"", "2.7":"", "2":""] // special case, no upstream branches
for (JB in JOB_BRANCHES) {
    SOURCE_BRANCH=JB.value // note: not used
    JOB_BRANCH=JB.key
    MIDSTM_BRANCH="crw-"+JOB_BRANCH+"-rhel-8"
    jobPath="${FOLDER_PATH}/${ITEM_NAME}_" + JOB_BRANCH
    if (JOB_BRANCH.equals("2")) { jobPath="${FOLDER_PATH}/${ITEM_NAME}_" + JOB_BRANCH + ".x" }
    pipelineJob(jobPath){
        description('''
Lang server dependency builder
<ul>
<li>Upstream: <a href=https://github.com/redhat-developer/codeready-workspaces-deprecated/tree/''' + MIDSTM_BRANCH + '''/>codeready-workspaces-deprecated</a></li>
<li>Midstream: <a href=https://github.com/redhat-developer/codeready-workspaces-images/tree/''' + MIDSTM_BRANCH + '''>codeready-workspaces-images</a></li>
</ul>

<p>When done, downstream builds can be triggered using these artifacts using 
<a href=../crw-plugins-and-stacks_''' + JOB_BRANCH + '''/>crw-plugins-and-stacks_''' + JOB_BRANCH + '''</a>
        ''')

        properties {
            disableConcurrentBuilds()
            // quietPeriod(30) // no more than one build every 30s

            ownership {
                primaryOwnerId("nboldt")
            }

            // poll SCM every 2 hrs for changes in upstream
            pipelineTriggers {
                [$class: "SCMTrigger", scmpoll_spec: "H H/2 * * *"]
            }
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