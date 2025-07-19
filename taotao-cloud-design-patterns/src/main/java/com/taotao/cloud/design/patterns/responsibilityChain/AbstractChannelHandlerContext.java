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

/**
 * 抽象ChannelHandlerContext
 *
 */
public abstract class AbstractChannelHandlerContext implements ChannelHandlerContext {
    volatile AbstractChannelHandlerContext next;
    volatile AbstractChannelHandlerContext prev;
    private DefaultChannelPipeline pipeline;
    private String name;

    AbstractChannelHandlerContext(
            DefaultChannelPipeline pipeline,
            String name,
            Class<? extends ChannelHandler> handlerClass) {
        this.name = (String) ObjectUtil.checkNotNull(name, "name");
        this.pipeline = pipeline;
    }

    @Override
    public Channel channel() {
        return pipeline.channel();
    }

    @Override
    public ChannelPipeline pipeline() {
        return pipeline;
    }

    @Override
    public ChannelHandlerContext fireExceptionCaught(Throwable cause, Object in, Object out) {
        invokeExceptionCaught(this.next, cause, in, out);
        return this;
    }

    @Override
    public ChannelHandlerContext fireChannelProcess(Object in, Object out) {
        invokeChannelProcess(this.next, in, out);
        return this;
    }

    private void invokeExceptionCaught(final Throwable cause, Object in, Object out) {
        try {
            handler().exceptionCaught(this, cause, in, out);
        } catch (Throwable error) {

        }
    }

    private void invokeChannelProcess(Object in, Object out) {
        try {
            handler().channelProcess(this, in, out);
        } catch (Throwable throwable) {
            invokeExceptionCaught(throwable, in, out);
        }
    }

    static void invokeExceptionCaught(
            final AbstractChannelHandlerContext next,
            final Throwable cause,
            Object in,
            Object out) {
        next.invokeExceptionCaught(cause, in, out);
    }

    static void invokeChannelProcess(
            final AbstractChannelHandlerContext next, Object in, Object out) {
        next.invokeChannelProcess(in, out);
    }

    @Override
    public ChannelHandlerContext process(Object in, Object out) {

        try {
            handler().channelProcess(this, in, out);
        } catch (Throwable t) {
            invokeExceptionCaught(t, in, out);
        }
        return this;
    }
}
