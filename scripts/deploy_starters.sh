#!/bin/zsh

function deploy_dependencies() {
    cd $1
    gradle publishMavenJavaPublicationToSonatypeRepository
}

function deploy_starters() {
    for file in `ls $1`
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        gradle publishMavenJavaPublicationToSonatypeRepository
      fi
    done
}

deploy_dependencies $(dirname $(pwd))/taotao-cloud-dependencies

deploy_starters $(dirname $(pwd))/taotao-cloud-microservice/taotao-cloud-starter
