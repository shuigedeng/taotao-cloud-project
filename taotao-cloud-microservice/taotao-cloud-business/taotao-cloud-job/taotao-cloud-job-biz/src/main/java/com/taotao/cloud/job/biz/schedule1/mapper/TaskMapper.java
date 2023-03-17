package com.taotao.cloud.job.biz.schedule1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.job.biz.schedule1.model.Task;
import com.taotao.cloud.job.biz.schedule1.model.TaskLog;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {

	List<Task> taskList();

	Task selectTaskById(String id);

	@Override
	int insert(Task task);

	int update(Task task);

	int updateVersion(@Param("task") Task task, @Param("version") Integer version);

	int deleteTask(String id);

	void insertTaskLog(TaskLog log);

}
