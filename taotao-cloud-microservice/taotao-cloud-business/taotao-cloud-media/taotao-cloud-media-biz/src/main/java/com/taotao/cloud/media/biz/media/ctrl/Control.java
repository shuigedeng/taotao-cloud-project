/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.media.biz.media.ctrl;

import com.sun.jna.NativeLong;

/** 摄像头控制 */
public class Control {

    private static HCNetSDK hCNetSDK = LoginPlay.hCNetSDK;

    /**
     * 截取摄像头实时图片并存储
     *
     * @param ip 摄像头ip
     * @param path 截取图片存储的路径
     * @return
     */
    public static boolean getImgSavePath(String ip, String path) {
        // 获取ip对应摄像头的句柄
        MyNativeLong nativeLong = TempData.getTempData().getNativeLong(ip);
        if (nativeLong == null) {
            return false;
        }

        // 用户句柄
        NativeLong lUserID = nativeLong.getlUserID();
        // 通道句柄
        NativeLong lChannel = nativeLong.getlChannel();
        // 创建截图质量对象
        HCNetSDK.NET_DVR_JPEGPARA net_d = new HCNetSDK.NET_DVR_JPEGPARA();

        boolean result = hCNetSDK.NET_DVR_CaptureJPEGPicture(lUserID, lChannel, net_d, path);
        return result;
    }

    /**
     * 云台控制<br>
     * 云台控制的方式为调用该方法摄像头便会一直执行该操作,直到该操作接收到"停止"指令及(iStop)参数
     *
     * @param ip 摄像头ip
     * @param iCommand 控制指令
     * @param iSpeed 云台运行速度
     * @param iStop 是否为停止操作
     * @return
     */
    public static boolean cloudControl(String ip, CloudCode iCommand, CloudCode iSpeed, CloudCode iStop) {
        // 获取ip对应摄像头的句柄
        MyNativeLong nativeLong = TempData.getTempData().getNativeLong(ip);
        if (nativeLong == null) {
            return false;
        }

        // 获取预览句柄
        NativeLong lRealHandle = nativeLong.getlRealHandle();
        if (lRealHandle.intValue() < 0) {
            return false;
        }

        // 判断是否为停止操作
        if (iSpeed.getKey() == 1) {
            return hCNetSDK.NET_DVR_PTZControl(lRealHandle, iCommand.getKey(), iStop.getKey());
        }

        return hCNetSDK.NET_DVR_PTZControlWithSpeed(lRealHandle, iCommand.getKey(), iStop.getKey(), iSpeed.getKey());
    }

    /**
     * 实时预览
     *
     * @param ip
     * @return
     */
    public static NativeLong realPlay(String ip) {
        // 获取ip对应摄像头的句柄
        MyNativeLong nativeLong = TempData.getTempData().getNativeLong(ip);
        if (nativeLong == null) {
            return null;
        }

        // 获取预览句柄
        NativeLong lRealHandle = nativeLong.getlRealHandle();
        if (lRealHandle.intValue() < 0) {
            return null;
        }

        return lRealHandle;
    }
}
