package com.taotao.cloud.java.serializable;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DesTask {
    /**
     * 从硬盘中反序列化对象
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("c://tasks"));
        ExecutorService pool = Executors.newCachedThreadPool();
        Task t = (Task) ois.readObject();
        pool.execute(t);
        ois.close();
        pool.shutdown();
    }

}
