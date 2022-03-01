package com.taotao.cloud.sys.biz.tools.codepatch.service.dtos;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jgit.diff.DiffEntry;

/**
 * diffEntry: DiffEntry[MODIFY src/main/java/com/sanri/app/servlet/SqlClientServlet.java]
 * relativePath: com/sanri/app/servlet/SqlClientServlet.java modulePath: sanri-tools-maven
 * compileFiles: - com/sanri/app/servlet/SqlClientServlet$1.class - com/sanri/app/servlet/SqlClientServlet.class
 */
public final class FileInfo {

	// 子模块路径
	private File modulePath;
	private DiffEntry diffEntry;
	private Path relativePath;
	private String baseName;
	private String extension;
	private Collection<File> compileFiles = new ArrayList<>();

	public FileInfo(DiffEntry diffEntry, Path relativePath) {
		this.diffEntry = diffEntry;
		this.relativePath = relativePath;
		this.baseName = FilenameUtils.getBaseName(relativePath.toFile().getName());
		this.extension = FilenameUtils.getExtension(relativePath.toFile().getName());
	}

	public FileInfo(DiffEntry diffEntry, Path relativePath, Collection<File> compileFiles) {
		this.diffEntry = diffEntry;
		this.relativePath = relativePath;
		this.compileFiles = compileFiles;
		this.baseName = FilenameUtils.getBaseName(relativePath.toFile().getName());
		this.extension = FilenameUtils.getExtension(relativePath.toFile().getName());
	}


	public File getModulePath() {
		return modulePath;
	}

	public void setModulePath(File modulePath) {
		this.modulePath = modulePath;
	}

	public DiffEntry getDiffEntry() {
		return diffEntry;
	}

	public void setDiffEntry(DiffEntry diffEntry) {
		this.diffEntry = diffEntry;
	}

	public Path getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(Path relativePath) {
		this.relativePath = relativePath;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public Collection<File> getCompileFiles() {
		return compileFiles;
	}

	public void setCompileFiles(Collection<File> compileFiles) {
		this.compileFiles = compileFiles;
	}


	@Override
	public String toString() {
		return "FileInfo{" +
			"modulePath=" + modulePath +
			", diffEntry=" + diffEntry +
			", relativePath=" + relativePath +
			", baseName='" + baseName + '\'' +
			", extension='" + extension + '\'' +
			", compileFiles=" + compileFiles +
			'}';
	}
}
