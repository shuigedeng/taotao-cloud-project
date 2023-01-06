FROM oraclelinux:7-slim

MAINTAINER shuigedeng

# Add Spring Boot Native app spring-boot-graal to Container
COPY ./build/native/nativeCompile/demo spring-native-demo

ENV PORT=9813

# Fire up our Spring Boot Native app by default
# CMD [ "sh", "-c", "./spring-native-demo -Dserver.port=$PORT" ]
CMD [ "sh", "-c", "./spring-native-demo" ]

