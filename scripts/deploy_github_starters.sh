#!/bin/zsh

function deploy_dependencies() {
    cd $1
    gradle publishMavenJavaPublicationToGitHubRepository -Dorg.gradle.java.home='/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home'
}

function deploy_starters() {
    for file in `ls $1`
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        gradle publishMavenJavaPublicationToGitHubRepository -Dorg.gradle.java.home='/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home'
      fi
    done
}

#deploy_dependencies $(dirname $(pwd))/taotao-cloud-dependencies

deploy_starters $(dirname $(pwd))/taotao-cloud-microservice/taotao-cloud-starter
