package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.git.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.cloud.sys.biz.api.controller.tools.core.utils.OnlyPath;
import lombok.ToString;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.ArrayList;
import java.util.List;

public class DiffChangesTree {
	/**
	 * 提交记录列表
	 */
	private List<String> commitIds = new ArrayList<>();
	/**
	 * 修改树列表
	 */
	private List<TreeFile> changeForest = new ArrayList<>();

	@ToString
	public static class TreeFile {
		private DiffEntry.ChangeType changeType;
		@JsonIgnore
		private OnlyPath relativePath;
		@JsonIgnore
		private OnlyPath parentRelativePath;
		private List<TreeFile> children = new ArrayList<>();
		/**
		 * 模块上次解析 classpath 的时间
		 */
		private Long classpathResolveTime;
		/**
		 * 模块上次编译时间, 如果当前是文件, 则为文件编译时间
		 */
		private Long lastCompileTime;

		public TreeFile() {
		}

		/**
		 * 主要用于构建目录的情况
		 * @param relativePath
		 */
		public TreeFile(OnlyPath relativePath) {
			this.relativePath = relativePath;
		}

		public TreeFile(DiffChanges.DiffFile diffFile) {
			this.changeType = diffFile.getChangeType();
			this.relativePath = new OnlyPath(diffFile.path());
		}

		/**
		 * 用于前端展示目录或文件名用; 如果真是 pom 文件改了会有 bug TODO
		 * @return
		 */
		public String getFileName(){
			final String fileName = relativePath.getFileName();
			return fileName;
		}

		public OnlyPath getRelativePath() {
			return relativePath;
		}

		public List<TreeFile> getChildren() {
			return children;
		}

		public DiffEntry.ChangeType getChangeType() {
			return changeType;
		}

		/**
		 * 用于前端 row-key 设置
		 * @return
		 */
		public String getPath(){
			if (isModule()){
				if (relativePath.getParent().isRoot()){
					// 如果当前模块是根路径, 响应项目名
					return parentRelativePath.getFileName();
				}
				return relativePath.getParent().getFileName();
			}
			return relativePath.toString();
		}


		public String getTreeFilePath(){
			return relativePath.toString();
		}

		/**
		 * 是否为模块标识
		 * @return
		 */
		public boolean isModule(){
			return changeType == null && "pom.xml".equals(getFileName());
		}

		/**
		 * 当前树节点是否是文件
		 * @return
		 */
		public boolean isFile(){
			if (isModule()){
				return false;
			}
			return this.relativePath.isFile();
		}

		/**
		 * 获取变更文件数
		 * @return
		 */
		public int getFileCount(){
			return calcTreeFileCount(this);
		}

		private int calcTreeFileCount(TreeFile treeFile){
			if (treeFile.isFile()){
				return 1;
			}
			int count = 0;
			for (TreeFile child : treeFile.getChildren()) {
				count += calcTreeFileCount(child);
			}
			return count;
		}

		public Long getClasspathResolveTime() {
			return classpathResolveTime;
		}

		public void setClasspathResolveTime(Long classpathResolveTime) {
			this.classpathResolveTime = classpathResolveTime;
		}

		public void setParentRelativePath(OnlyPath parentRelativePath) {
			this.parentRelativePath = parentRelativePath;
		}
	}
}
