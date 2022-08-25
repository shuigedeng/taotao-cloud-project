package com.taotao.cloud.oss.common.storage.aspect;


import com.taotao.cloud.oss.common.storage.FileInfo;
import com.taotao.cloud.oss.common.storage.platform.FileStorage;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 下载缩略图切面调用链结束回调
 */
public interface DownloadThAspectChainCallback {
    void run(FileInfo fileInfo, FileStorage fileStorage,Consumer<InputStream> consumer);
}
