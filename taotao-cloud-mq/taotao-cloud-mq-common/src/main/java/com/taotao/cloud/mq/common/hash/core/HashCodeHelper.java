//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;


public final class HashCodeHelper {
    private HashCodeHelper() {
    }

    public static int jdk(String text) {
        return HasheCodes.jdk().hash(text);
    }

    public static int crc(String text) {
        return HasheCodes.crc().hash(text);
    }

    public static int fnv(String text) {
        return HasheCodes.fnv().hash(text);
    }

    public static int ketama(String text) {
        return HasheCodes.ketama().hash(text);
    }

    public static int murmur(String text) {
        return HasheCodes.murmur().hash(text);
    }
}
