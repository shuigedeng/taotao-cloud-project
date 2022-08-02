package com.taotao.cloud.sys.biz.modules.core.service.file;

import com.taotao.cloud.sys.biz.modules.core.utils.OnlyPath;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Tailf {
    /**
     * 文件读取时间间隔配置, 1s
     */
    private long updateInterval = 1000;

    private static final MultiValueMap<OnlyPath,InnerTail> innerTailMultiValueMap = new LinkedMultiValueMap<>();

    private static final ThreadPoolExecutor threadPool =new ThreadPoolExecutor(1,5,0, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100),new NamedThreadFactory("tailPool"));

    public Tailf() {
        new AutoCloseThread().start();
    }

    /**
     * 开启一个文件查看实例
     */
    public InnerTail startTail(File file) throws IOException {
        final InnerTail innerTail = new InnerTail(file);
        innerTailMultiValueMap.add(new OnlyPath(file),innerTail);
        threadPool.submit(innerTail);
        return innerTail;
    }

    /**
     * 停止文件滚动查看
     * @param innerTail
     */
    public void stopTail(InnerTail innerTail) throws InterruptedException {
        innerTail.setKeepRunning(false);
    }

    /**
     * 在多久后停止
     * @param innerTail
     * @param time
     * @param timeUnit
     */
    public void stopTail(InnerTail innerTail,long time,TimeUnit timeUnit){
        try {
            timeUnit.sleep(time);

            innerTail.setKeepRunning(false);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    /**
     * 行数据更新
     */
    public static interface LineUpdateListener {
        void update(String line);
    }

    public final class InnerTail implements Runnable{
        private File file;
        private RandomAccessFile randomAccessFile;
        private long pointer;
        private volatile boolean keepRunning = true;
        private List<LineUpdateListener> listeners = new ArrayList<>();
        private Semaphore semaphore = new Semaphore(0);
        /**
         * 上次文件读取时间
         */
        private long lastReadTime = System.currentTimeMillis();

        public InnerTail(File file) throws FileNotFoundException {
            // 默认从尾部开始读取
            init(file,file.length());
        }

        private void init(File file,long pointer) throws FileNotFoundException {
            this.file = file;
            randomAccessFile = new RandomAccessFile(file,"r");
            this.pointer = pointer;
        }


        public InnerTail(File file,int lineNum) throws IOException {
            if (lineNum == 0){
                init(file,0);
                return;
            }
            if (lineNum < 0){
                // 负数表示从尾部开始读取
                init(file,file.length());
                return;
            }

            // 正数表示从某行开始读取
            final RandomAccessFile r = new RandomAccessFile(file, "r");
            String line = null;
            int lineIndex = 0;
            while ((line = r.readLine()) != null){
                if (lineIndex++ >= lineNum){
                    break;
                }
            }
            init(file,r.getFilePointer());
            r.close();
        }

        public void register(LineUpdateListener lineUpdateListener){
            listeners.add(lineUpdateListener);
        }

        @SneakyThrows
        @Override
        public void run() {
            while (keepRunning){
                try {
                    Thread.sleep(updateInterval);

                    if (pointer > file.length()){
                        log.error("文件只能追加, 现在遇到指针大于文件字段数量");
                        break;
                    }

                    randomAccessFile.seek(pointer);
                    String line = null;
                    while ((line = randomAccessFile.readLine()) != null) {
                        lastReadTime = System.currentTimeMillis();
                        String lineEncode = new String(line.getBytes("ISO-8859-1"),"utf-8");
                        for (LineUpdateListener listener : listeners) {
                            try {
                                listener.update(lineEncode);
                            }catch (Exception e){
                                log.error(e.getMessage(),e);
                            }
                        }
                    }
                    pointer = randomAccessFile.getFilePointer();
                } catch (InterruptedException | IOException e) {
                    // ignore
                }

            }

            log.info("关闭文件: {}",file);
            randomAccessFile.close();

            semaphore.release();
        }

        void setKeepRunning(boolean keepRunning) throws InterruptedException {
            this.keepRunning = keepRunning;

            semaphore.acquire();
        }
    }

    /**
     * 定时清理任务, 用于清理掉可能没有关闭的 tail 任务
     */
    public final class AutoCloseThread extends Thread{
        /**
         * 最大空闲时间, 如果 10s 内文件没有变更,则停止监听线程
         */
        private static final long maxIdleTime = 10000;

        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(maxIdleTime);
                } catch (InterruptedException e) {
                    // ignore
                }

                final Iterator<List<InnerTail>> iterator = innerTailMultiValueMap.values().iterator();
                while (iterator.hasNext()){
                    final List<InnerTail> innerTails = iterator.next();
                    final Iterator<InnerTail> innerTailIterator = innerTails.iterator();
                    while (innerTailIterator.hasNext()){
                        final InnerTail innerTail = innerTailIterator.next();
                        if (System.currentTimeMillis() - innerTail.lastReadTime > maxIdleTime){
                            innerTailIterator.remove();
                        }
                    }

                    if (CollectionUtils.isEmpty(innerTails)){
                        iterator.remove();
                    }
                }
            }

        }
    }
}
