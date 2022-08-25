package com.taotao.cloud.oss.common.storage.aspect;


import com.taotao.cloud.oss.common.storage.FileInfo;
import com.taotao.cloud.oss.common.storage.platform.FileStorage;

/**
 * 文件是否存在切面调用链结束回调
 */
public interface ExistsAspectChainCallback {
    boolean run(FileInfo fileInfo, FileStorage fileStorage);
}
