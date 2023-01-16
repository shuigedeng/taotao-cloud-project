package com.taotao.cloud.workflow.api.common.util.file;


/**
 * 导入导出工厂类
 *
 */
public interface FileExport {

    /**
     * 导出
     *
     * @param obj       要转成Json的类
     * @param filePath  写入位置
     * @param fileName  文件名
     * @param tableName 表明
     * @return
     */
    DownloadVO exportFile(Object obj, String filePath, String fileName, String tableName);

}
