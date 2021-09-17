#!/bin/zsh

function deploy_dependencies() {
    cd $1
    gradle publishAllPublicationsToSonatypeRepository -Dorg.gradle.java.home='/Users/dengtao/software/jdk-11.0.7/Contents/Home'
}

function deploy_starters() {
    for file in `ls $1`
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        gradle publishAllPublicationsToSonatypeRepository -Dorg.gradle.java.home='/Users/dengtao/software/jdk-11.0.7/Contents/Home'
      fi
    done
}

#deploy_dependencies $(dirname $(pwd))/taotao-cloud-dependencies

deploy_starters $(dirname $(pwd))/taotao-cloud-microservice/taotao-cloud-starter
