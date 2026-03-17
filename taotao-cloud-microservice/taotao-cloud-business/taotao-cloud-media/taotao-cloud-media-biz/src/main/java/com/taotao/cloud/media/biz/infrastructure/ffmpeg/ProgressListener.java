package com.taotao.cloud.media.biz.infrastructure.ffmpeg;

import java.io.File;

public interface ProgressListener {
    void onProgress(double percentage, String message);
    void onComplete( File outputFile);
    void onError(String error);
}

