/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.job.common.utils.net;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * socket 连通性助手
 *
 * @author shuigedeng
 * @since 2024/2/8
 */
@Slf4j
public class PingPongUtils {

    static final String PING = "ping";
    static final String PONG = "pong";

    /**
     * 验证目标 IP 和 端口的连通性
     * @param targetIp 目标 IP
     * @param targetPort 目标端口
     * @return true or false
     */
    public static boolean checkConnectivity(String targetIp, int targetPort) {

        try (Socket s = new Socket(targetIp, targetPort);
                InputStream is = s.getInputStream();
                OutputStream os = s.getOutputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            // 发送 PING 请求
            os.write(PING.getBytes(StandardCharsets.UTF_8));
            os.flush();

            // 读取服务器返回的消息
            String content = br.readLine();

            if (PONG.equalsIgnoreCase(content)) {
                return true;
            }
        } catch (UnknownHostException e) {
            log.warn("[SocketConnectivityUtils] unknown host: {}:{}", targetIp, targetPort);
        } catch (IOException e) {
            log.warn(
                    "[SocketConnectivityUtils] IOException: {}:{}, msg: {}",
                    targetIp,
                    targetPort,
                    ExceptionUtils.getMessage(e));
        } catch (Exception e) {
            log.error(
                    "[SocketConnectivityUtils] unknown TtcJobException for check ip: {}:{}",
                    targetIp,
                    targetPort,
                    e);
        }

        return false;
    }
}
