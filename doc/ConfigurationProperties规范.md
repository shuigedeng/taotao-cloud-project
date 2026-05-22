Resource   classpath:data/sample.txt

datasize    支持KB, MB, GB等  10MB
private DataSize maxHttpRequestHeaderSize = DataSize.ofKilobytes(8);


@DurationUnit(ChronoUnit.SECONDS) 默认值为秒

private Duration idleBetweenPolls = Duration.ZERO;   Duration支持多种格式，如"PT20.345S", "20.345S", "15M", "15m", "15"等，Spring Boot会自动识别这些格式。
private Duration delay = Duration.ofSeconds(1);

@DurationUnit(ChronoUnit.SECONDS)
private Duration timeout = Duration.ofMinutes(30);

private Class<?> keyDeserializer = StringDeserializer.class;

private File dir = new File("logs");
example.config-file=/path/to/your/logs




-javaagent:D:\project\taotao-cloud-starter\taotao-boot-starter-agent\build\libs\taotao-boot-starter-agent-2026.06.jar:C:\Users\57222\soft\apache-skywalking-java-agent-9.3.0\skywalking-agent\skywalking-agent.jar
-DSW_AGENT_NAME=taotao-cloud-demo
-DSW_AGENT_AUTHENTICATION=taotao-cloud
-DSW_AGENT_TRACE_IGNORE_PATH="Redisson/PING,/actuator/**,/admin/**"
-DSW_AGENT_COLLECTOR_BACKEND_SERVICES=127.0.0.1:11800
