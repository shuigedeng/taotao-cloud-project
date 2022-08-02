package com.taotao.cloud.sys.biz.modules.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.taotao.cloud.sys.biz.modules.core.dtos.RootTreeResponseDto;
import com.taotao.cloud.sys.biz.modules.core.dtos.TreeResponseDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TreeResponseDtoHelper {
    /**
     * 线性结构转树结构
     * @param treeModels
     * @param rootNo
     * @return
     */
//    public static List<? extends TreeResponseDto> convert(List<? extends TreeResponseDto> treeModels, String rootNo){
//        //查找树的根
//        List<TreeResponseDto> roots = findRoots(treeModels, rootNo);
//        if(roots != null && !roots.isEmpty()){
//            roots.stream().forEach(root -> findChildrens(root,treeModels));
//        }
//        return roots;
//    }

    /**
     * 查找所有子节点
     * @param root
     * @param treeResponseDtos
     * @return
     */
//    private static List<TreeResponseDto> findChildrens(TreeResponseDto root, List<? extends TreeResponseDto> treeResponseDtos) {
//        if(treeResponseDtos != null && !treeResponseDtos.isEmpty()){
//            List<TreeResponseDto> childrens = treeResponseDtos.stream().filter(treeModel -> root.getId().equals(treeModel.getParentId())).collect(Collectors.toList());
//            root.setChildrens(childrens);
//            childrens.stream().forEach(children -> findChildrens(children,treeResponseDtos));
//            return childrens;
//        }
//        return null;
//    }

    /**
     * 查找所有的根节点
     * @param treeModels
     * @param rootNo
     * @return
     */
//    public static List<TreeResponseDto> findRoots(List<? extends TreeResponseDto> treeModels,String rootNo){
//        List<TreeResponseDto> treeRoots = treeModels.stream().filter(treeModel -> treeModel.getId().equals(rootNo)).collect(Collectors.toList());
//        return treeRoots;
//    }

    /**
     * 查找根结点元素
     * @param treeModels
     * @param rootParentId
     * @return
     */
    public static List<? extends TreeResponseDto> findRoots(List<? extends TreeResponseDto> treeModels, String rootParentId){
        List<? extends TreeResponseDto> treeRoots = treeModels.stream().filter(treeModel -> (rootParentId + "").equals(((TreeResponseDto) treeModel).getParentId())).collect(Collectors.toList());
        return treeRoots;
    }

    /**
     * 快速转换森林结构
     * @param treeModels
     * @param rootParentId
     * @return
     */
    public static List<? extends TreeResponseDto> fastConvertForest(List<? extends TreeResponseDto> treeModels, String rootParentId){
        List<? extends TreeResponseDto> roots = findRoots(treeModels, rootParentId);
        if(CollectionUtils.isEmpty(roots)){
            log.warn("无根节点，无法生成树，rootParentId:"+rootParentId);
            return new ArrayList<>();
        }

        // 如果只有一个根节点，使用树转换
        if(roots.size() == 1){return fastConvertTree(treeModels,roots.get(0).getId());}
        //多个根节点，先把多个根节点挂载到虚拟根节点上，然后使用树转换
        RootTreeResponseDto<Object> virtualRoot = new RootTreeResponseDto<Object>(null) {
            @Override
            public String getId() {
                return rootParentId;
            }

            @Override
            public String getParentId() {
                return RandomStringUtils.random(10);
            }

            @Override
            public String getLabel() {
                return null;
            }

            @Override
            public Object getOrigin() {
                return null;
            }
        };

        List<TreeResponseDto> forest = new ArrayList<>();
        forest.addAll(treeModels);
        forest.add(virtualRoot);
        List<? extends TreeResponseDto> treeResponseDtos = fastConvertTree(forest, rootParentId);
        return treeResponseDtos.get(0).getChildren();
    }

    /**
     * 快速转换树结构; 还可以把 root 提取出来,变复杂度为 2O(n); 目前复杂度 3O(n)
     * @param treeModels
     * @return
     */
    public static List<? extends TreeResponseDto> fastConvertTree(List<? extends TreeResponseDto> treeModels, String rootNo){
        Map<String, ? extends TreeResponseDto> treeIdMap = treeModels.stream().collect(Collectors.toMap(TreeResponseDto::getId, treeModel -> treeModel));
        treeModels.stream().forEach(treeModel ->{
            TreeResponseDto parentTreeResponseDto = treeIdMap.get(((TreeResponseDto) treeModel).getParentId());
            if(parentTreeResponseDto != null){
                parentTreeResponseDto.getChildren().add(treeModel);
            }
        });

        //然后把 root 列表找出来
        List<? extends TreeResponseDto> collect = treeModels.stream().filter(treeModel ->
                ((TreeResponseDto) treeModel).getId().equals(rootNo)
        ).collect(Collectors.toList());
        return collect;
    }
}
