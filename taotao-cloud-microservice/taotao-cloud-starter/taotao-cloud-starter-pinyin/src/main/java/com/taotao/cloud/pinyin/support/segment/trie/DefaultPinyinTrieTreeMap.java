package com.taotao.cloud.pinyin.support.segment.trie;//package com.taotao.cloud.core.pinyin.support.segment.trie;
//
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
