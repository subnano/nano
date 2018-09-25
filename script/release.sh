#!/bin/bash

# establish the current project version
echo "Determining current version ..."
CURRENT_VER=$(mvn help:evaluate -Dexpression=project.version | grep -v '\[')

# Make sure version is sane
if [[ ${CURRENT_VER} =~ ^[0-9]{4}.[0-9]{2}.[0-9]+$ ]] ;then
    echo "Current version: $CURRENT_VER"
elif [[ ${CURRENT_VER} =~ ^[0-9]{4}.[0-9]{2}.[0-9]+-SNAPSHOT$ ]] ;then
    echo "Current version: $CURRENT_VER"
else
    echo "Invalid version format: $CURRENT_VER"
    exit 1
fi

# Determine the base version from current date
BASE_VER=`date +%Y.%02m`

# if base doesn't match then assume first release this month
if [[ ${CURRENT_VER} != *$BASE_VER* ]] ;then
    BUILD_NUM=1

# if it ends with -SNAPSHOT then use this build number
elif [[ ${CURRENT_VER} == *"-SNAPSHOT"* ]] ;then
    BUILD_NUM=${CURRENT_VER/-SNAPSHOT/}
    BUILD_NUM=${BUILD_NUM/$BASE_VER./}

# if not a snapshot then use the next number
else
    BUILD_NUM=$(expr ${CURRENT_VER/$BASE_VER./} + 1)
fi

RELEASE_VER="$BASE_VER.$BUILD_NUM"
NEXT_BUILD_NUM=$(expr ${BUILD_NUM} + 1)
DEV_VER="$BASE_VER.$NEXT_BUILD_NUM-SNAPSHOT"

echo "Release version: $RELEASE_VER"
echo "Next development version: $DEV_VER"
echo

# Bump to next release version
echo "Setting version to release version $RELEASE_VER .."
mvn -q versions:set -DnewVersion=$RELEASE_VER -DgenerateBackupPoms=false
git add -A
git commit -m "Bump version to $RELEASE_VER"
git push
echo

# this does the pushing to remote too
echo "Tagging $RELEASE_VER .."
git tag $RELEASE_VER
git push origin $RELEASE_VER
echo

# Now bump to next development version
echo "Setting version to next development version $DEV_VER .."
mvn -q versions:set -DnewVersion=$DEV_VER -DgenerateBackupPoms=false
git add -A
git commit -m "Bump version to $DEV_VER"
git push
