#!/bin/bash

projects=("taotao-cloud-gateway"
          "taotao-cloud-admin"
          "taotao-cloud-xxl-job-admin"
          "taotao-cloud-oauth2"
          "taotao-cloud-uc"
          "taotao-cloud-order")

current_dir=`dirname $(pwd)`

JAVA_HOME="/opt/common/jdk-11.0.2"

VERSION="2021.9.3"

function build_dockerfile() {
    for file in ${projects[@]}
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        docker build -t registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/$file:$VERSION .
      fi
    done
}

function jar_biz() {
    for file in ${projects[@]}
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        gradle clean bootJar -Dorg.gradle.java.home=$JAVA_HOME

        for item in $(ls $1"/"$file)
        do
          if [[ -d $1"/"$file && $item =~ $file ]]; then
          cd $1"/"$file"/"$item
          gradle clean bootJar -Dorg.gradle.java.home=$JAVA_HOME
          fi
        done
      fi
    done
}

function build_starters() {
    for file in `ls $1`
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        gradle clean build -Dorg.gradle.java.home=$JAVA_HOME
      fi
    done
}

build_starters $current_dir/taotao-cloud-microservice/taotao-cloud-starter
sleep 10
echo build_starters done

jar_biz $current_dir/taotao-cloud-microservice
sleep 30
echo jar_biz done

build_dockerfile $current_dir/taotao-cloud-microservice
echo build_dockerfile done
