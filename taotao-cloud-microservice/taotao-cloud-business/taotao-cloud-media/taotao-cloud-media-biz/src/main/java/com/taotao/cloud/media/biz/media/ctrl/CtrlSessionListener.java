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

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

/** session监听，用于自动断开云台控制会话 */
@WebListener
public class CtrlSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSessionListener.super.sessionCreated(event);
        HttpSession session = event.getSession();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // TODO Auto-generated method stub
        HttpSessionListener.super.sessionDestroyed(event);

        HttpSession session = event.getSession();

        String ip = Convert.toStr(session.getAttribute("ip"));
        if (ip != null) {
            MyNativeLong nativeLong = TempData.getTempData().getNativeLong(ip);
            boolean net_DVR_Logout = LoginPlay.hCNetSDK.NET_DVR_Logout(nativeLong.getlUserID());
            if (net_DVR_Logout) {
                // 退出登入成功
                TempData.getTempData().removeNativeLong(ip);
            }
        }
    }
}
