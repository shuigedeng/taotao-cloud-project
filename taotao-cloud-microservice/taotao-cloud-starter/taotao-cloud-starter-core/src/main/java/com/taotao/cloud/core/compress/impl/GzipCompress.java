package com.taotao.cloud.core.compress.impl;

import com.taotao.cloud.core.compress.Compress;
import com.taotao.cloud.core.compress.support.SPI;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * The Data Compression Based on gzip.
 */
@SPI("gzip")
public class GzipCompress implements Compress {

	@Override
	public byte[] compress(byte[] data) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;

		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(data);
			gzip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return out.toByteArray();
	}

	@Override
	public byte[] uncompress(byte[] data) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(data);

		try {
			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[2048];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return out.toByteArray();
	}
}
