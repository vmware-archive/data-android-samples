#!/bin/bash
set -e
set -x

mvn deploy:deploy-file -Dfile=$1 -Durl=file://`mvn help:evaluate -Dexpression=settings.localRepository | grep -v '[INFO]'` -DgroupId=$2 -DartifactId=$3 -Dversion=$4 -Dpackaging=jar
