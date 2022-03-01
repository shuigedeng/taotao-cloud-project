package com.taotao.cloud.sys.biz.tools.name.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 中文词序列
 */
public class TranslateCharSequence {
    // 原始词序列
    private CharSequence originSequence;

    //分词结果; 可能有很多种分词结果
    private List<List<String>> charses = new ArrayList<List<String>>();

    // 每个词对应的可能结果列表
    private Map<String,Set<String>> charMaps  = new HashMap<String, Set<String>>();

    //每个词是否需要后续处理
    private Map<String,Boolean> charTranslate = new HashMap<String, Boolean>();

    // 词直接英语翻译结果
    private Set<String> directTranslates = new HashSet<String>() ;

    // 整个词串是否需要后续处理
    private boolean translate ;

    public TranslateCharSequence(CharSequence originSequence) {
        this.originSequence = originSequence;
    }

    /**
     * 初始化翻译映射列表
     */
    public void initCharMaps(){
        for (List<String> chars : charses) {
            for (String perChar : chars) {
                charMaps.put(perChar,new HashSet<String>());
                charTranslate.put(perChar,false);
            }
        }
    }

    /**
     * 整个句子是否已经翻译完成
     * @return
     */
    public boolean isTranslate() {
        return translate;
    }

    /**
     * 某个词是否已经翻译
     * @param perChar
     * @return
     */
    public boolean isTranslate(String perChar){
        Boolean aBoolean = charTranslate.get(perChar);
        if(aBoolean == null) {
            return false;
        }
        return aBoolean.booleanValue();
    }

    /**
     * 设置翻译完成
     * @param translate
     */
    public void setTranslate(boolean translate) {
        this.translate = translate;
    }

    /**
     * 设置翻译完成
     * @param translate
     * @param perChar
     */
    public void setTranslate(boolean translate,String perChar) {
        charTranslate.put(perChar,translate);
    }

    /**
     * 添加分词结果
     * @param chars
     */
    public void addSegment(List<String> chars){
        charses.add(chars);
    }

    /**
     * 添加翻译结果
     * @param perChar
     */
    public void addTranslate(String perChar,String value){
        Set<String> values = charMaps.get(perChar);
        if(values == null){
            values = new HashSet<String>();
            charMaps.put(perChar,values);
        }

        values.add(value);
    }

    private void perpareResult(String perChar){
         Set<String> values = charMaps.get(perChar);
        if(values == null) {
            values = new HashSet<String>();
            values.add(perChar);
            charMaps.put(perChar,values);
        }
    }

    /**
     * 结果列表; 算法,先用层序遍历,将结果转换为树模型,然后对树模型进行深度优先遍历
     * @return
     */
    public Set<String> results(){
        Set<String> results = new HashSet<String>();
        if(!CollectionUtils.isEmpty(directTranslates)){
            //第一个结果为直译结果
            results.addAll(directTranslates);
        }

        for (List<String> chars : charses) {
            TreeModel root = new TreeModel("顶层");
            convert2TreeModel(chars,root,0);

            //深度优先搜索树
            Stack<TreeModel> stack = new Stack<TreeModel>();
            List<List<TreeModel>> treeResults = new ArrayList<List<TreeModel>>();
            loadProbablePaths(root,stack,treeResults);

            for (List<TreeModel> treeResult : treeResults) {
                //转换成驼峰命名
                String first = treeResult.get(1).element;
                String uncapitalize = StringUtils.uncapitalize(first);
                StringBuffer result = new StringBuffer(uncapitalize);
                for (int i = 2; i < treeResult.size(); i++) {
                    result.append(StringUtils.capitalize(treeResult.get(i).element));
                }

                results.add(result.toString());
            }

        }

        return results;
    }

    /**
     * 使用树的深度优先搜索遍历查找出所有根节点到叶子节点的路径
     * @param root
     * @param stack
     * @param treeResults
     */
    private void loadProbablePaths(TreeModel root, Stack<TreeModel> stack, List<List<TreeModel>> treeResults) {
        stack.push(root);
        List<TreeModel> childrens = root.childrens;
        if(CollectionUtils.isEmpty(childrens)){
            //为叶子节点,把栈中信息依次输出
            List<TreeModel> path = new ArrayList<TreeModel>();
            for (TreeModel treeModel : stack) {
                path.add(treeModel);
            }
            treeResults.add(path);
        }else{
            //加载子节点可能的路径
            for (TreeModel children : childrens) {
                loadProbablePaths(children,stack,treeResults);
                stack.pop();        //回溯时候出栈,加载下一条路径的时候，需要替换当前顶层元素比如 am 和 is
            }
        }
    }


    /**
     * 查找树的子节点,到到当前句子完毕;
     * 这个会根据当前分词的句子，把翻译词整成一颗树状
     *      I
     *   is     am
     * chinaman     chinaman
     * @param sequ
     * @param parent
     * @param deep
     */
    public void convert2TreeModel(List<String> sequ, TreeModel parent, int deep){
        if(deep >= sequ.size()){
            return ;
        }
        String element = sequ.get(deep);
        Set<String> values = charMaps.get(element);
        deep++;
        for (String value : values) {
            TreeModel child = new TreeModel(value);
            parent.addChild(child);
            convert2TreeModel(sequ,child,deep);
         }
    }

    class TreeModel {
        private String element;
        private List<TreeModel> childrens = new ArrayList();

        public TreeModel(String element) {
            this.element = element;
        }

        public void addChild(TreeModel treeModel){
            childrens.add(treeModel);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    public CharSequence getOriginSequence() {
        return originSequence;
    }

    /**
     * 获取需要翻译的词列表
     * @return
     */
    public Set<String> getNeedTranslateWords(){
        Set<String> words = charMaps.keySet();
        Set<String> needs = new HashSet<String>();
        for (String word : words) {
            boolean translate = isTranslate(word);
            if(!translate){
                needs.add(word);
            }
        }
        return needs;
    }

    /**
     * 添加直译结果
     * @param directTranslate
     */
    public void addDirectTranslate(String directTranslate){
        this.directTranslates.add(directTranslate);
    }

    public Map<String, Set<String>> getCharMaps() {
        return charMaps;
    }

    public Set<String> getDirectTranslates() {
        return directTranslates;
    }
}
