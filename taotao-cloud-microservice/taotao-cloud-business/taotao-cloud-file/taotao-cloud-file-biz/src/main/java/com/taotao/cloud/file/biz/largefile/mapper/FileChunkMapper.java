package com.taotao.cloud.file.biz.largefile.mapper;

import java.util.List;

public interface FileChunkMapper {

    /**
     * 通过 md5 查询记录
     *
     * @param md5 md5
     * @return
     */
    List<FileChunk> listByMd5(String md5);

    /**
     * 批量新增记录
     *
     * @param fileChunkList fileChunkList
     * @return
     */
    int batchInsert(List<FileChunk> fileChunkList);

    /**
     * 删除记录
     *
     * @param fileChunk fileChunk
     * @return
     */

    int delete(FileChunk fileChunk);

    FileChunk getById(Long id);

    int insert(FileChunk fileChunk);

    int update(FileChunk fileChunk);
}
