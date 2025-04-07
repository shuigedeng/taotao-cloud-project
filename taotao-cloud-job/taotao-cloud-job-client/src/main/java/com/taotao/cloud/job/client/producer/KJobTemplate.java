package com.taotao.cloud.job.client.producer;

import com.taotao.cloud.job.client.producer.entity.JobDeleteReq;
import com.taotao.cloud.job.client.producer.entity.JobUpdateReq;
import com.taotao.cloud.job.client.producer.uid.IdGenerateService;
import com.taotao.cloud.job.common.enums.TimeExpressionType;
import com.taotao.cloud.job.common.module.LifeCycle;
import com.taotao.cloud.job.remote.protos.MqCausa;
import com.taotao.cloud.job.common.utils.JsonUtils;
import java.util.concurrent.atomic.AtomicInteger;

public class KJobTemplate {
	MessageSendClient messageSendClient;
	IdGenerateService idGenerateService;

	/**
	 * @param nameServerAddress 127.0.0.1:9083,127.0.0.2:9083...
	 */
	public KJobTemplate(String nameServerAddress) {
		messageSendClient = new MessageSendClient(nameServerAddress);
		idGenerateService = new IdGenerateService();
	}

	public Long createJob(JobUpdateReq JobUpdateReq) {
		// 生成jobId
		long jobId = idGenerateService.allocate();

		MqCausa.CreateJobReq build = MqCausa.CreateJobReq.newBuilder()
			.setJobId(jobId)
			.setAppName(JobUpdateReq.getAppName())
			.setJobName(JobUpdateReq.getJobName())
			.setJobParams(JobUpdateReq.getJobParams())
			.setJobDescription(JobUpdateReq.getJobDescription())
			.setProcessorInfo(JobUpdateReq.getProcessorInfo())
			.setTimeExpressionType(TimeExpressionType.getProtoBufTimeExpressionType(JobUpdateReq.getTimeExpressionType()))
			.setTimeExpression(JobUpdateReq.getTimeExpression())
			.setLifeCycle(JsonUtils.toJSONString(JobUpdateReq.getLifeCycle()))
			.setMaxInstanceNum(JobUpdateReq.getMaxInstanceNum()).build();
		MqCausa.Message build1 = MqCausa.Message.newBuilder().setCreateJobReq(build)
			.setRetryTime(2)
			.setMessageType(MqCausa.MessageType.JOB_CREATE)
			.build();
		messageSendClient.sendMessageAsync(new AtomicInteger(0), build1);

		return jobId;
	}

	public void updateJob(JobUpdateReq jobUpdateReq) {
		MqCausa.UpdateJobReq build = MqCausa.UpdateJobReq.newBuilder()
			.setJobId(jobUpdateReq.getJobId())
			.setAppName(jobUpdateReq.getAppName())
			.setJobName(jobUpdateReq.getJobName())
			.setJobParams(jobUpdateReq.getJobParams())
			.setJobDescription(jobUpdateReq.getJobDescription())
			.setProcessorInfo(jobUpdateReq.getProcessorInfo())
			.setTimeExpressionType(TimeExpressionType.getProtoBufTimeExpressionType(jobUpdateReq.getTimeExpressionType()))
			.setTimeExpression(jobUpdateReq.getTimeExpression())
			.setLifeCycle(JsonUtils.toJSONString(jobUpdateReq.getLifeCycle()))
			.setMaxInstanceNum(jobUpdateReq.getMaxInstanceNum()).build();
		MqCausa.Message build1 = MqCausa.Message.newBuilder().setUpdateJobReq(build)
			.setRetryTime(2)
			.setMessageType(MqCausa.MessageType.JOB_UPDATE)
			.build();
		messageSendClient.sendMessageAsync(new AtomicInteger(0), build1);
	}

	public void deleteJob(JobDeleteReq jobDeleteReq) {
		MqCausa.DeleteJobReq build = MqCausa.DeleteJobReq.newBuilder().setJobId(jobDeleteReq.getJobId()).build();
		MqCausa.Message build1 = MqCausa.Message.newBuilder().setRetryTime(2).setMessageType(MqCausa.MessageType.JOB_DELETE).setDeleteJobReq(build).build();
		messageSendClient.sendMessageAsync(new AtomicInteger(0), build1);
	}


	public static void main(String[] args) {
		KJobTemplate kJobTemplate = new KJobTemplate("127.0.0.1:9083");
		JobUpdateReq build = JobUpdateReq.builder()
			.appName("root")
			.jobDescription("hahah")
			.jobName("hahahaha")
			.lifeCycle(new LifeCycle())
			.processorInfo("testProcessor")
			.timeExpression("*/15 * * * * ?")
			.maxInstanceNum(5)
			.jobParams("ewew")
			.timeExpressionType(TimeExpressionType.CRON).build();
		kJobTemplate.createJob(build);
	}
}
