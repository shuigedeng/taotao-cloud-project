package com.taotao.cloud.sys.biz.tools.jvm.service;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.apache.commons.net.telnet.TelnetClient;

import com.google.common.base.Charsets;


public class ArthasClient {
    private String host;
    private int port;

    private TelnetClient telnetClient;

    private final InputStream in;

    private final BufferedWriter out;

    public ArthasClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;

        this.telnetClient = new TelnetClient();
        telnetClient.setConnectTimeout(2000);
        telnetClient.connect(host,port);

        this.in = telnetClient.getInputStream();
        this.out = new BufferedWriter(new OutputStreamWriter(telnetClient.getOutputStream(), Charsets.UTF_8));
    }


    /**
     * 发送一个命令
     * @param command
     * @return
     */
    public String sendCommand(String command) throws IOException {
        if (command.getBytes(Charsets.UTF_8).length > 999) {
            throw new RuntimeException("命令过长");
        }
        out.write(command);
        out.newLine();
        out.flush();


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] bytes = outputStream.toByteArray();
        return new String(bytes, StandardCharsets.UTF_8);
    }


}
