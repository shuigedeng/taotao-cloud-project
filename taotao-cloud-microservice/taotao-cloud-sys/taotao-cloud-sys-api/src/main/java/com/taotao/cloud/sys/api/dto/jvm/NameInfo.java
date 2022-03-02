package com.taotao.cloud.sys.api.dto.jvm;

import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;

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
