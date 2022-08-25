package com.taotao.cloud.oss.common.storage.aspect;

import com.taotao.cloud.oss.common.storage.FileInfo;
import com.taotao.cloud.oss.common.storage.platform.FileStorage;
import com.taotao.cloud.oss.common.storage.recorder.FileRecorder;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;

/**
 * 删除的切面调用链
 */
@Getter
@Setter
public class DeleteAspectChain {

    private DeleteAspectChainCallback callback;
    private Iterator<FileStorageAspect> aspectIterator;

    public DeleteAspectChain(Iterable<FileStorageAspect> aspects,DeleteAspectChainCallback callback) {
        this.aspectIterator = aspects.iterator();
        this.callback = callback;
    }

    /**
     * 调用下一个切面
     */
    public boolean next(FileInfo fileInfo, FileStorage fileStorage, FileRecorder fileRecorder) {
        if (aspectIterator.hasNext()) {//还有下一个
            return aspectIterator.next().deleteAround(this,fileInfo,fileStorage,fileRecorder);
        } else {
            return callback.run(fileInfo,fileStorage,fileRecorder);
        }
    }
}
