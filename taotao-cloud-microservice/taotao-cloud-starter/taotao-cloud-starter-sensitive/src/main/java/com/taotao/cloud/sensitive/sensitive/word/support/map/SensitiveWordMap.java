package com.taotao.cloud.sensitive.sensitive.word.support.map;


import com.google.common.collect.Lists;
import com.taotao.cloud.common.support.instance.impl.Instances;
import com.taotao.cloud.common.utils.collection.CollectionUtils;
import com.taotao.cloud.common.utils.io.FileUtils;
import com.taotao.cloud.common.utils.lang.ObjectUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sensitive.sensitive.word.api.ISensitiveWordReplace;
import com.taotao.cloud.sensitive.sensitive.word.api.ISensitiveWordReplaceContext;
import com.taotao.cloud.sensitive.sensitive.word.api.IWordContext;
import com.taotao.cloud.sensitive.sensitive.word.api.IWordMap;
import com.taotao.cloud.sensitive.sensitive.word.api.IWordResult;
import com.taotao.cloud.sensitive.sensitive.word.constant.AppConst;
import com.taotao.cloud.sensitive.sensitive.word.constant.enums.ValidModeEnum;
import com.taotao.cloud.sensitive.sensitive.word.support.check.SensitiveCheckResult;
import com.taotao.cloud.sensitive.sensitive.word.support.check.impl.SensitiveCheckChain;
import com.taotao.cloud.sensitive.sensitive.word.support.check.impl.SensitiveCheckUrl;
import com.taotao.cloud.sensitive.sensitive.word.support.replace.SensitiveWordReplaceContext;
import com.taotao.cloud.sensitive.sensitive.word.support.result.WordResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词 map
 *
 */
public class SensitiveWordMap implements IWordMap {

    /**
     * 脱敏单词 map
     *
     */
    private Map innerWordMap;

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：
     *
     * @param collection 敏感词库集合
     * <p>
     * 使用对象代码 map 的这种一直递归。
     * 参考资料：https://www.cnblogs.com/AlanLee/p/5329555.html
     * https://blog.csdn.net/chenssy/article/details/26961957
     */
    @Override
    @SuppressWarnings("unchecked")
    public synchronized void initWordMap(Collection<String> collection) {
        long startTime = System.currentTimeMillis();
        // 避免扩容带来的消耗
        Map newInnerWordMap = new HashMap(collection.size());

        for (String key : collection) {
            if (StringUtils.isEmpty(key)) {
                continue;
            }

            // 用来按照相应的格式保存敏感词库数据
            char[] chars = key.toCharArray();
            final int size = chars.length;

            // 每一个新词的循环，直接将结果设置为当前 map，所有变化都会体现在结果的 map 中
            Map currentMap = newInnerWordMap;

            for (int i = 0; i < size; i++) {
                // 截取敏感词当中的字，在敏感词库中字为HashMap对象的Key键值
                char charKey = chars[i];
                // 如果集合存在
                Object wordMap = currentMap.get(charKey);

                // 如果集合存在
                if (ObjectUtils.isNotNull(wordMap)) {
                    // 直接将获取到的 map 当前当前 map 进行继续的操作
                    currentMap = (Map) wordMap;
                } else {
                    //不存在则，则构建一个新的map，同时将isEnd设置为0，因为他不是最后一
                    Map<String, Boolean> newWordMap = new HashMap<>(8);
                    newWordMap.put(AppConst.IS_END, false);

                    // 将新的节点放入当前 map 中
                    currentMap.put(charKey, newWordMap);

                    // 将新节点设置为当前节点，方便下一次节点的循环。
                    currentMap = newWordMap;
                }

                // 判断是否为最后一个，添加是否结束的标识。
                if (i == size - 1) {
                    currentMap.put(AppConst.IS_END, true);
                }
            }
        }

        // 最后更新为新的 map，保证更新过程中旧的数据可用
        this.innerWordMap = newInnerWordMap;

        long endTime = System.currentTimeMillis();
        LogUtils.info("Init sensitive word map end! Cost time: " + (endTime - startTime) + "ms");
    }

