#!/bin/bash
set -e

source ~/.bashrc

echo "Starting sshd service..."
/etc/init.d/ssh start

# the first argument provided is a comma-separated list of all ZooKeeper servers in the ensemble:
export BROKER_SERVERS=$1
# the second argument provided is vat of this ZooKeeper node:
export BROKER_ID=$2

# Put all ZooKeeper server IPs into an array:
IFS=', ' read -r -a BROKER_SERVERS_ARRAY <<< "$BROKER_SERVERS"
export BROKER_SERVERS_ARRAY=$BROKER_SERVERS_ARRAY
# now append information on every ZooKeeper node in the ensemble to the ZooKeeper config:
for index in "${!BROKER_SERVERS_ARRAY[@]}"
do
  ZKID=$(($index+1))
  ZKIP=${BROKER_SERVERS_ARRAY[index]}
  NEW_ZKIP=${ZKIP}

  echo

  if [ $ZKID == $BROKER_ID ]
  then
      # if IP's are used instead of hostnames, every ZooKeeper host has to specify itself as follows
      NEW_ZKIP=0.0.0.0
  fi

  BROKER_CONFIG=
  BROKER_CONFIG="$BROKER_CONFIG"$'\n'"broker.id=${ZKID}"
  BROKER_CONFIG="$BROKER_CONFIG"$'\n'"listeners=PLAINTEXT://${ZKIP}:909${ZKID}"
  BROKER_CONFIG="$BROKER_CONFIG"$'\n'"log.dir=/tmp/kafka-logs-${ZKID}"
  BROKER_CONFIG="$BROKER_CONFIG"$'\n'"offsets.topic.replication.factor=1"
  BROKER_CONFIG="$BROKER_CONFIG"$'\n'"transaction.state.log.replication.factor=1"
  BROKER_CONFIG="$BROKER_CONFIG"$'\n'"transaction.state.log.min.isr=1"
  BROKER_CONFIG="$BROKER_CONFIG"$'\n'"zookeeper.connect=${KAFKA_ZOOKEEPER_CONNECT}"

  echo "$BROKER_CONFIG" | tee ${KAFKA_HOME}/config/server-${ZKID}.properties
done
# Finally, write config file:

echo "Start Kafka"
${KAFKA_HOME}/bin/kafka-server-start.sh ${KAFKA_HOME}/config/server-${BROKER_ID}.properties
