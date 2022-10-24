package com.taotao.cloud.job.schedule.task;

import com.taotao.cloud.job.schedule.model.entity.Task;
import com.taotao.cloud.job.schedule.model.entity.TaskLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {
	List<Task> taskList();

	Task selectTaskById(String id);

	int insert(Task task);

	int update(Task task);

	int updateVersion(@Param("task") Task task, @Param("version") Integer version);

	int deleteTask(String id);

	void insertTaskLog(TaskLog log);

}
