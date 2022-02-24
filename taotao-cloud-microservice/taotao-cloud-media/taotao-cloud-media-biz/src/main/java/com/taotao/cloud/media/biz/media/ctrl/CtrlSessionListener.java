package com.taotao.cloud.media.biz.media.ctrl;

import cn.hutool.core.convert.Convert;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * session监听，用于自动断开云台控制会话
 * 
 *
 */
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
		if(ip != null) {
			MyNativeLong nativeLong = TempData.getTempData().getNativeLong(ip);
			boolean net_DVR_Logout = LoginPlay.hCNetSDK.NET_DVR_Logout(nativeLong.getlUserID());
			if (net_DVR_Logout) {
				//退出登入成功
				TempData.getTempData().removeNativeLong(ip);
			}
		}
		
	}
	
}
