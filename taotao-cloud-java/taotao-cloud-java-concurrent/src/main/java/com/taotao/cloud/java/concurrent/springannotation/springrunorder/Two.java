package com.taotao.cloud.java.concurrent.springannotation.springrunorder;

public class Two {
    public volatile String aString;

    public Two(String two) {
        System.out.println(two);
    }
}
