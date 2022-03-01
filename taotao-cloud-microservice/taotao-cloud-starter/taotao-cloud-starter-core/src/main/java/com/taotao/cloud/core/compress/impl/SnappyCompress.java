package com.taotao.cloud.core.compress.impl;

import com.taotao.cloud.core.compress.Compress;
import com.taotao.cloud.core.compress.support.SPI;
import java.io.IOException;

import org.xerial.snappy.Snappy;

/**
 * The Data Compression Based on snappy.
 */
@SPI("snappy")
public class SnappyCompress implements Compress {

	@Override
	public byte[] compress(byte[] data) throws IOException {
		return Snappy.compress(data);
	}

	@Override
	public byte[] uncompress(byte[] data) throws IOException {
		return Snappy.uncompress(data);
	}

}
