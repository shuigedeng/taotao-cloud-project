// package com.taotao.cloud.common.generator.util;
//
//
// import org.junit.Test;
//
// import static com.taotao.cloud.common.support.generator.util.ChineseCharUtils.genFixedLengthChineseChars;
// import static com.taotao.cloud.common.support.generator.util.ChineseCharUtils.genOneChineseChars;
// import static com.taotao.cloud.common.support.generator.util.ChineseCharUtils.genRandomLengthChineseChars;
// import static com.taotao.cloud.common.support.generator.util.ChineseCharUtils.getOneOddChar;
//
// @Test
// public class ChineseCharUtilsTest {
//     public void testGenOneChineseChars() {
//         final String result = genOneChineseChars();
//         System.out.println(result);
//         assertThat(result).hasSize(1);
//     }
//
//     public void testGenFixedLengthChineseChars() {
//         final String result = genFixedLengthChineseChars(20);
//         System.out.println(result);
//         assertThat(result).hasSize(20);
//     }
//
//     public void testGenRandomLengthChineseChars() {
//         final String result = genRandomLengthChineseChars(2, 10);
//         System.out.println(result);
//         assertThat(result).hasSizeBetween(2, 10);
//     }
//
//     public void testGetOneOddChar() {
//         final char result = getOneOddChar();
//         System.out.println(result);
//         assertThat(result).isNotNull();
//     }
// }
