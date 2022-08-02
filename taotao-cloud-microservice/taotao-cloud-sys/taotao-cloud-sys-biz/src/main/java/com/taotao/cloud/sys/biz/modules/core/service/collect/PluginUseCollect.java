package com.taotao.cloud.sys.biz.modules.core.service.collect;

import com.alibaba.fastjson.JSON;
import com.taotao.cloud.sys.biz.modules.core.service.plugin.PluginManager;
import com.taotao.cloud.sys.biz.modules.core.utils.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

/**
 * 插件使用情况数据采集
 */
@Slf4j
@Component
public class PluginUseCollect implements DataCollect , InitializingBean {
	@Autowired
	private PluginManager pluginManager;

	@Value("${collect.ip}")
	private String ip;
	@Value("${collect.port}")
	private int port;

	private Socket socket;
	private BufferedWriter bufferedWriter;

	@Override
	public void collect() {
		if (true) {
            return ;
        }
		final Map<String, PluginManager.EnhancePlugin> pluginRegisterMap = pluginManager.getPluginRegisterMap();
		final String userIp = NetUtil.getLocalIPs().get(0);
		final String sendData = JSON.toJSONString(pluginRegisterMap.values());
		// 数据上报, 通过 socket
		final UserDataCollectClient.CollectData collectData = new UserDataCollectClient.CollectData(userIp, PluginUseCollect.class.getName(), sendData);

		try {
			bufferedWriter.write(JSON.toJSONString(collectData));
			bufferedWriter.write('\n');
		} catch (IOException e) {
//			log.error("数据推送流异常:{}", e.getMessage());

			this.reconnect();
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.reconnect();
	}

	public void reconnect(){
		if (bufferedWriter != null){
			try {
				bufferedWriter.close();
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}
		if (socket != null){
			try{
				socket.close();
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}

		try {
//			log.info("开始连接数据收集地址: {}:{}",ip,port);
			socket = new Socket(ip, port);

			final OutputStream outputStream = socket.getOutputStream();
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
		} catch (IOException e) {
//			log.error("连接数据推送地址失败: {}", e.getMessage());
		}
	}
}
