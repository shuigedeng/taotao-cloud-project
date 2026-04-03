package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysFileMetadata;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 文件元数据Mapper
 */
@Mapper
public interface SysFileMetadataMapper extends BaseMapper<SysFileMetadata> {
    
    /**
     * 增加下载次数
     */
    @Update("UPDATE sys_file_metadata SET download_count = download_count + 1 WHERE file_id = #{fileId}")
    void incrementDownloadCount(String fileId);
}
