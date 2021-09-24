#!/bin/bash

current_dir=`dirname $(pwd)`

JAVA_HOME="/opt/common/jdk-11.0.2"

VERSION="2021.9.3"

function build_dockerfile() {
	microservice_dir=$current_dir/taotao-cloud-microservice
    for microservice in `ls $microservice_dir`
    do
      if [[ -d $microservice_dir"/"$microservice && $microservice =~ "taotao-cloud" && ! $microservice =~ "starter" ]]; then
        cd $microservice_dir"/"$microservice
        gradle clean bootJar -Dorg.gradle.java.home=$JAVA_HOME

        val=`ls . | grep Dockerfile | wc -w`
		if [ $val -eq 1]; then
		  docker build -t registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/$microservice:$VERSION .
		fi

        for item in $(ls $microservice_dir"/"$microservice)
        do
          if [[ -d $microservice_dir"/"$microservice"/"$item && $item =~ "taotao-cloud" ]]; then
          	cd $microservice_dir"/"$microservice"/"$item
          	gradle clean bootJar -Dorg.gradle.java.home=$JAVA_HOME

          	v=`ls . | grep Dockerfile | wc -w`
		  	if [ $v -eq 1]; then
		  	  docker build -t registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/$item:$VERSION .
		  	fi
          fi
        done
      fi
    done
}

./build_starters.sh
sleep 10
echo build_starters done

build_dockerfile
sleep 10
echo build_dockerfile done
