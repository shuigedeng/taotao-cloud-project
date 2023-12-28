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

package com.taotao.cloud.media.biz.media.controller;

import com.taotao.cloud.media.biz.media.ctrl.CameraCtrl;
import com.taotao.cloud.media.biz.media.ctrl.CloudCode;
import com.taotao.cloud.media.biz.media.ctrl.Control;
import com.taotao.cloud.media.biz.media.ctrl.LoginPlay;
import com.taotao.cloud.media.biz.media.ctrl.MyNativeLong;
import com.taotao.cloud.media.biz.media.ctrl.TempData;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 云台控制 */
@RequestMapping("hk")
@RestController
public class CloudCtrlController {

    //	http://localhost:8888/hk/ctrl?ip=192.168.2.120&op=left&username=admin&password=VZCDOY

    /**
     * 云台控制接口
     *
     * @param session
     * @param camera
     */
    @RequestMapping("ctrl")
    public void cloudCtrl(HttpSession session, CameraCtrl camera) {
        checkLogin(camera);
        session.setAttribute("ip", camera.getIp());

        // 截取摄像机实时图片
        //		boolean imgSavePath = Control.getImgSavePath(camera.getIp(), "D:\\tempFile\\3.jpg");

        if (StrUtil.equals(camera.getOp(), "up")) {
            // 制摄像机云台控制(开启)
            Control.cloudControl(camera.getIp(), CloudCode.TILT_UP, CloudCode.SPEED_LV6, CloudCode.START);
            try {
                // 让云台运行1000ms
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            // 制摄像机云台控制(关闭)
            Control.cloudControl(camera.getIp(), CloudCode.TILT_UP, CloudCode.SPEED_LV6, CloudCode.END);
        } else if (StrUtil.equals(camera.getOp(), "down")) {
            // 制摄像机云台控制(开启)
            Control.cloudControl(camera.getIp(), CloudCode.TILT_DOWN, CloudCode.SPEED_LV6, CloudCode.START);
            try {
                // 让云台运行1000ms
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            // 制摄像机云台控制(关闭)
            Control.cloudControl(camera.getIp(), CloudCode.TILT_DOWN, CloudCode.SPEED_LV6, CloudCode.END);
        } else if (StrUtil.equals(camera.getOp(), "left")) {
            // 制摄像机云台控制(开启)
            Control.cloudControl(camera.getIp(), CloudCode.PAN_LEFT, CloudCode.SPEED_LV6, CloudCode.START);
            try {
                // 让云台运行1000ms
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            // 制摄像机云台控制(关闭)
            Control.cloudControl(camera.getIp(), CloudCode.PAN_LEFT, CloudCode.SPEED_LV6, CloudCode.END);
        } else if (StrUtil.equals(camera.getOp(), "right")) {
            // 制摄像机云台控制(开启)
            Control.cloudControl(camera.getIp(), CloudCode.PAN_RIGHT, CloudCode.SPEED_LV6, CloudCode.START);
            try {
                // 让云台运行1000ms
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            // 制摄像机云台控制(关闭)
            Control.cloudControl(camera.getIp(), CloudCode.PAN_RIGHT, CloudCode.SPEED_LV6, CloudCode.END);
        } else if (StrUtil.equals(camera.getOp(), "left_up")) {
            // 制摄像机云台控制(开启)
            Control.cloudControl(camera.getIp(), CloudCode.UP_LEFT, CloudCode.SPEED_LV6, CloudCode.START);
            try {
                // 让云台运行1000ms
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            // 制摄像机云台控制(关闭)
            Control.cloudControl(camera.getIp(), CloudCode.UP_LEFT, CloudCode.SPEED_LV6, CloudCode.END);
        } else if (StrUtil.equals(camera.getOp(), "left_down")) {
            // 制摄像机云台控制(开启)
            Control.cloudControl(camera.getIp(), CloudCode.DOWN_LEFT, CloudCode.SPEED_LV6, CloudCode.START);
            try {
                // 让云台运行1000ms
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            // 制摄像机云台控制(关闭)
            Control.cloudControl(camera.getIp(), CloudCode.DOWN_LEFT, CloudCode.SPEED_LV6, CloudCode.END);
        } else if (StrUtil.equals(camera.getOp(), "right_up")) {
            // 制摄像机云台控制(开启)
            Control.cloudControl(camera.getIp(), CloudCode.UP_RIGHT, CloudCode.SPEED_LV6, CloudCode.START);
            try {
                // 让云台运行1000ms
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            // 制摄像机云台控制(关闭)
            Control.cloudControl(camera.getIp(), CloudCode.UP_RIGHT, CloudCode.SPEED_LV6, CloudCode.END);
        } else if (StrUtil.equals(camera.getOp(), "right_down")) {
            // 制摄像机云台控制(开启)
            Control.cloudControl(camera.getIp(), CloudCode.DOWN_RIGHT, CloudCode.SPEED_LV6, CloudCode.START);
            try {
                // 让云台运行1000ms
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            // 制摄像机云台控制(关闭)
            Control.cloudControl(camera.getIp(), CloudCode.DOWN_RIGHT, CloudCode.SPEED_LV6, CloudCode.END);
        } else if (StrUtil.equals(camera.getOp(), "big")) {
            // 制摄像机云台控制(开启)
            Control.cloudControl(camera.getIp(), CloudCode.ZOOM_IN, CloudCode.SPEED_LV6, CloudCode.START);
            try {
                // 让云台运行1000ms
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            // 制摄像机云台控制(关闭)
            Control.cloudControl(camera.getIp(), CloudCode.ZOOM_IN, CloudCode.SPEED_LV6, CloudCode.END);
        } else if (StrUtil.equals(camera.getOp(), "small")) {
            // 制摄像机云台控制(开启)
            Control.cloudControl(camera.getIp(), CloudCode.ZOOM_OUT, CloudCode.SPEED_LV6, CloudCode.START);
            try {
                // 让云台运行1000ms
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            // 制摄像机云台控制(关闭)
            Control.cloudControl(camera.getIp(), CloudCode.ZOOM_OUT, CloudCode.SPEED_LV6, CloudCode.END);
        }
    }

    /** sdk登入 */
    private void checkLogin(CameraCtrl camera) {
        MyNativeLong nativeLong = TempData.getTempData().getNativeLong(camera.getIp());
        if (null == nativeLong) {
            LoginPlay lp = new LoginPlay();
            // 输入摄像机ip，端口，账户，密码登录
            try {
                boolean doLogin = lp.doLogin(
                        camera.getIp(),
                        Convert.toShort(camera.getPort(), (short) 8000),
                        camera.getUsername(),
                        camera.getPassword());
            } catch (Exception e) {
                LogUtils.error(e);
            }
        }
    }
}
