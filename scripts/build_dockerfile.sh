#!/bin/bash

function build_dockerfile() {
    projects = ('taotao-cloud-gateway',
                'taotao-cloud-admin',
                'taotao-cloud-xxl-job-admin',
                'taotao-cloud-oauth2',
                'taotao-cloud-uc',
                'taotao-cloud-order')

    for file in $projects
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        docker build -t registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/$file:2021.9.3 .
      fi
    done
}

build_dockerfile $(dirname $(pwd))/taotao-cloud-microservice
