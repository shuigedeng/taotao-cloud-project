package com.taotao.cloud.sys.biz.api.controller.tools.core.service.collect;
//
//
//import com.alibaba.fastjson.JSON;
//import org.apache.commons.io.FileUtils;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.nio.charset.StandardCharsets;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//public class DataCollectServer extends Thread{
//    private ServerSocket serverSocket ;
//
//    private File base = new File("/data/collect");
//
//    @Override
//    public void run() {
//        try {
//            serverSocket = new ServerSocket(8085);
//            System.out.println("等待数据接收,端口 8085 , 数据写入 /data/collect");
//            while (true) {
//                final Socket accept = serverSocket.accept();
//                new Thread(new DataRead(accept)).start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public final class DataRead implements Runnable{
//        private Socket socket;
//        private BufferedReader bufferedReader;
//
//        public DataRead(Socket socket) throws IOException {
//            this.socket = socket;
//
//            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        }
//
//
//        @Override
//        public void run() {
//            while (true){
//                String line = null;
//                try {
//                    while ((line = bufferedReader.readLine()) != null) {
//                        final UserDataCollectClient.CollectData collectData = JSON.parseObject(line, UserDataCollectClient.CollectData.class);
//                        final String userIp = collectData.getUserIp();
//                        final String module = collectData.getModule();
//                        final File file = new File(base, userIp + "/" + module);
//                        file.mkdirs();
//                        FileUtils.writeStringToFile(new File(file,"data"),collectData.getData(), StandardCharsets.UTF_8);
//                    }
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        final DataCollectServer dataCollectServer = new DataCollectServer();
//        dataCollectServer.setDaemon(true);
//        dataCollectServer.start();
//        dataCollectServer.join();
//    }
//}
