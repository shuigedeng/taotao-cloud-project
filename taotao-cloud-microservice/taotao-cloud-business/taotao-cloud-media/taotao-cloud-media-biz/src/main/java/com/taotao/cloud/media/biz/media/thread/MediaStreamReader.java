package com.taotao.cloud.media.biz.media.thread;

import java.io.InputStream;
import org.bytedeco.javacv.FFmpegFrameGrabber;


/**
 * 提供管道流接入
 */
public class MediaStreamReader {

	private InputStream in;

	public MediaStreamReader(InputStream in) {
		super();
		this.in = in;
	}

	public void init() {
		FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(in);

		/**
		 * 待完善
		 */
	}


	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}
}
