#!/bin/sh

curl -fs http://127.0.0.1:$APP_PORT/actuator/health

curl -fs http://127.0.0.1:$APP_PORT/v3/api-docs
