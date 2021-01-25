def JOB_BRANCHES = ["2.6"] // , "2.7"]
for (String JOB_BRANCH : JOB_BRANCHES) {
    pipelineJob("${FOLDER_PATH}/${ITEM_NAME}"){
        MIDSTM_BRANCH="crw-"+JOB_BRANCH+"-rhel-8"

        description('''
Send an email to QE announcing an ER or RC build, including a list of images.
        ''')

        properties {
            ownership {
                primaryOwnerId("nboldt")
            }
        }

        throttleConcurrentBuilds {
            maxPerNode(1)
            maxTotal(1)
        }

        logRotator {
            daysToKeep(5)
            numToKeep(5)
            artifactDaysToKeep(2)
            artifactNumToKeep(2)
        }

        parameters{
            MMdd = ""+(new java.text.SimpleDateFormat("MM-dd")).format(new Date())
            stringParam("mailSubject","CRW " + JOB_BRANCH + ".0.tt-" + MMdd + " ready for QE",
'''email subject should be one of two formats:
* CRW ''' + JOB_BRANCH + '''.0.ER-''' + MMdd + ''' ready for QE
* CRW ''' + JOB_BRANCH + '''.0.RC-''' + MMdd + ''' ready for QE
''')
            stringParam("errataURL","https://errata.devel.redhat.com/advisory/65969",'')
            stringParam("unresolvedCriticalsBlockersURL","https://issues.redhat.com/browse/CRW-1479?jql=fixversion%20%3D%20" + JOB_BRANCH + ".0.GA%20AND%20project%20%3D%20CRW%20AND%20priority%20%3E%20Major%20AND%20resolution%20is%20null",'''query for unresolved criticals/blockers for the current release''')
            textParam("additionalNotes",
'''Additional Info:

stuff goes here if applicable''',"Stuff to mention after the lists of images")
            booleanParam("doSendEmail",false,'''if checked, send mail; if not, draft email but do not send''')
            booleanParam("doOSBS",false,'''if checked, include OSBS images in email''')
            booleanParam("doStage",false,'''if checked, include RHCC stage images in email''')
            stringParam("recipientOverride","nboldt@redhat.com",'''if set, send mail to recipient(s) listed rather than default mailing lists''')

            stringParam("MIDSTM_BRANCH",MIDSTM_BRANCH,"redhat-developer/codeready-workspaces branch to update")
            stringParam("JOB_BRANCH", JOB_BRANCH)
        }

        // Trigger builds remotely (e.g., from scripts), using Authentication Token = CI_BUILD
        authenticationToken('CI_BUILD')

        definition {
            cps{
                sandbox(true)
                script(readFileFromWorkspace('jobs/CRW_CI/Releng/send-email-qe-build-list.jenkinsfile'))
            }
        }
    }
}
