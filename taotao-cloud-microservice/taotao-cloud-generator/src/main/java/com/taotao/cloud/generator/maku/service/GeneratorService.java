package com.taotao.cloud.generator.maku.service;

import com.taotao.cloud.generator.maku.vo.PreviewVO;

import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface GeneratorService {

    void downloadCode(Long tableId, ZipOutputStream zip);

    void generatorCode(Long tableId);

    List<PreviewVO> preview(Long tableId);
}
