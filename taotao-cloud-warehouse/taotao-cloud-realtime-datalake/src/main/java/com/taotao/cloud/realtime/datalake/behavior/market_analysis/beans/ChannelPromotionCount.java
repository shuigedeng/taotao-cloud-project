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

package com.taotao.cloud.realtime.datalake.behavior.market_analysis.beans;

/**
 * ChannelPromotionCount
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class ChannelPromotionCount {

    private String channel;
    private String behavior;
    private String windowEnd;
    private Long count;

    public ChannelPromotionCount() {
    }

    public ChannelPromotionCount( String channel, String behavior, String windowEnd, Long count ) {
        this.channel = channel;
        this.behavior = behavior;
        this.windowEnd = windowEnd;
        this.count = count;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel( String channel ) {
        this.channel = channel;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior( String behavior ) {
        this.behavior = behavior;
    }

    public String getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd( String windowEnd ) {
        this.windowEnd = windowEnd;
    }

    public Long getCount() {
        return count;
    }

    public void setCount( Long count ) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ChannelPromotionCount{"
                + "channel='"
                + channel
                + '\''
                + ", behavior='"
                + behavior
                + '\''
                + ", windowEnd='"
                + windowEnd
                + '\''
                + ", count="
                + count
                + '}';
    }
}
