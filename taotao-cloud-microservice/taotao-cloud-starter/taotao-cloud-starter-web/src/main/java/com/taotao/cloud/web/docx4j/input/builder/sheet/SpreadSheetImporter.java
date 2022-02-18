package com.taotao.cloud.web.docx4j.input.builder.sheet;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 电子表格导入构建
 */
public class SpreadSheetImporter {
    /**
     * 电子表格信息
     */
    private final Workbook workbook;
    /**
     * 跳过行数
     */
    private int skip;
    /**
     * 是否快速失败
     */
    private boolean failFast;
    /**
     * sheet页
     */
    private int sheetIndex;

    SpreadSheetImporter(InputStream is, boolean closeable) {
        try {
            ZipSecureFile.setMinInflateRatio(0D);
            this.workbook = WorkbookFactory.create(is);
        } catch (IOException e) {
            throw new SpreadSheetImportException(e);
        } finally {
            if (closeable) {
                IOUtils.closeQuietly(is);
            }
        }

        // 默认解析第一个sheet
        this.sheetIndex = 0;
        // 默认非快速失败
        this.failFast = false;
        // 默认跳过第一行表头
        this.skip = 1;
    }

    /**
     * 通过输入流创建
     * @param is        {@link InputStream}
     * @param closeable 是否关闭输入流
     * @return {@link SpreadSheetImporter}
     */
    public static SpreadSheetImporter create(InputStream is, boolean closeable) {
        return new SpreadSheetImporter(is, closeable);
    }

    /**
     * 通过输入流创建
     * @param is {@link InputStream}
     * @return {@link SpreadSheetImporter}
     */
    public static SpreadSheetImporter create(InputStream is) {
        return SpreadSheetImporter.create(is, true);
    }

    /**
     * {@link MultipartFile}快速创建
     * @param file {@link MultipartFile}
     * @return {@link SpreadSheetImporter}
     */
    public static SpreadSheetImporter create(MultipartFile file) {
        try {
            return SpreadSheetImporter.create(file.getInputStream());
        } catch (IOException e) {
            throw new SpreadSheetImportException(e);
        }
    }

    /**
     * 指定解析表格的sheet索引
     * @param sheetIndex sheet索引
     * @return {@link SpreadSheetImporter}
     */
    public SpreadSheetImporter sheet(int sheetIndex) {
        this.sheetIndex = sheetIndex;
        return this;
    }

    /**
     * 设置是否快速失败
     * @param failFast true快速失败 false完全校验
     * @return {@link SpreadSheetImporter}
     */
    public SpreadSheetImporter failFast(boolean failFast) {
        this.failFast = failFast;
        return this;
    }

    /**
     * 表头行跳过行数
     * @param skip 跳过行数
     * @return {@link SpreadSheetImporter}
     */
    public SpreadSheetImporter skip(int skip) {
        this.skip = skip;
        return this;
    }

    /**
     * 将数据处理为给定类型
     * @param clazz 解析结果类型
     * @param <T>   解析结果类型
     * @return {@link ImportResult}
     */
    public <T> ImportResult<T> resolve(Class<T> clazz) {
        // sheet越界校验
        int sheets = this.workbook.getNumberOfSheets();
        if (this.sheetIndex >= sheets) {
            throw new SpreadSheetImportException("sheet index out of range");
        }

        try {
            return
                new SpreadSheetHandler<>(clazz, this.skip, this.failFast)
                    .handle(this.workbook.getSheetAt(this.sheetIndex))
                    .result();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new SpreadSheetImportException(e);
        }
    }
}
