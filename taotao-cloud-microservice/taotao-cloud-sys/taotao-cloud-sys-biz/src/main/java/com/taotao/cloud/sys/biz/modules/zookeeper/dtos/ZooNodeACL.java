package com.taotao.cloud.sys.biz.modules.zookeeper.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 作者:sanri <br>
 * 时间:2017-7-24下午6:21:09<br>
 * 功能:节点的 acl 权限 <br>
 */
@Data
public class ZooNodeACL {
	private String schema;
	private String id;
	private int perms;
	private List<String> permsParser = new ArrayList<String>();
	
	public ZooNodeACL(String schema, String id, int perms){
		this.schema = schema;
		this.id = id;
		this.perms = perms;
		
		parserPerms();
	}
	
	/**
	 * 
	 * 作者:sanri <br>
	 * 时间:2017-7-24下午6:31:04<br>
	 * 功能:对权限进行解析  <br>
	 */
	private void parserPerms() {
		if ((perms & 0x1) == 1) {
			permsParser.add("read");
		}
		if ((perms & 0x2) == 2) {
			permsParser.add("write");
		}
		if ((perms & 0x4) == 4) {
			permsParser.add("create");
		}
		if ((perms & 0x8) == 8) {
			permsParser.add("delete");
		}
		if ((perms & 0x10) == 16) {
			permsParser.add("admin");
		}
	}
}
