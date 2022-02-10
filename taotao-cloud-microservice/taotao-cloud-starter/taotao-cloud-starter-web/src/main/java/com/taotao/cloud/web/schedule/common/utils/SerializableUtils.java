package com.taotao.cloud.web.schedule.common.utils;


public class SerializableUtils {

    //public static void toFile(Object obj, String fileSrc, String fileName) {
    //    fileSrc = existFile(fileSrc);
    //    fileSrc = fileSrc + File.separator + fileName;
    //    try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileSrc))) {
    //        os.writeObject(obj);
    //        os.flush();
    //    } catch (IOException ex) {
    //        ex.printStackTrace();
    //    }
    //}

    //public static void toIncFile(Object obj, String fileSrc, String fileName) {
    //    fileSrc = existFile(fileSrc);
    //    File file = new File(fileSrc + File.separator + fileName);
    //    IncObjectOutputStream.file = file;
    //    try (IncObjectOutputStream os = new IncObjectOutputStream(file)) {
    //        os.writeObject(obj);
    //        os.flush();
    //    } catch (IOException ex) {
    //        ex.printStackTrace();
    //    }
    //}

    /**
     * 反序列化对象
     * @param fileSrc 文件夹路径
     * @param fileName 文件名
     */
    //public  static <T> List<T> fromIncFile(String fileSrc, String fileName) {
    //    List<T> list = new ArrayList<>();
    //    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileSrc + File.separator + fileName))) {
    //        T obj = null;
    //        while ((obj = (T) ois.readObject()) != null){
    //            list.add(obj);
    //        }
    //    }catch (EOFException e){
    //        //文件读取结束，不需要处理
    //    }catch (IOException | ClassNotFoundException ex) {
    //        ex.printStackTrace();
    //    }
    //    return list;
    //}

    /**
     * 获取指定文件夹下的所有文件信息
     * @param fileSrc 文件夹路径
     */
    //public static List<ScheduledLogFile> getScheduledLogFiles(String fileSrc) {
    //    File file = new File(fileSrc);
    //    if (file.isDirectory()) {
    //        // 获取路径下的所有文件
    //        File[] files = file.listFiles();
    //        List<ScheduledLogFile> logFiles = new ArrayList<>();
    //        for (int i = 0; i < files.length; i++) {
    //            // 如果还是文件夹 递归获取里面的文件 文件夹
    //            if (!files[i].isDirectory()) {
    //                ScheduledLogFile scheduledLogFile = new ScheduledLogFile(files[i]);
    //                logFiles.add(scheduledLogFile);
    //            }
    //        }
    //        return logFiles;
    //    } else {
    //        throw new ScheduledException("路径："+fileSrc+"不是文件夹");
    //    }
    //}

    /**
     * 判断文件夹是否存在，如果不存在就创建
     * @param fileSrc 文件夹路径
     */
    //private static String existFile(String fileSrc) {
    //    if (StringUtils.isEmpty(fileSrc)) {
    //        fileSrc = "";
    //    } else {
    //        File file = new File(fileSrc);
    //        //如果文件夹不存在
    //        if (!file.exists()) {
    //            //创建文件夹
    //            file.mkdir();
    //        }
    //    }
    //    return fileSrc;
    //}
}
