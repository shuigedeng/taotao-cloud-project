#!/bin/bash
set -e

source ~/.bashrc

FLUME_CONF_DIR=/opt/flume/conf
FLUME_AGENT_NAME=agent

[[ -d "${FLUME_CONF_DIR}"  ]]  || { echo "Flume config file not mounted in /opt/flume/conf";  exit 1; }
[[ -z "/opt/flume/bin/flume-ng" ]] && { echo "FLUME_AGENT_NAME required"; exit 1; }
[[ -z "${FLUME_AGENT_NAME}" ]] && { echo "FLUME_AGENT_NAME required"; exit 1; }

echo "Starting flume agent : ${FLUME_AGENT_NAME}"

if test -x /opt/flume/bin/flume-ng ; then
  echo "/opt/flume/bin/flume-ng 具有执行"
else
  echo "/opt/flume/bin/flume-ng 不具有执行"
  sudo chmod +x /opt/flume/bin/flume-ng
  ls -l /opt/flume/bin/flume-ng
fi

flume-ng agent -c ${FLUME_CONF_DIR}  -f ${FLUME_CONF_DIR}/kafka_flume_hadoop.conf -n ${FLUME_AGENT_NAME} -Dflume.root.logger=INFO,LOGFILE


