package com.taotao.cloud.message.biz.austin.web.vo.amis;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shuigedeng
 * 图表的Vo
 * https://aisuda.bce.baidu.com/amis/zh-CN/components/chart
 * https://www.runoob.com/echarts/echarts-setup.html
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class EchartsVo {
    /**
     * title 标题
     */
    @JsonProperty
    private TitleVO title;
    /**
     * tooltip 提示
     */
    @JsonProperty
    private TooltipVO tooltip;
    /**
     * legend 图例
     */
    @JsonProperty
    private LegendVO legend;
    /**
     * xAxis x轴
     */
    @JsonProperty
    private XaxisVO xAxis;
    /**
     * yAxis y轴
     */
    @JsonProperty
    private YaxisVO yAxis;
    /**
     * series 系列列表
     * <p>
     * 每个系列通过 type 决定自己的图表类型
     */
    @JsonProperty
    private List<SeriesVO> series;

    /**
     * TitleVO
     */
    @Data
    @Accessors(chain=true)
    public static class TitleVO {
        /**
         * text
         */
        private String text;
    }

    /**
     * TooltipVO
     */
    @Data
    @Accessors(chain=true)
    public static class TooltipVO {
        private String color;
    }

    /**
     * LegendVO
     */
    @Data
    @Accessors(chain=true)
    public static class LegendVO {
        /**
         * data
         */
        private List<String> data;
    }

    /**
     * XAxisVO
     */
    @Data
    @Accessors(chain=true)

    public static class XaxisVO {
        /**
         * data
         */
        private List<String> data;
    }

    /**
     * YAxisVO
     */
    @Data
    @Accessors(chain=true)
    public static class YaxisVO {
        private String type;
    }

    /**
     * SeriesVO
     */
    @Data
    @Accessors(chain=true)
    public static class SeriesVO {
        /**
         * name
         */
        private String name;
        /**
         * type
         */
        private String type;
        /**
         * data
         */
        private List<Integer> data;
    }
}
