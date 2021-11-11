package com.taotao.cloud.xxl.job.admin.dao;

import com.taotao.cloud.xxl.job.admin.core.model.XxlJobLogGlue;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * job log for glue
 */
@Mapper
public interface XxlJobLogGlueDao {
	
	public int save(XxlJobLogGlue xxlJobLogGlue);
	
	public List<XxlJobLogGlue> findByJobId(@Param("jobId") int jobId);

	public int removeOld(@Param("jobId") int jobId, @Param("limit") int limit);

	public int deleteByJobId(@Param("jobId") int jobId);
	
}
