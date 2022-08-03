package com.taotao.cloud.sys.biz.api.controller.tools.kafka.service;
//
//import com.alibaba.fastjson.JSON;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.time.DateFormatUtils;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.TopicPartition;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import javax.websocket.Session;
//import java.io.IOException;
//import java.time.Duration;
//import java.util.*;
//import java.util.concurrent.*;
//
//@Service
//@Slf4j
//public class KafkaWebsocketService {
//    @Autowired
//    private MessageDispatch messageDispatch;
//    @Autowired
//    private KafkaService kafkaService;
//    @Autowired
//    private ClassloaderService classloaderService;
//    @Autowired
//    private SerializerChoseService serializerChoseService;
//
//    @PostConstruct
//    public void init(){
//        KafkaMessageListener kafkaMessageListener = new KafkaMessageListener();
//        messageDispatch.register(kafkaMessageListener);
//    }
//
//    private static Map<String,ConsumerTask> consumerTaskMap = new ConcurrentHashMap<>();
//
//    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 5, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10), new ThreadFactory() {
//        @Override
//        public Thread newThread(Runnable r) {
//            Thread thread = new Thread(r);
//            // 设置线程内的类加载器
//            thread.setContextClassLoader(KafkaService.class.getClassLoader());
//            return thread;
//        }
//    });
//
//    class KafkaMessageListener implements WebsocketMessageListener{
//        @Override
//        public void listen(ClientMessage clientMessage, WebSocketClient webSocketClient) {
//            KafkaConsumerPayload kafkaConsumerPayload = (KafkaConsumerPayload) clientMessage.getPayload();
//            // 检测是否有已经在消费的主题任务,有则加入,否则启动新的主题任务
//            String topic = kafkaConsumerPayload.getTopic();
//            ConsumerTask consumerTask = consumerTaskMap.get(topic);
//            if(consumerTask == null){
//                consumerTask = new ConsumerTask(kafkaConsumerPayload);
//                threadPoolExecutor.submit(consumerTask);
//                consumerTaskMap.put(topic,consumerTask);
//            }
//            consumerTask.addListener(webSocketClient);
//        }
//
//        @Override
//        public void exceptionCaught(Exception e, ClientMessage clientMessage) {
//            log.error("处理 kafka 消息时异常:{}",e.getMessage(),e);
//        }
//
//        @Override
//        public boolean support(String routingKey) {
//            return "kafka".equals(routingKey);
//        }
//    }
//
//    class ConsumerTask implements Runnable{
//        private KafkaConsumerPayload kafkaConsumerPayload;
//        private long lastNoClientTime;
//
//        Serializer serializer;
//        Serializer defaultSerializer;
//        ClassLoader classloader;
//
//        public ConsumerTask(KafkaConsumerPayload kafkaConsumerPayload) {
//            this.kafkaConsumerPayload = kafkaConsumerPayload;
//            serializer = serializerChoseService.choseSerializer(kafkaConsumerPayload.getSerializable());
//            classloader = classloaderService.getClassloader(kafkaConsumerPayload.getClassloader());
//            if ( classloader == null ){
//                classloader = ClassLoader.getSystemClassLoader();
//            }
//            defaultSerializer = serializerChoseService.choseSerializer("string");
//        }
//
//        private List<WebSocketClient> notifyConsumerRecordClients = new ArrayList<>();
//
//        @Override
//        public void run() {
//            String clusterName = kafkaConsumerPayload.getClusterName();
//            String topic = kafkaConsumerPayload.getTopic();
//            try {
//                int partitions = kafkaService.partitions(clusterName, topic);
//
//                KafkaConsumer<byte[], byte[]> consumer = kafkaService.loadConsumerClient(clusterName);
//                try {
//                    List<TopicPartition> topicPartitions = new ArrayList<>();
//                    for (int i=0;i < partitions;i++) {
//                        TopicPartition topicPartition = new TopicPartition(topic, i);
//                        topicPartitions.add(topicPartition);
//                    }
//
//                    consumer.assign(topicPartitions);
//
//                    Map<TopicPartition, Long> endOffsets = consumer.endOffsets(topicPartitions);
//                    Iterator<Map.Entry<TopicPartition, Long>> iterator = endOffsets.entrySet().iterator();
//                    while (iterator.hasNext()){
//                        Map.Entry<TopicPartition, Long> next = iterator.next();
//                        TopicPartition key = next.getKey();
//                        Long value = next.getValue();
//                        consumer.seek(key,value);
//                    }
//                    boolean openConsumer = lastNoClientTime == 0 || (System.currentTimeMillis() - lastNoClientTime < 300000);
//                    if(!openConsumer){
//                        log.info("关闭消费[{}],没有客户端已经超过 5 分钟 ",topic);
//
//                        // 通知客户端,已经没有在监听数据了
//
//                    }
//                    while (openConsumer) {      // 当没有客户端并超过 5 分钟时关闭消费
//                        ConsumerRecords<byte[], byte[]> consumerRecords = consumer.poll(Duration.ofMillis(10));       // 100ms 内抓取的数据，不是抓取的数据量
//                        Iterable<ConsumerRecord<byte[], byte[]>> records = consumerRecords.records(topic);
//                        Iterator<ConsumerRecord<byte[], byte[]>> consumerRecordIterator = records.iterator();
//                        while (consumerRecordIterator.hasNext()){
//                            ConsumerRecord<byte[], byte[]> consumerRecord = consumerRecordIterator.next();
//                            byte[] value = consumerRecord.value();
//                            Object deserialize = null;
//                            try {
//                                deserialize = serializer.deserialize(value, classloader);
//                            } catch (ClassNotFoundException e) {
//                                log.info("数据反序列化失败,将使用 string 格式数据发送 ");
//                                try {
//                                    deserialize = defaultSerializer.deserialize(value,classloader);
//                                } catch (ClassNotFoundException ex) {
//                                   log.error("ConsumerTask deserialize error : {}",e.getMessage(),e);
//                                }
//                            }
//                            for (WebSocketClient notifyConsumerRecordClient : notifyConsumerRecordClients) {
//                                // send message to client
//                                Session clientSession = notifyConsumerRecordClient.getSession();
//                                Date date = new Date(consumerRecord.timestamp());
//                                String format = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(date);
//                                ExtendConsumerRecord extendConsumerRecord = new ExtendConsumerRecord(deserialize, consumerRecord.partition(), consumerRecord.offset(), format);
//
//                                clientSession.getAsyncRemote().sendText(JSON.toJSONString(extendConsumerRecord));
//                            }
//                        }
//                    }
//                }finally {
//                    if(consumer != null) {
//                        consumer.close();
//                    }
//                }
//            } catch (IOException | InterruptedException | ExecutionException e) {
//                log.error("未能加载 kafka 客户端 [{}]",e.getMessage(),e);
//            }
//
//        }
//
//        /**
//         * 检查是否需要关闭消费端
//         */
//        public void checkCloseConsumer(){
//            if(notifyConsumerRecordClients.isEmpty()){
//                if (lastNoClientTime == 0) {
//                    lastNoClientTime = System.currentTimeMillis();
//                }
//            }
//        }
//
//        /**
//         * 添加消费监听客户端
//         * @param notifyConsumerRecordClient
//         */
//        public void addListener(WebSocketClient webSocketClient) {
//            notifyConsumerRecordClients.add(webSocketClient);
//            this.lastNoClientTime = 0 ;
//        }
//
//        /**
//         * 清除监听者
//         * @param session
//         */
//        public void removeListener(Session session){
//            Iterator<WebSocketClient> iterator = notifyConsumerRecordClients.iterator();
//            while (iterator.hasNext()){
//                WebSocketClient next = iterator.next();
//                Session clientSession = next.getSession();
//                if(clientSession.getId().equals(session.getId())){
//                    iterator.remove();break;
//                }
//            }
//            // 检查是否需要停止消费
//            checkCloseConsumer();
//        }
//    }
//}
