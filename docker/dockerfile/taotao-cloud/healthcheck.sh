#!/bin/sh

# argv

# functions

#docker health
#0：成功;
#1：失败;
#2：保留值，不要使用
function _main
{
  health_status_code=$(curl -s -o /dev/null -w "%{http_code}" -X GET "http://127.0.0.1:$APP_PORT/actuator/health")
  echo $health_status_code

  if [[ $REQUEST_API_DOCS -eq 1 ]]; then
    api_status_code=$(curl -s -o /dev/null -w "%{http_code}" -X GET "http://127.0.0.1:$APP_PORT/v3/api-docs")
    echo $api_status_code

  	[[ $health_status_code -eq 200 && $api_status_code -eq 200 ]] && exit 0
  else
    [[ $health_status_code -eq 200 ]] && exit 0
  fi

   exit 1
}

# main
_main "$@"
