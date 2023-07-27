cd /software

git clone https://github.com/ctripcorp/apollo.git

mysql -uroot -p123456789 < apollo/scripts/sql/apolloportaldb.sql

mysql -uroot -p123456789 < apollo/scripts/sql/apolloconfigdb.sql


docker run -d \
  --name apollo-configservice \
  --net=host \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://192.168.10.220:3306/ApolloConfigDB?characterEncoding=utf8" \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=123456 \
  apolloconfig/apollo-configservice

  docker run -d \
  --name apollo-adminservice \
  --net=host \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://192.168.10.220:3306/ApolloConfigDB?characterEncoding=utf8" \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=123456 \
  apolloconfig/apollo-adminservice

  docker run -d \
  --name apollo-portal \
  --net=host \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://192.168.10.220:3306/ApolloPortalDB?characterEncoding=utf8" \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=123456 \
  -e APOLLO_PORTAL_ENVS=dev \
  -e DEV_META=http://192.168.10.220:8080 \
  apolloconfig/apollo-portal


浏览器输入：

Port: http://192.168.31.115:8070/，账号/密码：apollo/admin

Eureka：http://192.168.31.115:8080/

Adminservice：http://192.168.31.115:8090/
