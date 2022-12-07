package com.taotao.cloud.data.sync.batch;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : JCccc
 * @CreateTime : 2020/3/17
 * @Description :
 **/
@RestController
public class TestController {

	@Autowired
	JobLauncher jobLauncher;

	//@Autowired
	//Job myJob;
	//
	//@GetMapping("testJob")
	//public void testJob()
	//	throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
	//	//    后置参数：使用JobParameters中绑定参数 addLong  addString 等方法
	//	JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
	//	jobLauncher.run(myJob, jobParameters);
	//
	//}

	//@Autowired
	//Job mybatisJob;
	//
	//@GetMapping("testJobNew")
	//public void testJobNew(@RequestParam("authorId") String authorId)
	//	throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
	//	JobParameters jobParametersNew = new JobParametersBuilder()
	//		.addLong("timeNew", System.currentTimeMillis())
	//		.addString("authorId", authorId)
	//		.toJobParameters();
	//	jobLauncher.run(mybatisJob, jobParametersNew);
	//
	//}

}
