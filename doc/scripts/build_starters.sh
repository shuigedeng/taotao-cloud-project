#!/bin/bash

current_dir=`dirname $(pwd)`

#JAVA_HOME="/opt/common/jdk-21"
#JAVA_HOME="/Users/shuigedeng/software/jdk-21.1/Contents/Home"
JAVA_HOME="/Users/shuigedeng/software/jdk-21.jdk/Contents/Home"

function install_dependencies() {
    cd $current_dir/taotao-cloud-dependencies
    gradle publishToMavenLocal -Dorg.gradle.java.home=$JAVA_HOME
}

function build_starters() {
    starter_dir=$current_dir/taotao-cloud-microservice/taotao-cloud-starter
    for starter in `ls $starter_dir`
    do
      starter_inner_dir="$starter_dir/$starter"
      if [ -d $starter_inner_dir ];then
        if [ -d "$starter_inner_dir/src" ]; then
          cd $starter_inner_dir
          gradle clean build clean build -x test -x bootJar -Dorg.gradle.java.home=$JAVA_HOME
        else
          for two_starter in `ls $starter_inner_dir`
          do
            if [ -d "$starter_inner_dir/$two_starter" ]; then
              cd "$starter_inner_dir/$two_starter"
              gradle clean build clean build -x test -x bootJar -Dorg.gradle.java.home=$JAVA_HOME
            fi
          done
        fi
      fi
    done
}

#install_dependencies

build_starters
