package com.taotao.cloud.sys.biz.tools.codepatch.service.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jgit.diff.DiffEntry;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeFiles {
    private List<String> commitIds = new ArrayList<>();
    @JsonIgnore
    private List<FileInfo> modifyFileInfos = new ArrayList<>();
    @JsonIgnore
    private List<FileInfo> deleteFileInfos = new ArrayList<>();

    public ChangeFiles() {
    }

    public ChangeFiles(List<String> commitIds) {
    }

    public ChangeFiles(List<String> commitIds, List<FileInfo> modifyFileInfos, List<FileInfo> deleteFileInfos) {
        this.commitIds = commitIds;
        this.modifyFileInfos = modifyFileInfos;
        this.deleteFileInfos = deleteFileInfos;
    }

    public ChangeFiles(List<FileInfo> modifyFileInfos, List<FileInfo> deleteFileInfos) {
        this.modifyFileInfos = modifyFileInfos;
        this.deleteFileInfos = deleteFileInfos;
    }

    /**
     * 获取原始变更文件列表
     * @return
     */
    public List<ChangeFile> getChangeFiles(){
        final ArrayList<FileInfo> fileInfos = new ArrayList<>(modifyFileInfos);
        fileInfos.addAll(deleteFileInfos);

        return fileInfos.stream().map(modifyFileInfo ->
                new ChangeFile(modifyFileInfo.getDiffEntry().getChangeType(),
                        modifyFileInfo.getRelativePath().toString(),
                        modifyFileInfo.getCompileFiles().stream().map(File::toPath).map(Path::toString).collect(Collectors.toList()),
                        modifyFileInfo.getCompileFiles().stream().map(File::toPath).map(Path::toFile).collect(Collectors.toList())
                        )).collect(Collectors.toList());
    }

    public List<String> getCommitIds() {
        return commitIds;
    }

    public static final class ChangeFile{
        private DiffEntry.ChangeType changeType;
        private String relativePath;
        // 编译路径, 将使用 compileFiles
        @Deprecated
        private List<String> compilePaths = new ArrayList<>();
        // 带编译时间的编译路径
        private List<CompileFile> compileFiles = new ArrayList<>();

        public ChangeFile() {
        }

        public ChangeFile(DiffEntry.ChangeType changeType, String relativePath, List<String> compilePaths) {
            this.changeType = changeType;
            this.relativePath = relativePath;
            this.compilePaths = compilePaths;
        }

        public ChangeFile(DiffEntry.ChangeType changeType, String relativePath, List<String> compilePaths, List<File> files) {
            this.changeType = changeType;
            this.relativePath = relativePath;
            this.compilePaths = compilePaths;
            if (CollectionUtils.isNotEmpty(files)){
                for (File file : files) {
                    compileFiles.add(new CompileFile(file.getPath(), new Date(file.lastModified())));
                }
            }
        }

	    public DiffEntry.ChangeType getChangeType() {
		    return changeType;
	    }

	    public void setChangeType(DiffEntry.ChangeType changeType) {
		    this.changeType = changeType;
	    }

	    public String getRelativePath() {
		    return relativePath;
	    }

	    public void setRelativePath(String relativePath) {
		    this.relativePath = relativePath;
	    }

	    public List<String> getCompilePaths() {
		    return compilePaths;
	    }

	    public void setCompilePaths(List<String> compilePaths) {
		    this.compilePaths = compilePaths;
	    }

	    public List<CompileFile> getCompileFiles() {
		    return compileFiles;
	    }

	    public void setCompileFiles(
		    List<CompileFile> compileFiles) {
		    this.compileFiles = compileFiles;
	    }
    }

    public static final class CompileFile{
        private String path;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date modifyTime;

        public CompileFile() {
        }

        public CompileFile(String path, Date modifyTime) {
            this.path = path;
            this.modifyTime = modifyTime;
        }

        public long getTime(){
            if (modifyTime == null){
                return -1;
            }
            return modifyTime.getTime();
        }

	    public String getPath() {
		    return path;
	    }

	    public void setPath(String path) {
		    this.path = path;
	    }

	    public Date getModifyTime() {
		    return modifyTime;
	    }

	    public void setModifyTime(Date modifyTime) {
		    this.modifyTime = modifyTime;
	    }
    }

    public List<FileInfo> getDeleteFileInfos() {
        return deleteFileInfos;
    }

    public List<FileInfo> getModifyFileInfos() {
        return modifyFileInfos;
    }

    public void setCommitIds(List<String> commitIds) {
        this.commitIds = commitIds;
    }
}
