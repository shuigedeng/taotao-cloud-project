#!/bin/bash

current_dir=`dirname $(pwd)`

#JAVA_HOME="/opt/common/jdk-21"
JAVA_HOME="/Users/shuigedeng/software/jdk-21.jdk/Contents/Home"

VERSION=2025.09

function build_dockerfile() {
	microservice_dir=$current_dir/taotao-cloud-microservice
    for microservice in `ls $microservice_dir`
    do
      if [[ -d $microservice_dir"/"$microservice && $microservice =~ "taotao-cloud" && ! $microservice =~ "starter" ]]; then
        cd $microservice_dir"/"$microservice
        gradle clean bootJar -Dorg.gradle.java.home=$JAVA_HOME

        dockerfile=`ls . | grep Dockerfile | wc -w`
        if [[ $dockerfile -eq 1 ]]; then
          docker build -t registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/$microservice:$VERSION .
        fi
        #kubernetes=`ls . | grep kubernetes | wc -w`
        #if [[ $kubernetes -eq 1 ]]; then
        #  kubectl apply -f  kubernetes.yml
        #fi

        for item in $(ls $microservice_dir"/"$microservice)
        do
          if [[ -d $microservice_dir"/"$microservice"/"$item && $item =~ "taotao-cloud" ]]; then
          	cd $microservice_dir"/"$microservice"/"$item
          	gradle clean bootJar -Dorg.gradle.java.home=$JAVA_HOME

          	biz_dockerfile=`ls . | grep Dockerfile | wc -w`
            if [[ $biz_dockerfile -eq 1 ]]; then
              docker build -t registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/$item:$VERSION .
            fi
            #biz_kubernetes=`ls . | grep kubernetes | wc -w`
            #if [[ $biz_kubernetes -eq 1 ]]; then
            #  kubectl apply -f  kubernetes.yml
            #fi
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
