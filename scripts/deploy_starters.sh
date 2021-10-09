#!/bin/bash

current_dir=`dirname $(pwd)`

JAVA_HOME="/opt/common/jdk-17"
#JAVA_HOME="/Users/dengtao/software/jdk-11.0.7/Contents/Home"

function deploy_dependencies() {
    cd $current_dir/taotao-cloud-dependencies
    gradle $1 -Dorg.gradle.java.home=$JAVA_HOME
}

function deploy_starters() {
    starter_dir=$current_dir/taotao-cloud-microservice/taotao-cloud-starter
    for starter in `ls $starter_dir`
    do
      if [ -d $starter_dir"/"$starter ];then
        cd $starter_dir"/"$starter
        gradle $1 -Dorg.gradle.java.home=$JAVA_HOME
      fi
    done
}

./build_starters.sh

deploy_dependencies publishAllPublicationsToSonatypeRepository 
deploy_starters publishAllPublicationsToSonatypeRepository

deploy_dependencies publishMavenJavaPublicationToGitHubRepository
deploy_starters publishMavenJavaPublicationToGitHubRepository

#deploy_dependencies publishMavenJavaPublicationToAliyunRepository
#deploy_starters publishMavenJavaPublicationToAliyunRepository
