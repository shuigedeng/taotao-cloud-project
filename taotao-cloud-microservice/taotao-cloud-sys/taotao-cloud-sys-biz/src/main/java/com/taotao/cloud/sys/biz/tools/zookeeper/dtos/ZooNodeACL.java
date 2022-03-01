package com.taotao.cloud.sys.biz.tools.zookeeper.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 功能:节点的 acl 权限 <br/>
 */
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
	 * 作者:sanri <br/>
	 * 时间:2017-7-24下午6:31:04<br/>
	 * 功能:对权限进行解析  <br/>
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

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPerms() {
		return perms;
	}

	public void setPerms(int perms) {
		this.perms = perms;
	}

	public List<String> getPermsParser() {
		return permsParser;
	}

	public void setPermsParser(List<String> permsParser) {
		this.permsParser = permsParser;
	}
}
