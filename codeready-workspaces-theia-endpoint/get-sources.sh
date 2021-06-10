#!/bin/bash -xe
# script to get tarball(s) from quay
verbose=1
scratchFlag=""
JOB_BRANCH=""
doRhpkgContainerBuild=1
forceBuild=0

while [[ "$#" -gt 0 ]]; do
  case $1 in
  '-n'|'--nobuild') doRhpkgContainerBuild=0; shift 0;;
  '-f'|'--force-build') forceBuild=1; shift 0;;
  '-s'|'--scratch') scratchFlag="--scratch"; shift 0;;
  *) JOB_BRANCH="$1"; shift 0;;
  esac
  shift 1
done

# if not set, compute from current branch
if [[ ! ${JOB_BRANCH} ]]; then 
  DWNSTM_BRANCH="$(git rev-parse --abbrev-ref HEAD 2>/dev/null || true)"
  JOB_BRANCH=${DWNSTM_BRANCH//crw-}; JOB_BRANCH=${JOB_BRANCH%%-rhel*}
else
  if [[ ${JOB_BRANCH} == "2.x" ]]; then 
    DWNSTM_BRANCH="crw-2-rhel-8"
  else
    DWNSTM_BRANCH="crw-${JOB_BRANCH}-rhel-8"
  fi
fi
if [[ ${JOB_BRANCH} == "2" ]]; then JOB_BRANCH="2.x"; fi
# echo "Got DWNSTM_BRANCH=${DWNSTM_BRANCH} and JOB_BRANCH=${JOB_BRANCH}"

function log()
{
  if [[ ${verbose} -gt 0 ]]; then
    echo "$1"
  fi
}

OLD_SHA="$(git rev-parse --short=4 HEAD)"
# collect assets by running collect-assets.sh
./build/scripts/collect-assets.sh --cb ${DWNSTM_BRANCH} --target $(pwd)/ -e --rmi:tmp --ci --commit
NEW_SHA="$(git rev-parse --short=4 HEAD)"

if [[ "${OLD_SHA}" != "${NEW_SHA}" ]]; then
  if [[ ${doRhpkgContainerBuild} -eq 1 ]]; then
    echo "[INFO] #1 Trigger container-build in current branch: rhpkg container-build ${scratchFlag}"
    git status || true
    tmpfile=$(mktemp) && rhpkg container-build ${scratchFlag} --nowait | tee 2>&1 $tmpfile
    taskID=$(cat $tmpfile | grep "Created task:" | sed -e "s#Created task:##") && brew watch-logs $taskID | tee 2>&1 $tmpfile
    ERRORS="$(grep "image build failed" $tmpfile)" && rm -f $tmpfile
    if [[ "$ERRORS" != "" ]]; then echo "Brew build has failed:

$ERRORS

"; exit 1; fi
  fi
else
  if [[ ${forceBuild} -eq 1 ]]; then
    echo "[INFO] #2 Trigger container-build in current branch: rhpkg container-build ${scratchFlag}"
    git status || true
    tmpfile=$(mktemp) && rhpkg container-build ${scratchFlag} --nowait | tee 2>&1 $tmpfile
    taskID=$(cat $tmpfile | grep "Created task:" | sed -e "s#Created task:##") && brew watch-logs $taskID | tee 2>&1 $tmpfile
    ERRORS="$(grep "image build failed" $tmpfile)" && rm -f $tmpfile
    if [[ "$ERRORS" != "" ]]; then echo "Brew build has failed:

$ERRORS

"; exit 1; fi
  else
    log "[INFO] No new sources, so nothing to build."
  fi
fi
