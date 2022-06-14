version: '3.8'
services:
  rabbitmq1:
    image: rabbitmq:management
    container_name: rabbitmq1
    restart: always
    hostname: rabbitmq1
    ports:
      - "5672:5672"
      - "15672:15672"
    volumes:
      - /opt/docker_volume/rabbitmq/rabbitmq1/data:/var/lib/rabbitmq
      - /opt/docker_compose_yml/rabbitmq/rabbitmq-ram.sh:/opt/rabbitmq/rabbitmq-ram.sh
      - /opt/docker_compose_yml/rabbitmq/rabbitmq-disk.sh:/opt/rabbitmq/rabbitmq-disk.sh
      - /opt/docker_compose_yml/rabbitmq/hosts:/etc/hosts
    environment:
      - RABBITMQ_DEFAULT_USER=root
      - RABBITMQ_DEFAULT_PASS=root
      - RABBITMQ_ERLANG_COOKIE=CURIOAPPLICATION
    networks:
      rabbitmq:
        ipv4_address: 172.25.0.2

  rabbitmq2:
    image: rabbitmq:management
    container_name: rabbitmq2
    restart: always
    hostname: rabbitmq2
    ports:
      - "5673:5672"
    volumes:
      - /opt/docker_volume/rabbitmq/rabbitmq2/data:/var/lib/rabbitmq
      - /opt/docker_compose_yml/rabbitmq/rabbitmq-ram.sh:/opt/rabbitmq/rabbitmq-ram.sh
      - /opt/docker_compose_yml/rabbitmq/rabbitmq-disk.sh:/opt/rabbitmq/rabbitmq-disk.sh
      - /opt/docker_compose_yml/rabbitmq/hosts:/etc/hosts
    environment:
      - RABBITMQ_ERLANG_COOKIE=CURIOAPPLICATION
    networks:
      rabbitmq:
        ipv4_address: 172.25.0.3

  rabbitmq3:
    image: rabbitmq:management
    container_name: rabbitmq3
    restart: always
    hostname: rabbitmq3
    ports:
      - "5674:5672"
    volumes:
      - /opt/docker_volume/rabbitmq/rabbitmq3/data:/var/lib/rabbitmq
      - /opt/docker_compose_yml/rabbitmq/rabbitmq-ram.sh:/opt/rabbitmq/rabbitmq-ram.sh
      - /opt/docker_compose_yml/rabbitmq/rabbitmq-disk.sh:/opt/rabbitmq/rabbitmq-disk.sh
      - /opt/docker_compose_yml/rabbitmq/hosts:/etc/hosts
    environment:
      - RABBITMQ_ERLANG_COOKIE=CURIOAPPLICATION
    networks:
      rabbitmq:
        ipv4_address: 172.25.0.4

networks:
  rabbitmq:
    driver: bridge
    ipam:
      config:
        - subnet: "172.25.0.1/24"
