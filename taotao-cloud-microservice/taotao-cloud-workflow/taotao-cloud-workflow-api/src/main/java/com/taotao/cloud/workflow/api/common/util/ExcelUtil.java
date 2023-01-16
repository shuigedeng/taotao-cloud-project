package com.taotao.cloud.workflow.api.common.util;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import lombok.Cleanup;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 */
public class ExcelUtil {

    /**
     * excel转成实体
     * @param filePath 路径
     * @param titleRows 行
     * @param headerRows 列
     * @param pojoClass 实体
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass){
        if (StringUtil.isBlank(filePath)){
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * excel转成实体
     * @param file 文件
     * @param titleRows 行
     * @param headerRows 列
     * @param pojoClass 实体
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(File file, Integer titleRows, Integer headerRows, Class<T> pojoClass){
        if (file == null){
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file, pojoClass, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * excel转成实体
     * @param file 文件
     * @param titleRows 行
     * @param headerRows 列
     * @param pojoClass 实体
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass){
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            @Cleanup InputStream inputStream = file.getInputStream();
            list = ExcelImportUtil.importExcel(inputStream, pojoClass, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
