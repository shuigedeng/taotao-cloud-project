//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCodeKetama extends AbstractHashCode {
    private static MessageDigest md5Digest;

    public int doHash(String origin) {
        byte[] bKey = computeMd5(origin);
        long rv = (long)(bKey[3] & 255) << 24 | (long)(bKey[2] & 255) << 16 | (long)(bKey[1] & 255) << 8 | (long)(bKey[0] & 255);
        return (int)(rv & 4294967295L);
    }

    private static byte[] computeMd5(String k) {
        MessageDigest md5;
        try {
            md5 = (MessageDigest)md5Digest.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("clone of MD5 not supported", e);
        }

        md5.update(k.getBytes());
        return md5.digest();
    }

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }
}
