package com.taotao.cloud.canal.client.interfaces;

/**
 * canal 客户端接口层
 *
 * @author 阿导
 * @CopyRight 萬物皆導
 * @created 2018/5/28 14:10
 * @Modified_By 阿导 2018/5/28 14:10
 */


public interface CanalClient {

    /**
     * 开启 canal 客户端，并根绝配置连接到 canal ,然后进行针对性的监听
     *
     * @author 阿导
     * @time 2018/5/28 14:10
     * @CopyRight 万物皆导
     * @param
     * @return
     */
    void start();
    
    
    /**
     * 关闭 canal 客户端
     *
     * @author 阿导
     * @time 2018/5/28 14:12
     * @CopyRight 万物皆导
     * @param
     * @return
     */
    void stop();
    
    /**
     * 判断 canal 客户端是否是开启状态
     *
     * @author 阿导
     * @time 2018/5/28 14:12
     * @CopyRight 万物皆导
     * @param
     * @return
     */
    boolean isRunning();
}
