wget https://github.com/alibaba/arthas/releases/download/arthas-all-3.5.2/arthas-tunnel-server-3.5.2-fatjar.jar


nohup java -jar arthas-tunnel-server-3.4.5-fatjar.jar > /dev/null 2>&1 &

http://127.0.0.1:8080/actuator/arthas 登陆用户名是arthas
密码在arthas tunnel server的日志里可以找到，比如：

 <dependency>
        <groupId>com.taobao.arthas</groupId>
        <artifactId>arthas-spring-boot-starter</artifactId>
        <version>3.4.4</version>
    </dependency>


arthas:
  agent-id: URJZ5L48RPBR2ALI5K4V  #需手工指定agent-id
  tunnel-server: ws://127.0.0.1:7777/ws
