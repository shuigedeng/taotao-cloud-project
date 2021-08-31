#!/bin/zsh

function clean_starters() {
    for file in `ls $1`
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        gradle build
      fi
    done
}

clean_starters $(dirname $(pwd))/taotao-cloud-microservice/taotao-cloud-starter
