package com.taotao.cloud.common.utils.pinyin.support.segment.trie;//package com.taotao.cloud.core.pinyin.support.segment.trie;
//
//import com.github.houbb.heaven.annotation.ThreadSafe;
//import com.github.houbb.heaven.util.guava.Guavas;
//
//import com.taotao.cloud.core.pinyin.support.tone.PinyinTones;
//import java.util.Collection;
//import java.util.Map;
//
///**
// * <p> project: pinyin-DefaultPinyinTrieMap </p>
// * <p> create on 2020/2/7 17:39 </p>
// *
// */
//@ThreadSafe
//public class DefaultPinyinTrieTreeMap extends AbstractTrieTreeMap {
//
//    /**
//     * 内部单词 map
//     *
//     * @since 0.0.2
//     */
//    private static volatile Map innerWordMap = Guavas.newHashMap();
//
//    @Override
//    protected Map getStaticVolatileMap() {
//        return innerWordMap;
//    }
//
//    @Override
//    protected Collection<String> getWordCollection() {
//        return PinyinTones.defaults().phraseSet();
//    }
//
//}
