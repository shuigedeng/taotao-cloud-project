package com.taotao.cloud.design.patterns.responsibilityChain;

/**
 * 通道
 *
 */
public interface Channel {

    Channel process(Object in,
                    Object out);

    ChannelPipeline pipeline();

    interface ChannelProcessor {
        void doProcess(Object in,
                       Object out);
    }
}
