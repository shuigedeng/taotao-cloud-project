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
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

/** 登录到摄像头 */
public class LoginPlay {

    /** 全局HCNetSDK对象 */
    public static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;

    /** 设备参数信息 */
    private HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;

    /** 设备IP参数 */
    private HCNetSDK.NET_DVR_IPPARACFG m_strIpparaCfg;

    /** 用户参数 */
    private HCNetSDK.NET_DVR_CLIENTINFO m_strClientInfo;

    /** 用户句柄 */
    private NativeLong lUserID;

    /** 预览句柄 */
    private NativeLong lPreviewHandle;

    /** 登录到设备 */
    public boolean doLogin(String ip, short port, String username, String password) throws Exception {
        boolean initSuc = hCNetSDK.NET_DVR_Init();
        if (initSuc != true) {
            // hCNetSDK初始化失败
            throw new Exception("init HCNetSDK error");
        }

        m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        // 获取用户句柄
        lUserID = hCNetSDK.NET_DVR_Login_V30(ip, port, username, password, m_strDeviceInfo);

        long userID = lUserID.longValue();
        if (userID == -1) {
            // 登录失败
            throw new Exception("login error, check your username and password");
        }

        // 获取通道号
        int chan = getChannel();
        if (chan == -1) {
            // 没有获取到通道号
            throw new Exception("didn't get the channel");
        }

        // 获取设备预览句柄
        NativeLong lRealHandle = play(chan);
        // 获取设备通道句柄
        NativeLong lChannel = new NativeLong(chan);
        // 保存到缓存中, 下次不必再进行登录操作
        TempData.getTempData().setNativeLong(ip, lUserID, lRealHandle, lChannel);

        return true;
    }

    /**
     * 注销登录
     *
     * @return
     */
    public boolean doLogout() {
        boolean net_DVR_Logout = hCNetSDK.NET_DVR_Logout(lUserID);
        if (net_DVR_Logout) {
            return true;
        }
        return false;
    }

    /**
     * 获取设备通道 <br>
     * 不支持IP通道返回-1 <br>
     *
     * @return
     */
    private int getChannel() {
        // 获取IP接入配置参数
        IntByReference ibrBytesReturned = new IntByReference(0);
        boolean bRet = false;

        m_strIpparaCfg = new HCNetSDK.NET_DVR_IPPARACFG();
        m_strIpparaCfg.write();
        Pointer lpIpParaConfig = m_strIpparaCfg.getPointer();
        bRet = hCNetSDK.NET_DVR_GetDVRConfig(
                lUserID,
                HCNetSDK.NET_DVR_GET_IPPARACFG,
                new NativeLong(0),
                lpIpParaConfig,
                m_strIpparaCfg.size(),
                ibrBytesReturned);
        m_strIpparaCfg.read();

        // 设备是否支持IP通道, true为不支持
        if (!bRet) {
            for (int iChannum = 0; iChannum < m_strDeviceInfo.byChanNum; iChannum++) {
                LogUtils.info("通道号: " + iChannum + m_strDeviceInfo.byStartChan);
            }
            if (m_strDeviceInfo.byChanNum > 0) {
                return Integer.valueOf(0 + m_strDeviceInfo.byStartChan);
            }
        }

        return -1;
    }

    /**
     * 获取预览句柄
     *
     * @return
     */
    public NativeLong play(int chan) {

        m_strClientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();
        m_strClientInfo.lChannel = new NativeLong(chan);
        m_strClientInfo.lLinkMode = new NativeLong(0); // tcp取流
        //        m_strClientInfo.sMultiCastIP=null;              //不启动多播模式

        lPreviewHandle = hCNetSDK.NET_DVR_RealPlay_V30(lUserID, m_strClientInfo, null, null, true);
        return lPreviewHandle;
    }

    /**
     * 获取用户句柄
     *
     * @return
     */
    public NativeLong user() {
        return lUserID;
    }
}