    /**
     * 是否包含
     * （1）直接遍历所有
     * （2）如果遇到，则直接返回 true
     *
     * @param string 字符串
     * @return 是否包含
     */
    @Override
    public boolean contains(String string, final IWordContext context) {
        if (StringUtils.isEmpty(string)) {
            return false;
        }

        for (int i = 0; i < string.length(); i++) {
            SensitiveCheckResult checkResult = sensitiveCheck(string, i, ValidModeEnum.FAIL_FAST, context);
            // 快速返回
            if (checkResult.index() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回所有对应的敏感词
     * （1）结果是有序的
     * （2）为了保留所有的下标，结果从 v0.1.0 之后不再去重。
     *
     * @param string 原始字符串
     * @return 结果
     */
    @Override
    public List<IWordResult> findAll(String string, final IWordContext context) {
        return getSensitiveWords(string, ValidModeEnum.FAIL_OVER, context);
    }

    @Override
    public IWordResult findFirst(String string, final IWordContext context) {
        List<IWordResult> stringList = getSensitiveWords(string, ValidModeEnum.FAIL_FAST, context);

        if (CollectionUtils.isEmpty(stringList)) {
            return null;
        }

        return stringList.get(0);
    }

    @Override
    public String replace(String target, final ISensitiveWordReplace replace, final IWordContext context) {
        if(StringUtils.isEmpty(target)) {
            return target;
        }

        return this.replaceSensitiveWord(target, replace, context);
    }

    /**
     * 获取敏感词列表
     *
     * @param text     文本
     * @param modeEnum 模式
     * @return 结果列表
     */
    private List<IWordResult> getSensitiveWords(final String text, final ValidModeEnum modeEnum,
                                           final IWordContext context) {
        //1. 是否存在敏感词，如果比存在，直接返回空列表
        if (StringUtils.isEmpty(text)) {
            return Lists.newArrayList();
        }

        List<IWordResult> resultList = Lists.newArrayList();
        for (int i = 0; i < text.length(); i++) {
            SensitiveCheckResult checkResult = sensitiveCheck(text, i, ValidModeEnum.FAIL_OVER, context);
            // 命中
            int wordLength = checkResult.index();
            if (wordLength > 0) {
                // 保存敏感词
                String sensitiveWord = text.substring(i, i + wordLength);

                // 添加去重
                WordResult wordResult = WordResult.newInstance()
                        .startIndex(i)
                        .endIndex(i+wordLength)
                        .word(sensitiveWord);
                resultList.add(wordResult);

                // 快速返回
                if (ValidModeEnum.FAIL_FAST.equals(modeEnum)) {
                    break;
                }

                // 增加 i 的步长
                // 为什么要-1，因为默认就会自增1
                // TODO: 这里可以根据字符串匹配算法优化。
                i += wordLength - 1;
            }
        }

        return resultList;
    }

    /**
     * 直接替换敏感词，返回替换后的结果
     * @param target           文本信息
     * @param replace 替换策略
     * @param context 上下文
     * @return 脱敏后的字符串
     */
    private String replaceSensitiveWord(final String target,
                                        final ISensitiveWordReplace replace,
                                        final IWordContext context) {
        if(StringUtils.isEmpty(target)) {
            return target;
        }
        // 用于结果构建
        StringBuilder resultBuilder = new StringBuilder(target.length());

        for (int i = 0; i < target.length(); i++) {
            char currentChar = target.charAt(i);
            // 内层直接从 i 开始往后遍历，这个算法的，获取第一个匹配的单词
            SensitiveCheckResult checkResult = sensitiveCheck(target, i, ValidModeEnum.FAIL_OVER, context);

            // 敏感词
            int wordLength = checkResult.index();
            if(wordLength > 0) {
                // 是否执行替换
                Class checkClass = checkResult.checkClass();
                String string = target.substring(i, i+wordLength);
                if(SensitiveCheckUrl.class.equals(checkClass)
                    && FileUtils.isImage(string)) {
                    // 直接使用原始内容，避免 markdown 图片转换失败
                    resultBuilder.append(string);
                } else {
                    // 创建上下文
                    ISensitiveWordReplaceContext replaceContext = SensitiveWordReplaceContext.newInstance()
                            .sensitiveWord(string)
                            .wordLength(wordLength);
                    String replaceStr = replace.replace(replaceContext);

                    resultBuilder.append(replaceStr);
                }

                // 直接跳过敏感词的长度
                i += wordLength-1;
            } else {
                // 普通词
                resultBuilder.append(currentChar);
            }
        }

        return resultBuilder.toString();
    }

    @Override
    public SensitiveCheckResult sensitiveCheck(String txt, int beginIndex, ValidModeEnum validModeEnum, IWordContext context) {
        // 默认执行敏感词操作
        context.sensitiveWordMap(innerWordMap);

        // 责任链模式调用
        return Instances.singleton(SensitiveCheckChain.class)
                .sensitiveCheck(txt, beginIndex, validModeEnum, context);
    }

}
