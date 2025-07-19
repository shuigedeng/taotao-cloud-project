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

package com.taotao.cloud.design.patterns.responsibilityChain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChannelPipeline默认实现
 *
 */
public class DefaultChannelPipeline implements ChannelPipeline {

    AbstractChannelHandlerContext head;
    AbstractChannelHandlerContext tail;

    private static final String HEAD_NAME = generateName0(HeadContext.class);
    private static final String TAIL_NAME = generateName0(TailContext.class);

    private Channel channel;

    protected DefaultChannelPipeline(Channel channel) {
        this.channel = channel;
        tail = new TailContext(this);
        head = new HeadContext(this);
        head.next = tail;
        tail.prev = head;
    }

    public ChannelPipeline addLast(String name, ChannelHandler handler) {
        AbstractChannelHandlerContext newCtx =
                new DefaultChannelHandlerContext(this, name, handler);
        AbstractChannelHandlerContext prev = tail.prev;
        newCtx.prev = prev;
        newCtx.next = tail;
        prev.next = newCtx;
        tail.prev = newCtx;
        return this;
    }

    public Channel channel() {
        return channel;
    }

    @Override
    public ChannelPipeline fireExceptionCaught(Throwable cause, Object in, Object out) {
        AbstractChannelHandlerContext.invokeExceptionCaught(head, cause, in, out);
        return this;
    }

    @Override
    public ChannelPipeline fireChannelProcess(Object in, Object out) {
        AbstractChannelHandlerContext.invokeChannelProcess(head, in, out);
        return this;
    }

    private static String generateName0(Class<?> handlerType) {
        return handlerType.getSimpleName() + "#0";
    }

    static final class TailContext extends AbstractChannelHandlerContext implements ChannelHandler {

        private Logger logger = LoggerFactory.getLogger(TailContext.class);

        TailContext(DefaultChannelPipeline pipeline) {
            super(pipeline, TAIL_NAME, TailContext.class);
        }

        @Override
        public ChannelHandler handler() {
            return this;
        }

        @Override
        public void channelProcess(ChannelHandlerContext ctx, Object in, Object out)
                throws Exception {
            if (logger.isDebugEnabled()) {
                logger.debug("tail:channelProcess:there is no more handler");
            }
        }

        @Override
        public void exceptionCaught(
                ChannelHandlerContext ctx, Throwable cause, Object in, Object out)
                throws Exception {
            if (logger.isDebugEnabled()) {
                logger.debug("tail:exceptionCaught:there is no more handler");
            }
        }
    }

    static final class HeadContext extends AbstractChannelHandlerContext implements ChannelHandler {

        private Logger logger = LoggerFactory.getLogger(TailContext.class);

        HeadContext(DefaultChannelPipeline pipeline) {
            super(pipeline, HEAD_NAME, HeadContext.class);
        }

        @Override
        public ChannelHandler handler() {
            return this;
        }

        @Override
        public void channelProcess(ChannelHandlerContext ctx, Object in, Object out)
                throws Exception {
            if (logger.isDebugEnabled()) {
                logger.debug("head:channelProcess");
            }
            ctx.fireChannelProcess(in, out);
        }

        @Override
        public void exceptionCaught(
                ChannelHandlerContext ctx, Throwable cause, Object in, Object out)
                throws Exception {
            logger.info("head:exceptionCaught");
        }
    }

    @Override
    public ChannelPipeline process(Object in, Object out) {
        head.process(in, out);
        return this;
    }

    @Override
    public ChannelHandlerContext head() {
        return head;
    }

    @Override
    public ChannelHandlerContext tail() {
        return tail;
    }
}
