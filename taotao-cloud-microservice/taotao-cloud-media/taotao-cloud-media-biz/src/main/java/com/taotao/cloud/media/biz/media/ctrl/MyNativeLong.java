package com.taotao.cloud.media.biz.media.ctrl;

import com.sun.jna.NativeLong;
import java.util.Date;

/**
 * 自定义登录句柄保存对象
 * 
 *
 */
public class MyNativeLong {
	
	/**
	 * 用户句柄
	 */
	private NativeLong lUserID;
	
	/**
	 * 预览句柄
	 */
	private NativeLong lRealHandle;
	
	/**
	 * 通道句柄
	 */
	private NativeLong lChannel;
	
	/**
	 * 最后一次使用时间
	 */
	private Date lastUse;
	
	/**
	 * 是否正在使用
	 */
	private boolean isUse;
	
	/**
	 * 开始控制摄像头,设置使用时间,正在使用中
	 */
	public void start(){
		this.lastUse = new Date();
		this.isUse = true;
	}
	
	/**
	 * 停止控制摄像头,已经不再使用
	 */
	public void down(){
		this.isUse = false;
	}

	public NativeLong getlRealHandle() {
		return lRealHandle;
	}

	public void setlRealHandle(NativeLong lRealHandle) {
		this.lRealHandle = lRealHandle;
	}

	public NativeLong getlUserID() {
		return lUserID;
	}

	public void setlUserID(NativeLong lUserID) {
		this.lUserID = lUserID;
	}

	public NativeLong getlChannel() {
		return lChannel;
	}

	public void setlChannel(NativeLong lChannel) {
		this.lChannel = lChannel;
	}

	public Date getLastUse() {
		return lastUse;
	}

	public void setLastUse(Date lastUse) {
		this.lastUse = lastUse;
	}

	public boolean isUse() {
		return isUse;
	}

	public void setUse(boolean isUse) {
		this.isUse = isUse;
	}
	
}
