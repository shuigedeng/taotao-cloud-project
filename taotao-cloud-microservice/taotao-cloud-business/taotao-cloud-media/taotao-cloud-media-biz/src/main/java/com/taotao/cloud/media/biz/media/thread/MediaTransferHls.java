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

package com.taotao.cloud.media.biz.media.thread;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.media.biz.media.common.MediaConstant;
import com.taotao.cloud.media.biz.media.dto.CameraDto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/** hls切片 */
public class MediaTransferHls extends MediaTransfer {

    /** 运行状态 */
    private boolean running = false;

    private boolean enableLog = false;
    private Process process;
    private Thread inputThread;
    private Thread errThread;

    private int port = 8888;

    /** 相机 */
    private CameraDto cameraDto;

    /** cmd */
    private List<String> command = new ArrayList<>();

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * @param cameraDto
     */
    public MediaTransferHls(CameraDto cameraDto, int port) {
        this.cameraDto = cameraDto;
        this.port = port;
        buildCommand();
    }

    /**
     * String cmd = "ffmpeg -i rtsp://admin:VZCDOY@192.168.2.120:554/Streaming/Channels/102 -r 25 -g
     * 25 -c:v libx264 -c:a aac -f hls -hls_list_size 1 -hls_wrap 6 -hls_time 1 -hls_base_url
     * /ts/"+22+"/ -method put http://localhost:8888/record/"+22+"/out.m3u8";
     */
    private void buildCommand() {

        command.add(System.getProperty(MediaConstant.ffmpegPathKey));
        command.add("-i");
        command.add(cameraDto.getUrl());
        command.add("-r");
        command.add("25");
        command.add("-g");
        command.add("25");
        command.add("-c:v");
        command.add("libopenh264"); // javacv 1.5.5 无法使用libx264
        command.add("-c:a");
        command.add("aac");
        command.add("-f");
        command.add("hls");
        command.add("-hls_list_size");
        command.add("1");
        command.add("-hls_wrap");
        command.add("6");
        command.add("-hls_time");
        command.add("1");
        command.add("-hls_base_url");
        command.add("/ts/" + cameraDto.getMediaKey() + "/");
        command.add("-method");
        command.add("put");
        command.add("http://localhost:" + port + "/record/" + cameraDto.getMediaKey() + "/out.m3u8");
    }

    /** 执行 */
    public void execute() {
        String join = CollUtil.join(command, " ");
        LogUtils.info(join);

        try {
            process = new ProcessBuilder(command).start();
            running = true;
            dealStream(process);
        } catch (IOException e) {
            running = false;
            LogUtils.error(e);
        }
    }

    /** 关闭 */
    public void stop() {
        this.running = false;
        try {
            process.destroy();
            LogUtils.info("关闭媒体流-ffmpeg，{} ", cameraDto.getUrl());
        } catch (Exception e) {
            process.destroyForcibly();
        }
    }

    /**
     * 控制台输出
     *
     * @param process
     */
    private void dealStream(Process process) {
        if (process == null) {
            return;
        }
        // 处理InputStream的线程
        inputThread = new Thread() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    while (running) {
                        line = in.readLine();
                        if (line == null) {
                            break;
                        }
                        if (enableLog) {
                            LogUtils.info("output: " + line);
                        }
                    }
                } catch (IOException e) {
                    LogUtils.error(e);
                } finally {
                    try {
                        running = false;
                        in.close();
                    } catch (IOException e) {
                        LogUtils.error(e);
                    }
                }
            }
        };
        // 处理ErrorStream的线程
        errThread = new Thread() {
            @Override
            public void run() {
                BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line = null;
                try {
                    while (running) {
                        line = err.readLine();
                        if (line == null) {
                            break;
                        }
                        if (enableLog) {
                            LogUtils.info("err: " + line);
                        }
                    }
                } catch (IOException e) {
                    LogUtils.error(e);
                } finally {
                    try {
                        running = false;
                        err.close();
                    } catch (IOException e) {
                        LogUtils.error(e);
                    }
                }
            }
        };

        inputThread.start();
        errThread.start();
    }
}
