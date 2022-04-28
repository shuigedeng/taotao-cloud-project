package com.taotao.cloud.ip2region.impl;


import com.taotao.cloud.ip2region.model.DBReader;

import java.io.IOException;

/**
 * 字节数组dbreader
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:28:33
 */
public class ByteArrayDBReader implements DBReader {

	protected byte[] buf;

	protected long pos;

	public ByteArrayDBReader(byte[] buf) {
		this.buf = buf;
	}

	@Override
	public byte[] full() throws IOException {
		return buf;
	}

	@Override
	public void readFully(long pos, byte[] buf, int offset, int length) throws IOException {
		System.arraycopy(this.buf, (int) pos, buf, offset, length);
	}

	@Override
	public void close() throws IOException {
		// nop
	}

}
