package com.taotao.cloud.idea.plugin.toolkit.domain;

import java.util.Arrays;
import java.util.List;

/**
 * RegularExample
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class RegularExample {

    private String name;
    private String regular;

    public RegularExample( String name, String regular ) {
        this.name = name;
        this.regular = regular;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular( String regular ) {
        this.regular = regular;
    }


    public static List<RegularExample> getUsuallyRegulars() {
        return Arrays.asList(
                new RegularExample("匹配邮箱", "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}"),
                new RegularExample("匹配手机号", "(13\\d|14[579]|15[^4\\D]|17[^49\\D]|18\\d)\\d{8}"),
                new RegularExample("匹配IP（IPV4）", "\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}"),
                new RegularExample("匹配中文", "[\\u4e00-\\u9fa5]+"),
                new RegularExample("匹配双字节字符（包含汉字）", "[^\\x00-\\xff]+"),
                new RegularExample("匹配时间（时:分:秒）", "([01]?\\d|2[0-3]):[0-5]?\\d:[0-5]?\\d"),
                new RegularExample("匹配身份证", "\\d{17}[0-9Xx]|\\d{15}"),
                new RegularExample("匹配日期（年-月-日）",
                        "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)"),
                new RegularExample("匹配正整数", "[1-9]\\d*"),
                new RegularExample("匹配负整数", "-[1-9]\\d*"),
                new RegularExample("空白行", "\\s"),
                new RegularExample("腾讯QQ", "[1-9]([0-9]{5,11})"),
                new RegularExample("邮政编码", "\\d{6}")
        );
    }

}
