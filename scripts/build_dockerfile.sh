#!/bin/bash

projects=("taotao-cloud-gateway"
          "taotao-cloud-admin"
          "taotao-cloud-xxl-job-admin"
          "taotao-cloud-oauth2"
          "taotao-cloud-uc"
          "taotao-cloud-order")

current_dir=`dirname $(pwd)`

function build_dockerfile() {
    for file in ${projects[@]}
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        docker build -t registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/$file:2021.9.3 .
      fi
    done
}

function build_biz() {
    for file in ${projects[@]}
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        gradle build -Dorg.gradle.java.home='/opt/common/jdk-11.0.2'
      fi
    done
}

function clean_starters() {
    for file in `ls $1`
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        gradle clean -Dorg.gradle.java.home='/opt/common/jdk-11.0.2'
      fi
    done
}

function build_starters() {
    for file in `ls $1`
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        gradle build -Dorg.gradle.java.home='/opt/common/jdk-11.0.2'
      fi
    done
}

clean_starters $current_dir/taotao-cloud-microservice/taotao-cloud-starter
sleep 10
echo clean_starters done

build_starters $current_dir/taotao-cloud-microservice/taotao-cloud-starter
sleep 10
echo build_starters done

build_biz $current_dir/taotao-cloud-microservice
sleep 30
echo build_biz done

build_dockerfile $current_dir/taotao-cloud-microservice
echo build_dockerfile done
