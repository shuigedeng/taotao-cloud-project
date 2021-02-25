package com.taotao.cloud.java.concurrent.serializable;

import java.io.Serializable;
import java.util.Date;

public class Task implements Runnable, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void run() {
        System.out.println(new Date());
    }

}
