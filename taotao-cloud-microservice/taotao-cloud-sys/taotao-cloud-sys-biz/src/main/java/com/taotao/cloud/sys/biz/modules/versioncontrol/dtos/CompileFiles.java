package com.taotao.cloud.sys.biz.modules.versioncontrol.dtos;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sanri.tools.modules.core.utils.OnlyPath;
import com.sanri.tools.modules.versioncontrol.git.dtos.DiffChanges;

import com.sanri.tools.modules.core.dtos.RelativeFile;
import lombok.Data;
import org.eclipse.jgit.diff.DiffEntry;

import javax.security.sasl.RealmCallback;

@Data
public class CompileFiles {
    private List<String> commitIds = new ArrayList<>();
    /**
     * 编译后文件
     */
    private List<DiffCompileFile> diffCompileFiles = new ArrayList<>();

    public CompileFiles() {
    }

    public CompileFiles(DiffChanges diffChanges) {
        this.commitIds = diffChanges.getCommitIds();
    }

    public void addDiffCompileFile(DiffCompileFile diffCompileFile){
        this.diffCompileFiles.add(diffCompileFile);
    }


    @Data
    public static final class DiffCompileFile {
        /**
         * 源文件变更信息
         */
        @JsonIgnore
        private DiffChanges.DiffFile diffFile;

        /**
         * 用于前端展示用, 变更文件信息
         */
        private DiffFilePath diffFilePath;

        /**
         * 模块路径(eg: tools-core ;  eg2: module-parent/module-a/module-ab) 非绝对路径
         * 相对于项目的路径
         */
        private RelativeFile modulePath;

        /**
         * 编译后文件列表, 相对于 classpath 路径
         */
        private Collection<RelativeFile> compileFiles = new ArrayList<>();

        public DiffCompileFile() {
        }

        public DiffCompileFile(DiffChanges.DiffFile diffFile) {
            this.diffFile = diffFile;
        }

        public DiffCompileFile(DiffChanges.DiffFile diffFile, RelativeFile modulePath, OnlyPath projectPath) {
            this.diffFile = diffFile;
            this.modulePath = modulePath;
            OnlyPath relativePath = projectPath.resolve(modulePath.path()).relativize(diffFile.path());
            final File moduleDir = modulePath.relativeFile();
//            this.relativeDiffFile = new DiffChanges.DiffFile(diffFile.getChangeType(),new RelativeFile(moduleDir,relativePath));
            this.diffFilePath = new DiffFilePath(projectPath.resolve(modulePath.path()),new RelativeFile(moduleDir,relativePath),diffFile.getChangeType());
        }
    }

    @Data
    public static final class DiffFilePath{
        private OnlyPath parentPath;
        private RelativeFile relativeFile;
        private DiffEntry.ChangeType changeType;

        public DiffFilePath(OnlyPath parentPath, RelativeFile relativeFile, DiffEntry.ChangeType changeType) {
            this.parentPath = parentPath;
            this.relativeFile = relativeFile;
            this.changeType = changeType;
        }

        public String getParentPath() {
            return parentPath.toString();
        }
    }
}
