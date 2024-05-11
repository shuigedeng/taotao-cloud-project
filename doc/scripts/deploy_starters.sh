#!/bin/bash

current_dir=`dirname $(pwd)`

#JAVA_HOME="/opt/common/jdk-21"
JAVA_HOME="/Users/shuigedeng/software/jdk-21.1/Contents/Home"

function deploy_dependencies() {
    cd $current_dir/taotao-cloud-dependencies
    gradle $1 -Dorg.gradle.java.home=$JAVA_HOME
}

function deploy_starters() {
    starter_dir=$current_dir/taotao-cloud-microservice/taotao-cloud-starter
    for starter in `ls $starter_dir`
    do
      starter_inner_dir="$starter_dir/$starter"
      if [ -d $starter_inner_dir ];then
        if [ -d "$starter_inner_dir/src" ]; then
          cd $starter_inner_dir
          echo `pwd`
          gradle $1 -Dorg.gradle.java.home=$JAVA_HOME
        else
          for two_starter in `ls $starter_inner_dir`
          do
            if [ -d "$starter_inner_dir/$two_starter" ]; then
              cd "$starter_inner_dir/$two_starter"
              echo `pwd`
              gradle $1 -Dorg.gradle.java.home=$JAVA_HOME
            fi
          done
        fi
      fi
    done
}

./build_starters.sh

deploy_dependencies publishMavenJavaPublicationToSonatypeRepository
deploy_starters publishMavenJavaPublicationToSonatypeRepository

deploy_dependencies publishMavenJavaPublicationToGitHubRepository
deploy_starters publishMavenJavaPublicationToGitHubRepository

#deploy_dependencies publishMavenJavaPublicationToAliyunRepository
#deploy_starters publishMavenJavaPublicationToAliyunRepository
