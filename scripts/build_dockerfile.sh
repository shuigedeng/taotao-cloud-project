#!/bin/bash

current_dir=`dirname $(pwd)`

JAVA_HOME="/opt/common/jdk-11.0.2"

VERSION="2021.9.3"

function build_dockerfile() {
	docker_dir=$current_dir/docker
    for project in `ls $docker_dir`
    do
      if [ -d $docker_dir"/"$project ];then
        cd $docker_dir"/"$project
        docker build -t registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/$file:$VERSION .
      fi
    done
}

function jar_biz() {
	microservice_dir=$current_dir/taotao-cloud-microservice
    for microservice in `ls $microservice_dir`
    do
      if [ -d $microservice_dir"/"$microservice && $microservice =~ "taotao-cloud" ];then
        cd $microservice_dir"/"$microservice
        gradle clean bootJar -Dorg.gradle.java.home=$JAVA_HOME

        for item in $(ls $microservice_dir"/"$microservice)
        do
          if [[ -d $microservice_dir"/"$microservice"/"$item && $item =~ "taotao-cloud" ]]; then
          cd $microservice_dir"/"$microservice"/"$item
          gradle clean bootJar -Dorg.gradle.java.home=$JAVA_HOME
          fi
        done
      fi
    done
}

#function build_starters() {
#    for file in `ls $1`
#    do
#      if [ -d $1"/"$file ];then
#        cd $1"/"$file
#        gradle clean build -Dorg.gradle.java.home=$JAVA_HOME
#      fi
#    done
#}
#
#build_starters $current_dir/taotao-cloud-microservice/taotao-cloud-starter
#sleep 10
#echo build_starters done

./build_starters.sh

jar_biz
sleep 30
echo jar_biz done

build_dockerfile $current_dir/taotao-cloud-microservice
echo build_dockerfile done
