package com.taotao.cloud.sys.biz.api.controller.tools.monitor;

import com.taotao.cloud.sys.biz.service.IZookeeperService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ZookeeperController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 15:56:19
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-zookeeper管理API", description = "工具管理端-zookeeper管理API")
@RequestMapping("/sys/tools/monitor/zookeeper")
public class ZookeeperController {

	private final IZookeeperService zookeeperService;

	///**
	// * 添加路径收藏
	// */
	//@PostMapping("/addFavorite")
	//public void addFavorite(@NotNull String connName, @NotNull String name, @NotNull String path){
	//    PathFavorite pathFavorite = new PathFavorite(name, path);
	//    zookeeperExtendService.addFavorite(connName,pathFavorite);
	//}
	//
	//@PostMapping("/removeFavorite")
	//public void removeFavorite(@NotNull String connName,@NotNull String name){
	//    zookeeperExtendService.removeFavorite(connName,name);
	//}
	//
	///**
	// * 列出收藏夹
	// */
	//@GetMapping("/favorites")
	//public Set<PathFavorite> favorites(@NotNull String connName){
	//    return zookeeperExtendService.favorites(connName);
	//}
	//
	///**
	// * zookeeper 子节点
	// * @param connName 连接名称
	// * @param path 父级路径
	// */
	//@GetMapping("/childrens")
	//public List<String> childrens(@NotNull String connName, @NotNull String path) throws IOException {
	//    return zookeeperService.childrens(connName,path);
	//}
	//
	///**
	// * zookeeper 节点元数据
	// * @param connName 连接名称
	// * @param path 节点路径
	// */
	//@GetMapping("/meta")
	//public Stat meta(@NotNull String connName, @NotNull String path) throws IOException{
	//    return zookeeperService.meta(connName,path);
	//}
	//
	///**
	// * 节点权限信息
	// * @param connName 连接名称
	// * @param path 节点路径
	// */
	//@GetMapping("/acls")
	//public List<ZooNodeACL> acls(@NotNull String connName, @NotNull String path) throws IOException{
	//    return zookeeperService.acls(connName,path);
	//}
	//
	///**
	// * 读取节点数据
	// * @param connName 连接名称
	// * @param path  节点路径
	// * @param deserialize 序列化
	// */
	//@GetMapping("/readData")
	//public Object readData(@NotNull String connName,@NotNull String path,String deserialize) throws IOException{
	//    return zookeeperService.readData(connName,path,deserialize);
	//}
	//
	///**
	// * 删除节点
	// * @param connName 连接名称
	// * @param path 节点路径
	// */
	//@PostMapping("/deleteNode")
	//public void deleteNode(@NotNull String connName,@NotNull String path) throws IOException{
	//    zookeeperService.deleteNode(connName,path);
	//}
	//
	///**
	// * 写入数据
	// * @param connName 连接名称
	// * @param path 节点路径
	// * @param data 数据
	// */
	//@PostMapping("/writeData")
	//public void writeData(@NotNull String connName,@NotNull String path,@NotNull String data) throws IOException {
	//    zookeeperService.writeData(connName,path,data);
	//}
}
