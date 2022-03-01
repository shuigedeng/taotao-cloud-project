package com.taotao.cloud.sys.biz.tools.jvm.service.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.List;

/**
 * 名称信息
 */
public class NameInfo {
	private String label;
	private List<NameInfo> children = new ArrayList<>();
	private ObjectName ref;

	/**
	 * 节点类型
	 */
	private NodeType nodeType = NodeType.FOLDER;

	public NameInfo() {
	}

	public NameInfo(String label) {
		this.label = label;
	}

	public enum NodeType {
		FOLDER, MBEAN
	}

	public String getLabel() {
		return label;
	}

	public List<NameInfo> getChildren() {
		return children;
	}

	public String getRef() {
		if (ref != null) {
			return ref.getCanonicalName();
		}
		return null;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setRef(ObjectName ref) {
		this.ref = ref;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}
}
