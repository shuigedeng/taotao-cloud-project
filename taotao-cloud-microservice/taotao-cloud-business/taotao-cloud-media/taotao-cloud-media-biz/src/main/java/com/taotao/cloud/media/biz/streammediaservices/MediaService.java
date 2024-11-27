package com.taotao.cloud.media.biz.streammediaservices;

import com.taotao.cloud.media.biz.media.common.ClientType;

/**
 * 媒体服务
 *
 * @author LGC
 */
@Component
@RequiredArgsConstructor
public class MediaService {

    private final ThreadPoolExecutor threadPoolExecutor;

    /**
     * 缓存流转换线程
     */
    public static ConcurrentHashMap<String, MediaTransfer> mediaTransferMap = new ConcurrentHashMap<>();


    /**
     * http-flv播放
     */
    public void playForHttp(CameraDTO cameraDto, ChannelHandlerContext ctx) {
        if (mediaTransferMap.containsKey(cameraDto.getMediaKey())) {
            MediaTransfer mediaConvert = mediaTransferMap.get(cameraDto.getMediaKey());
            if (mediaConvert instanceof MediaTransferFlvByJavacv) {
                MediaTransferFlvByJavacv mediaTransferFlvByJavacv = (MediaTransferFlvByJavacv) mediaConvert;
                mediaTransferFlvByJavacv.addClient(ctx, ClientType.HTTP);
            }
        } else {
            MediaTransferFlvByJavacv mediaConvert = new MediaTransferFlvByJavacv(cameraDto);
            mediaTransferMap.put(cameraDto.getMediaKey(), mediaConvert);
            threadPoolExecutor.execute(mediaConvert);
            mediaConvert.addClient(ctx, ClientType.HTTP);
        }
    }

    /**
     * http-flv 关闭播放
     */
    public void closeForHttp(CameraDTO cameraDto, ChannelHandlerContext ctx) {
        if (mediaTransferMap.containsKey(cameraDto.getMediaKey())) {
            MediaTransfer mediaConvert = mediaTransferMap.get(cameraDto.getMediaKey());
            if (mediaConvert instanceof MediaTransferFlvByJavacv) {
                MediaTransferFlvByJavacv mediaTransferFlvByJavacv = (MediaTransferFlvByJavacv) mediaConvert;
                mediaTransferFlvByJavacv.removeClient(ctx, ClientType.HTTP);
            }
        }
    }


    /**
     * ws-flv播放
     */
    public void playForWs(CameraDTO cameraDto, ChannelHandlerContext ctx) {
        if (mediaTransferMap.containsKey(cameraDto.getMediaKey())) {
            MediaTransfer mediaConvert = mediaTransferMap.get(cameraDto.getMediaKey());
            if (mediaConvert instanceof MediaTransferFlvByJavacv) {
                MediaTransferFlvByJavacv mediaTransferFlvByJavacv = (MediaTransferFlvByJavacv) mediaConvert;
                mediaTransferFlvByJavacv.addClient(ctx, ClientType.WEBSOCKET);
            }
        } else {
            MediaTransferFlvByJavacv mediaConvert = new MediaTransferFlvByJavacv(cameraDto);
            mediaTransferMap.put(cameraDto.getMediaKey(), mediaConvert);
            threadPoolExecutor.execute(mediaConvert);
            mediaConvert.addClient(ctx, ClientType.WEBSOCKET);
        }
    }

    /**
     * http-ws 关闭播放
     */
    public void closeForWs(CameraDTO cameraDto, ChannelHandlerContext ctx) {
        if (mediaTransferMap.containsKey(cameraDto.getMediaKey())) {
            MediaTransfer mediaConvert = mediaTransferMap.get(cameraDto.getMediaKey());
            if (mediaConvert instanceof MediaTransferFlvByJavacv) {
                MediaTransferFlvByJavacv mediaTransferFlvByJavacv = (MediaTransferFlvByJavacv) mediaConvert;
                mediaTransferFlvByJavacv.removeClient(ctx, ClientType.WEBSOCKET);
            }
        }
    }
}
