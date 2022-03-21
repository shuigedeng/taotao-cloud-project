version: '3'
services:
    rabbitmq1:
        image: rabbitmq:3.7-management
        deploy:
          resources:
            limits:
              cpus: '2'
              memory: 8G
          restart_policy:
            condition: any
        ports:
          - "15672:15672"
          - "5672:5672"
        hostname: rabbitmq1
        container_name: rabbitmq1
        environment:
          - RABBITMQ_ERLANG_COOKIE=rabbitcookie
        volumes:
          - /data/docker/data/rabbitmq/rabbitmq1:/var/lib/rabbitmq
          - /etc/localtime:/etc/localtime


    rabbitmq2:
        image: rabbitmq:3.7-management
        deploy:
          resources:
            limits:
              cpus: '2'
              memory: 8G
          restart_policy:
            condition: any
        ports:
            - "5673:5672"
        hostname: rabbitmq2
        container_name: rabbitmq2
        environment:
          - RABBITMQ_ERLANG_COOKIE=rabbitcookie
        volumes:
          - /data/docker/data/rabbitmq/rabbitmq2:/var/lib/rabbitmq
          - /etc/localtime:/etc/localtime


    rabbitmq3:
        image: rabbitmq:3.7-management
        deploy:
          resources:
            limits:
              cpus: '2'
              memory: 8G
          restart_policy:
            condition: any
        ports:
          - "5674:5672"
        hostname: rabbitmq3
        container_name: rabbitmq3
        environment:
          - RABBITMQ_ERLANG_COOKIE=rabbitcookie
        volumes:
          - /data/docker/data/rabbitmq/rabbitmq3:/var/lib/rabbitmq
          - /etc/localtime:/etc/localtime



#!/bin/bash

#reset first node
echo "Reset first rabbitmq node."
docker exec rabbitmq1 /bin/bash -c 'rabbitmqctl stop_app'
docker exec rabbitmq1 /bin/bash -c 'rabbitmqctl reset'
docker exec rabbitmq1 /bin/bash -c 'rabbitmqctl start_app'

#build cluster
echo "Starting to build rabbitmq cluster with two ram nodes."
docker exec rabbitmq2 /bin/bash -c 'rabbitmqctl stop_app'
docker exec rabbitmq2 /bin/bash -c 'rabbitmqctl reset'
docker exec rabbitmq2 /bin/bash -c 'rabbitmqctl join_cluster --ram rabbit@rabbitmq1'
docker exec rabbitmq2 /bin/bash -c 'rabbitmqctl start_app'

docker exec rabbitmq3 /bin/bash -c 'rabbitmqctl stop_app'
docker exec rabbitmq3 /bin/bash -c 'rabbitmqctl reset'
docker exec rabbitmq3 /bin/bash -c 'rabbitmqctl join_cluster --ram rabbit@rabbitmq1'
docker exec rabbitmq3 /bin/bash -c 'rabbitmqctl start_app'

#check cluster status
echo "Check cluster status:"
docker exec rabbitmq1 /bin/bash -c 'rabbitmqctl cluster_status'
docker exec rabbitmq2 /bin/bash -c 'rabbitmqctl cluster_status'
docker exec rabbitmq3 /bin/bash -c 'rabbitmqctl cluster_status'

echo "Starting to create user."
docker exec rabbitmq1 /bin/bash -c 'rabbitmqctl add_user admin admin@123'

echo "Set tags for new user."
docker exec rabbitmq1 /bin/bash -c 'rabbitmqctl set_user_tags admin administrator'

echo "Grant permissions to new user."
docker exec rabbitmq1 /bin/bash -c "rabbitmqctl set_permissions -p '/' admin '.*' '.*' '.*'"
