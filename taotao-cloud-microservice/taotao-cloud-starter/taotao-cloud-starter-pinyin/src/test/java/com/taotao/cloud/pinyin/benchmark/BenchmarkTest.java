package com.taotao.cloud.pinyin.benchmark;//package com.taotao.cloud.core.pinyin.benchmark;
//
//import com.dtp.common.util.StreamUtil;
//import com.taotao.cloud.core.pinyin.bs.PinyinBs;
//import com.taotao.cloud.core.pinyin.support.segment.PinyinSegments;
//import net.sourceforge.pinyin4j.PinyinHelper;
//import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
//import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
//import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.ThreadLocalRandom;
//
//@Ignore
//public class BenchmarkTest {
//
//    /**
//     * 1w 次
//     */
//    private static final int TIMES = 10000;
//
//    /**
//     * 单个字符次数统计
//     */
//    private static final int SINGLE_TIMES = 1000000;
//
//    /**
//     * pinyin4j: Pinyin4j cost: 26324
//     *
//     * Pinyin4j 有中文分词，不过拼音的格式效果不是很好。
//     *
//     * 重庆火锅：chong qing huoguo
//     */
//    @Test
//    public void pinyin4jTest() throws BadHanyuPinyinOutputFormatCombination {
//        // 创建汉语拼音处理类
//        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//        // 输出设置，大小写，音标方式
//        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        // 看的出来，没有支持分词。
//        // chong qing huoguo
//        String result = PinyinHelper.toHanYuPinyinString("重庆火锅", defaultFormat, " ", false);
//        System.out.println(result);
//
//        // 验证
//        final String text = getText();
//        long startTime = System.currentTimeMillis();
//        for(int i = 0; i < TIMES; i++) {
//            PinyinHelper.toHanYuPinyinString(text, defaultFormat, " ", false);
//        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("Pinyin4j cost: " + (endTime-startTime));
//    }
//
//    /**
//     * 预热耗时：565 ms
//     * Pinyin cost: 16260
//     *
//     * 重庆火锅：chóng qìng huǒ guō
//     * 本框架，支持分词的测试。
//     */
//    @Test
//    public void pinyinWithSegmentTest() {
//        // 预热
//        String result = PinyinHelper.toPinyin("重庆火锅");
//        System.out.println(result);
//
//        // 验证
//        final String text = getText();
//        long startTime = System.currentTimeMillis();
//        for(int i = 0; i < TIMES; i++) {
//            PinyinHelper.toPinyin(text);
//        }
//        long endTime = System.currentTimeMillis();
//
//        System.out.println("Pinyin cost: " + (endTime-startTime));
//    }
//
//    /**
//     * chóng qìng huǒ guō
//     * Pinyin cost: 14804
//     *
//     * 重庆火锅：chóng qìng huǒ guō
//     *
//     * 使用指定的 chars 分词测试
//     *
//     */
//    @Test
//    public void pinyinWithCharSegmentTest() {
//        // 预热
//        String result =PinyinHelper.toPinyin("重庆火锅");
//        System.out.println(result);
//
//        // 验证
//        final String text = getText();
//        long startTime = System.currentTimeMillis();
//        PinyinBs pinyinBs = PinyinBs.newInstance().segment(PinyinSegments.chars());
//        for(int i = 0; i < TIMES; i++) {
//            pinyinBs.toPinyin(text);
//        }
//        long endTime = System.currentTimeMillis();
//
//        System.out.println("Pinyin cost: " + (endTime-startTime));
//    }
//
//    /**
//     * 获取文本内容
//     * @return 内容
//     */
//    private String getText() {
//        return StreamUtil.getFileContent("back/永远的夏娃.txt");
//    }
//
//    /**
//     * 单个字符耗时统计
//     * [zhong4, chong2]
//     *
//     * Pinyin4j cost: 650
//     */
//    @Test
//    public void pinyin4jCharTest() {
//        String[] result = PinyinHelper.toHanyuPinyinStringArray('重');
//        System.out.println(Arrays.toString(result));
//
//        // 验证
//        final String text = getText();
//
//        long startTime = System.currentTimeMillis();
//        // 随机选择一个单字
//        for(int i = 0; i < SINGLE_TIMES; i++) {
//            int index = ThreadLocalRandom.current().nextInt(text.length()-1);
//
//            PinyinHelper.toHanyuPinyinStringArray(text.charAt(index));
//        }
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("Pinyin4j cost: " + (endTime-startTime));
//    }
//
//    /**
//     * [zhòng, chóng, tóng]
//     *
//     * Pinyin cost: 410
//     */
//    @Test
//    public void pinyinCharTest() {
//        List<String> result = PinyinHelper.toPinyinList('重');
//        System.out.println(result);
//
//        // 验证
//        final String text = getText();
//
//        long startTime = System.currentTimeMillis();
//        // 随机选择一个单字
//        for(int i = 0; i < SINGLE_TIMES; i++) {
//            int index = ThreadLocalRandom.current().nextInt(text.length()-1);
//
//           PinyinHelper.toPinyinList(text.charAt(index));
//        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("Pinyin cost: " + (endTime-startTime));
//    }
//
//}
