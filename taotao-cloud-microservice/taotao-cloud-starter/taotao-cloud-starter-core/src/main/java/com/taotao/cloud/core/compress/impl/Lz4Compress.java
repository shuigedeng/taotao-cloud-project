package com.taotao.cloud.core.compress.impl;

import com.taotao.cloud.core.compress.Compress;
import com.taotao.cloud.core.compress.support.SPI;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

/**
 * The Data Compression Based on lz4.
 */
@SPI("lz4")
public class Lz4Compress implements Compress {

	@Override
	public byte[] compress(byte[] data) throws IOException {
		LZ4Factory factory = LZ4Factory.fastestInstance();
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		LZ4Compressor compressor = factory.fastCompressor();
		LZ4BlockOutputStream compressedOutput = new LZ4BlockOutputStream(byteOutput, 2048, compressor);
		compressedOutput.write(data);
		compressedOutput.close();
		
		return byteOutput.toByteArray();
	}

	@Override
	public byte[] uncompress(byte[] data) throws IOException {
		LZ4Factory factory = LZ4Factory.fastestInstance();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		LZ4FastDecompressor decompresser = factory.fastDecompressor();
		LZ4BlockInputStream lzis = new LZ4BlockInputStream(new ByteArrayInputStream(data), decompresser);
		
		int count;
		byte[] buffer = new byte[2048];
		while ((count = lzis.read(buffer)) != -1) {
			baos.write(buffer, 0, count);
		}
		lzis.close();
		
		return baos.toByteArray();
	}

}
