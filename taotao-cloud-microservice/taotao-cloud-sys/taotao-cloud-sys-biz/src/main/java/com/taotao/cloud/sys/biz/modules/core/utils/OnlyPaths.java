package com.taotao.cloud.sys.biz.modules.core.utils;

import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class OnlyPaths {

    /**
     * 路径列表转树结构, 路径列表中路径必须全部以根路径做前缀,否则会解析失败
     * @param paths 路径列表,必须都是文件(未排序)
     * @param root 根路径
     * @return
     */
    public static TreeFile treeFiles(List<OnlyPath> paths,OnlyPath root){
        // paths 映射 TreeFile
        final List<TreeFile> treeFiles = paths.stream().filter(OnlyPath::isFile).map(path -> new TreeFile(path, root)).collect(Collectors.toList());

        // root 映射 TreeFile
        final TreeFile rootTreeFile = new TreeFile(root, root);

        // 映射成 map
        final Map<OnlyPath, TreeFile> treeFileMap = treeFiles.stream().collect(Collectors.toMap(TreeFile::getWholePath, Function.identity()));

        // 构建树结构
        for (TreeFile treeFile : treeFiles) {
            final OnlyPath parentPath = treeFile.getWholePath().getParent();
            if (treeFileMap.containsKey(parentPath)){
                treeFileMap.get(parentPath).getChildren().add(treeFile);
                treeFile.setParentWholePath(parentPath);
                continue;
            }

            // 如果不存在上级路径, 则创建上级路径并挂载到最近的一个路径上, 如果上级路径是根路径, 则直接挂载到根路径上;
            // 异常情况: 根路径是不能为空的, 如果上级路径为空, 说明路径本身就是 / 路径, 这种情况需要排除
            if (parentPath == null){
                log.error("路径的父级为空, 说明这个路径在 root 之外, 可以丢弃此路径: {}",treeFile.getWholePath());
                continue;
            }

            if (parentPath.equals(root)){
                // parentPath 路径是根路径, 则将当前路径挂载到根路径上
                rootTreeFile.getChildren().add(treeFile);
                treeFile.setParentWholePath(rootTreeFile.getWholePath());
                continue;
            }

            // 新建一个 parent TreeFile, 并添加到 treeFileMap 中
            final TreeFile parentTreeFile = new TreeFile(parentPath, root);
            parentTreeFile.getChildren().add(treeFile);
            treeFile.setParentWholePath(parentTreeFile.getWholePath());
            treeFileMap.put(parentPath,parentTreeFile);

            // 将 parentTreeFile 进行挂载, 一直往上找父级, 找到根路径时, 挂载到根上; 如果一直没找到根, 则抛弃
            OnlyPath supParent = parentPath.getParent();
            while (supParent != null && !supParent.equals(root)){
                if (treeFileMap.containsKey(supParent)){
                    treeFileMap.get(supParent).getChildren().add(parentTreeFile);
                    parentTreeFile.setParentWholePath(supParent);
                    break;
                }
                supParent = supParent.getParent();

                // 如果 supParent 路径比 root 短, 则也是没找到
                if (supParent.getNameCount() < root.getNameCount()){
                    break;
                }
            }
            if (supParent == null || supParent.getNameCount() < root.getNameCount()){
                // 如果找到顶层了, 没找着, 则抛弃此路径
                log.error("抛弃路径[{}],它不属于 root 下的路径",supParent);
                continue;
            }

            if (supParent.equals(root)){
                // 如果找到了 root 路径没找着, 那么挂载到 root 路径
                rootTreeFile.getChildren().add(parentTreeFile);
                parentTreeFile.setParentWholePath(rootTreeFile.getWholePath());
                continue;
            }

            // 其它情况, 应该不存在
            log.error("未知情况, 路径: {}, root: {}",parentTreeFile,root);
        }

        return rootTreeFile;
    }

    /**
     * 从一堆路径中, 过滤出最短路径列表
     * @param paths 路径列表(未排序)
     * @return
     */
    public static List<OnlyPath> filterShorterPaths(Collection<OnlyPath> paths){
        List<OnlyPath> topPaths = new ArrayList<>();
        for (OnlyPath onlyPath : paths) {
            if (topPaths.size() == 0){
                topPaths.add(onlyPath);
                continue;
            }
            final int nameCount = topPaths.get(0).getNameCount();
            if (onlyPath.getNameCount() == nameCount){
                topPaths.add(onlyPath);
                continue;
            }
            if (onlyPath.getNameCount() < nameCount){
                topPaths.clear();
                topPaths.add(onlyPath);
                continue;
            }
        }
        return topPaths;
    }
}
