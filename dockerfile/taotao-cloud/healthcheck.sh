#!/bin/sh

# argv

# functions

function _main
{
  health_status_code=$(curl -s -o /dev/null -w "%{http_code}" -X GET "http://127.0.0.1:$APP_PORT/actuator/health")
  api_status_code=$(curl -s -o /dev/null -w "%{http_code}" -X GET "http://127.0.0.1:$APP_PORT/v3/api-docs")

  [[ $health_status_code -eq 200 && $health_status_code -eq 200 ]] && exit 1

  exit 0
}

# main

_main "$@"


##!/bin/sh
#
#curl -fs http://127.0.0.1:$APP_PORT/actuator/health
#health_status_code=$(curl -s -o /dev/null -w "%{http_code}" -X GET "http://127.0.0.1:$APP_PORT/actuator/health")
#echo $health_status_code
#
#curl -fs http://127.0.0.1:$APP_PORT/v3/api-docs
#api_status_code=$(curl -s -o /dev/null -w "%{http_code}" -X GET "http://127.0.0.1:$APP_PORT/v3/api-docs")
#echo $api_status_code
#
#echo success
