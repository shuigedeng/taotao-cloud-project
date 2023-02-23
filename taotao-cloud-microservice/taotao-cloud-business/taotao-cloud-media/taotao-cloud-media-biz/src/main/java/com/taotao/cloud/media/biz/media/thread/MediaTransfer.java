package com.taotao.cloud.media.biz.media.thread;

import io.netty.channel.ChannelHandlerContext;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegLogCallback;

/**
 * 媒体转换者
 */
public abstract class MediaTransfer {

	static {
		avutil.av_log_set_level(avutil.AV_LOG_ERROR);
		FFmpegLogCallback.set();
	}

	/**
	 * ws客户端
	 */
	public ConcurrentHashMap<String, ChannelHandlerContext> wsClients = new ConcurrentHashMap<>();

	/**
	 * http客户端
	 */
	public ConcurrentHashMap<String, ChannelHandlerContext> httpClients = new ConcurrentHashMap<>();

	/**
	 * 当前在线人数
	 */
	public int hcSize, wcSize = 0;

	/**
	 * 用于没有客户端时候的计时
	 */
	public int noClient = 0;

	/**
	 * flv header
	 */
	public byte[] header = null;

	/**
	 * 输出流，视频最终会输出到此
	 */
	public ByteArrayOutputStream bos = new ByteArrayOutputStream();

	/**
	 * 转码回调
	 */
	public TransferCallback transferCallback;

	public ConcurrentHashMap<String, ChannelHandlerContext> getWsClients() {
		return wsClients;
	}

	public void setWsClients(
		ConcurrentHashMap<String, ChannelHandlerContext> wsClients) {
		this.wsClients = wsClients;
	}

	public ConcurrentHashMap<String, ChannelHandlerContext> getHttpClients() {
		return httpClients;
	}

	public void setHttpClients(
		ConcurrentHashMap<String, ChannelHandlerContext> httpClients) {
		this.httpClients = httpClients;
	}

	public int getHcSize() {
		return hcSize;
	}

	public void setHcSize(int hcSize) {
		this.hcSize = hcSize;
	}

	public int getWcSize() {
		return wcSize;
	}

	public void setWcSize(int wcSize) {
		this.wcSize = wcSize;
	}

	public int getNoClient() {
		return noClient;
	}

	public void setNoClient(int noClient) {
		this.noClient = noClient;
	}

	public byte[] getHeader() {
		return header;
	}

	public void setHeader(byte[] header) {
		this.header = header;
	}

	public ByteArrayOutputStream getBos() {
		return bos;
	}

	public void setBos(ByteArrayOutputStream bos) {
		this.bos = bos;
	}

	public TransferCallback getTransferCallback() {
		return transferCallback;
	}

	public void setTransferCallback(
		TransferCallback transferCallback) {
		this.transferCallback = transferCallback;
	}
	//	public void addClient() {
//		
//	}
}
