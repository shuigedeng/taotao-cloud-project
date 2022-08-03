package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.git.dtos;

import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.RelativeFile;
import lombok.Data;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件变更列表
 */
@Data
public class DiffChanges {
    /**
     * 提交记录列表
     */
    private List<String> commitIds = new ArrayList<>();
    /**
     * 修改的文件列表
     */
    private List<DiffFile> changeFiles = new ArrayList<>();

    public DiffChanges() {
    }

    public DiffChanges(List<String> commitIds) {
        this.commitIds = commitIds;
    }

    public void addChangeFile(DiffFile diffFile){
        this.changeFiles.add(diffFile);
    }

    @Data
    public static class DiffFile{
        private DiffEntry.ChangeType changeType;

        /**
         * 相对文件信息, 相对于仓库路径
         */
        private RelativeFile relativeFile;

        public DiffFile() {
        }

        public DiffFile(DiffEntry.ChangeType changeType, RelativeFile relativeFile) {
            this.changeType = changeType;
            this.relativeFile = relativeFile;
        }

        public String path(){
            return relativeFile.getPath();
        }
    }
}
