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

package com.taotao.cloud.netty.itcast.nio.c5;

import static com.taotao.cloud.netty.itcast.nio.c2.ByteBufferUtil.debugAll;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.taotao.cloud.netty.itcast.nio.c2.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * AioFileChannel
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class AioFileChannel {

    public static void main( String[] args ) throws IOException {
        try (AsynchronousFileChannel channel =
                AsynchronousFileChannel.open(Paths.get("data.txt"), StandardOpenOption.READ)) {
            // 参数1 ByteBuffer
            // 参数2 读取的起始位置
            // 参数3 附件
            // 参数4 回调对象 CompletionHandler
            ByteBuffer buffer = ByteBuffer.allocate(16);
            log.debug("read begin...");
            channel.read(
                    buffer,
                    0,
                    buffer,
                    new CompletionHandler<Integer, ByteBuffer>() {
                        @Override // read 成功
                        public void completed( Integer result, ByteBuffer attachment ) {
                            log.debug("read completed...{}", result);
                            attachment.flip();
                            debugAll(attachment);
                        }

                        @Override // read 失败
                        public void failed( Throwable exc, ByteBuffer attachment ) {
                            exc.printStackTrace();
                        }
                    });
            log.debug("read end...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.in.read();
    }
}
