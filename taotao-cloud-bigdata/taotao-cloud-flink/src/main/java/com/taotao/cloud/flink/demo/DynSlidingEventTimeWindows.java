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

package com.taotao.cloud.flink.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner.WindowAssignerContext;
import org.apache.flink.streaming.api.windowing.triggers.EventTimeTrigger;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class DynSlidingEventTimeWindows extends WindowAssigner<Object, TimeWindow> {
    private static final long serialVersionUID = 1L;

    private final long size;

    private final long slide;

    private final long offset;

    // 从原始数据中获取窗口长度
    private final TimeAdjustExtractor sizeTimeAdjustExtractor;
    // 从原始数据中获取窗口步长
    private final TimeAdjustExtractor slideTimeAdjustExtractor;

    protected DynSlidingEventTimeWindows(long size, long slide, long offset) {
        if (Math.abs(offset) >= slide || size <= 0) {
            throw new IllegalArgumentException(
                    "SlidingEventTimeWindows parameters must satisfy "
                            + "abs(offset) < slide and size > 0");
        }

        this.size = size;
        this.slide = slide;
        this.offset = offset;
        this.sizeTimeAdjustExtractor = (elem) -> 0;
        this.slideTimeAdjustExtractor = (elem) -> 0;
    }

    protected DynSlidingEventTimeWindows(
            long size,
            long slide,
            long offset,
            TimeAdjustExtractor sizeTimeAdjustExtractor,
            TimeAdjustExtractor slideTimeAdjustExtractor) {
        if (Math.abs(offset) >= slide || size <= 0) {
            throw new IllegalArgumentException(
                    "SlidingEventTimeWindows parameters must satisfy "
                            + "abs(offset) < slide and size > 0");
        }

        this.size = size;
        this.slide = slide;
        this.offset = offset;
        this.sizeTimeAdjustExtractor = sizeTimeAdjustExtractor;
        this.slideTimeAdjustExtractor = slideTimeAdjustExtractor;
    }

    // 每次分配窗口的时候，都从数据里面抽取窗口与步长，如果存在就将新定义的长度以及步长作为新的长度与步长，这样就实现了动态调整
    @Override
    public Collection<TimeWindow> assignWindows(
            Object element, long timestamp, WindowAssignerContext context) {
        long realSize = this.sizeTimeAdjustExtractor.extract(element);
        long realSlide = this.slideTimeAdjustExtractor.extract(element);
        if (timestamp > Long.MIN_VALUE) {
            List<TimeWindow> windows =
                    new ArrayList<>(
                            (int)
                                    ((realSize == 0 ? size : realSize)
                                            / (realSlide == 0 ? slide : realSlide)));
            long lastStart =
                    TimeWindow.getWindowStartWithOffset(
                            timestamp, offset, (realSlide == 0 ? slide : realSlide));
            for (long start = lastStart;
                    start > timestamp - (realSize == 0 ? size : realSize);
                    start -= (realSlide == 0 ? slide : realSlide)) {
                windows.add(new TimeWindow(start, start + (realSize == 0 ? size : realSize)));
            }
            return windows;
        } else {
            throw new RuntimeException(
                    "Record has Long.MIN_VALUE timestamp (= no timestamp marker). "
                            + "Is the time characteristic set to 'ProcessingTime', or did you forget to call "
                            + "'DataStream.assignTimestampsAndWatermarks(...)'?");
        }
    }

    public long getSize() {
        return size;
    }

    public long getSlide() {
        return slide;
    }

    @Override
    public Trigger<Object, TimeWindow> getDefaultTrigger() {
        return EventTimeTrigger.create();
    }

    @Override
    public String toString() {
        return "SlidingEventTimeWindows(" + size + ", " + slide + ")";
    }

    /**     * Creates a new {@code SlidingEventTimeWindows} {@link WindowAssigner} that assigns elements to     * sliding time windows based on the element timestamp.     *     * @param size The size of the generated windows.     * @param slide The slide interval of the generated windows.     * @return The time policy.     */
    public static DynSlidingEventTimeWindows of(Time size, Time slide) {
        return new DynSlidingEventTimeWindows(size.toMilliseconds(), slide.toMilliseconds(), 0);
    }

    /**     * Creates a new {@code SlidingEventTimeWindows} {@link WindowAssigner} that assigns elements to     * time windows based on the element timestamp and offset.     *     * <p>For example, if you want window a stream by hour,but window begins at the 15th minutes of     * each hour, you can use {@code of(Time.hours(1),Time.minutes(15))},then you will get time     * windows start at 0:15:00,1:15:00,2:15:00,etc.     *     * <p>Rather than that,if you are living in somewhere which is not using UTC±00:00 time, such as     * China which is using UTC+08:00,and you want a time window with size of one day, and window     * begins at every 00:00:00 of local time,you may use {@code of(Time.days(1),Time.hours(-8))}.     * The parameter of offset is {@code Time.hours(-8))} since UTC+08:00 is 8 hours earlier than     * UTC time.     *     * @param size The size of the generated windows.     * @param slide The slide interval of the generated windows.     * @param offset The offset which window start would be shifted by.     * @return The time policy.     */
    public static DynSlidingEventTimeWindows of(Time size, Time slide, Time offset) {
        return new DynSlidingEventTimeWindows(
                size.toMilliseconds(), slide.toMilliseconds(), offset.toMilliseconds());
    }

    public static DynSlidingEventTimeWindows of(
            Time size,
            Time slide,
            Time offset,
            TimeAdjustExtractor sizeTimeAdjustExtractor,
            TimeAdjustExtractor slideTimeAdjustExtractor) {
        return new DynSlidingEventTimeWindows(
                size.toMilliseconds(),
                slide.toMilliseconds(),
                offset.toMilliseconds(),
                sizeTimeAdjustExtractor,
                slideTimeAdjustExtractor);
    }

    @Override
    public TypeSerializer<TimeWindow> getWindowSerializer(ExecutionConfig executionConfig) {
        return new TimeWindow.Serializer();
    }

    @Override
    public boolean isEventTime() {
        return true;
    }

    public interface TimeAdjustExtractor {
        long extract(Object element);
    }
}
