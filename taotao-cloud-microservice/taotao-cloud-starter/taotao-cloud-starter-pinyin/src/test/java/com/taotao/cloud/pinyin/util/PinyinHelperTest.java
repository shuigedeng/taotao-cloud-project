package com.taotao.cloud.pinyin.util;

import com.taotao.cloud.pinyin.constant.enums.PinyinStyleEnum;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class PinyinHelperTest {

    /**
     * 转换中文测试
     */
    @Test
    public void toPinyinTest() {
        String pinyin = PinyinHelper.toPinyin("我爱中文");
        Assert.assertEquals("wǒ ài zhōng wén", pinyin);
    }


    /**
     * 转换英文测试
     */
    @Test
    public void toPinyinNumEnTest() {
        String pinyin = PinyinHelper.toPinyin("my name is pinyin4j");
        Assert.assertEquals("my name is pinyin4j", pinyin);
    }

    /**
     * 转换为中文测试
     */
    @Test
    public void toPinyinEmptyTest() {
        Assert.assertNull(PinyinHelper.toPinyin(null));
        Assert.assertEquals("", PinyinHelper.toPinyin(""));
    }

    /**
     * 返回多音字列表测试
     */
    @Test
    public void toPinyinListTest() {
        List<String> pinyinList = PinyinHelper.toPinyinList('重');
        Assert.assertEquals("[zhòng, chóng, tóng]", pinyinList.toString());
    }

    /**
     * 返回多音字列表测试
     */
    @Test
    public void toPinyinListNormalTest() {
        List<String> pinyinList = PinyinHelper.toPinyinList('重', PinyinStyleEnum.NORMAL);
        Assert.assertEquals("[zhong, chong, tong]", pinyinList.toString());
    }

    /**
     * 转换中文繁体测试
     */
    @Test
    public void toPinyinChineseSimpleTest() {
        String pinyin = PinyinHelper.toPinyin("奮斗");
        Assert.assertEquals("fèn dòu", pinyin);
    }

    /**
     * 中文转拼音之后如果前后不是中文，且不是空格，那么应该添加一个空格。
     * （1）句子的结尾后面不用。
     * （2）句子的开头前面不用。
     */
    @Test
    public void whiteSpaceTest() {
        final String text = "xiaomi手机";
        final String text2 = "xiaomi 手机";

        Assert.assertEquals("xiaomi shǒu jī", PinyinHelper.toPinyin(text));
        Assert.assertEquals("xiaomi shǒu jī", PinyinHelper.toPinyin(text2));
    }

    /**
     * 地名
     */
    @Test
    public void locationTest() {
        Assert.assertEquals("hé jiāng", PinyinHelper.toPinyin("合江"));
        Assert.assertEquals("bèng shān", PinyinHelper.toPinyin("蚌山"));
        Assert.assertEquals("hóng tóng", PinyinHelper.toPinyin("洪洞"));
        Assert.assertEquals("shàn xiàn", PinyinHelper.toPinyin("单县"));
        Assert.assertEquals("lào tíng", PinyinHelper.toPinyin("乐亭"));
    }

    /**
     * 同音字
     */
    @Test
    public void samePinyinMapTest() {
        final char hanzi = '汉';
        Map<String,List<String>> map = PinyinHelper.samePinyinMap(hanzi);
        Assert.assertEquals("{han4=[㑵, 㒈, 㢨, 㨔, 㪋, 㲦, 㵄, 㺝, 䁔, 䎯, 䏷, 䐄, 䓿, 䕿, 䖔, 䗣, 䘶, 䛞, 䜗, 䧲, 䫲, 䮧, 仠, 傼, 含, 垾, 屽, 岾, 忓, 悍, 感, 憾, 扞, 捍, 撖, 撼, 攼, 旰, 旱, 晘, 暵, 桿, 汉, 汗, 泔, 浛, 涆, 涵, 淊, 漢, 澉, 澣, 瀚, 灘, 焊, 熯, 猂, 皔, 睅, 矸, 罕, 翰, 肣, 莟, 菡, 蘫, 蛿, 蜭, 螒, 譀, 邯, 酣, 釬, 銲, 鋎, 閈, 闬, 雗, 靬, 頷, 顄, 顩, 颔, 馯, 駻, 鳱, 鶾, \uD842\uDC87, \uD843\uDE44, \uD844\uDC40, \uD846\uDCD4, \uD847\uDDDB, \uD847\uDEE1, \uD848\uDC35, \uD848\uDCD7, \uD848\uDD1C, \uD848\uDDDE, \uD848\uDF98, \uD849\uDD14, \uD84A\uDDE6, \uD84D\uDC3A, \uD84D\uDCB7, \uD850\uDC09, \uD850\uDF10, \uD851\uDFB6, \uD853\uDCC9, \uD853\uDFE7, \uD854\uDDCC, \uD854\uDE70, \uD858\uDEE3, \uD859\uDC85, \uD85C\uDC83, \uD85F\uDC2A, \uD85F\uDD4A, \uD860\uDC44, \uD861\uDECE, \uD862\uDC88, \uD863\uDE17, \uD864\uDF92, \uD865\uDD60, \uD865\uDDBA, \uD865\uDDE4, \uD865\uDFBF, \uD866\uDF65, \uD867\uDE51, \uD867\uDE7C, \uD867\uDF9D, \uD868\uDC5F, \uD86D\uDE23]}", map.toString());

        final char hanzi2 = '重';
        Map<String,List<String>> map2 = PinyinHelper.samePinyinMap(hanzi2);
        System.out.println(map2);
        Assert.assertEquals("{tong2=[㠉, 㠽, 㣚, 㣠, 㤏, 㮔, 㸗, 㼧, 㼿, 䂈, 䆚, 䮵, 䳋, 䴀, 䶱, 仝, 佟, 侗, 偅, 僮, 勭, 同, 哃, 垌, 峂, 峒, 峝, 庝, 彤, 晍, 曈, 朣, 桐, 橦, 氃, 洞, 浵, 湩, 潼, 烔, 燑, 爞, 犝, 狪, 獞, 痌, 眮, 瞳, 砼, 硐, 硧, 秱, 穜, 童, 筒, 筩, 粡, 絧, 膧, 艟, 茼, 蚒, 蜼, 蟲, 衕, 詷, 赨, 酮, 重, 鉖, 鉵, 銅, 铜, 餇, 鮦, 鲖, 鼕, \uD841\uDD84, \uD846\uDD9C, \uD848\uDE09, \uD848\uDFD5, \uD849\uDCD8, \uD84D\uDC78, \uD84E\uDEAF, \uD853\uDC47, \uD853\uDE84, \uD856\uDE4C, \uD856\uDEC2, \uD858\uDFC6, \uD859\uDC8D, \uD85A\uDE34, \uD85C\uDDCC, \uD85C\uDE9A, \uD85C\uDED2, \uD85C\uDEDA, \uD85C\uDF1D, \uD85F\uDCC6, \uD861\uDEAF, \uD861\uDF33, \uD861\uDF6F, \uD862\uDC0C, \uD864\uDF45, \uD866\uDE45, \uD867\uDEE1, \uD868\uDC2D, \uD86C\uDF63], zhong4=[㐺, 㲴, 㼿, 䱰, 中, 乑, 仲, 众, 偅, 堹, 妕, 媑, 狆, 眾, 祌, 种, 種, 穜, 筗, 緟, 茽, 蚛, 蟲, 衆, 衶, 衷, 褈, 諥, 踵, 重, \uD843\uDC67, \uD846\uDD7F, \uD849\uDF46, \uD84F\uDC67, \uD851\uDE8F, \uD857\uDEDD, \uD858\uDF0B, \uD859\uDD09, \uD85E\uDF24, \uD85F\uDCEE, \uD860\uDE62, \uD867\uDF8B, \uD867\uDFC0], chong2=[㓽, 㹐, 䌬, 䖝, 䳯, 崇, 崈, 漴, 烛, 爞, 痋, 种, 種, 緟, 茧, 虫, 蝩, 蟲, 褈, 酮, 重, 隀, \uD847\uDFC2, \uD849\uDD84, \uD849\uDF48, \uD84D\uDC2F, \uD85D\uDF4E, \uD861\uDEF1, \uD864\uDD43, \uD864\uDF28, \uD865\uDF16, \uD865\uDF89, \uD865\uDF8B]}", map2.toString());
    }


    /**
     * 同音字列表
     */
    @Test
    public void samePinyinListTest() {
        final String pinyinNumLast = "zhong4";
        List<String> pinyinList = PinyinHelper.samePinyinList(pinyinNumLast);
        System.out.println(pinyinList);
        Assert.assertEquals("[㐺, 㲴, 㼿, 䱰, 中, 乑, 仲, 众, 偅, 堹, 妕, 媑, 狆, 眾, 祌, 种, 種, 穜, 筗, 緟, 茽, 蚛, 蟲, 衆, 衶, 衷, 褈, 諥, 踵, 重, \uD843\uDC67, \uD846\uDD7F, \uD849\uDF46, \uD84F\uDC67, \uD851\uDE8F, \uD857\uDEDD, \uD858\uDF0B, \uD859\uDD09, \uD85E\uDF24, \uD85F\uDCEE, \uD860\uDE62, \uD867\uDF8B, \uD867\uDFC0]", pinyinList.toString());
    }
}
