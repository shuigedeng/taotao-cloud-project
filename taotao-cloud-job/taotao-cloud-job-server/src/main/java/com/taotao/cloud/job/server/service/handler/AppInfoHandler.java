package com.taotao.cloud.job.server.service.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.ServerDiscoverCausa;
import com.taotao.cloud.job.server.persistence.domain.AppInfo;
import com.taotao.cloud.job.server.persistence.mapper.AppInfoMapper;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppInfoHandler implements RpcHandler {

	@Autowired
	AppInfoMapper appInfoMapper;

	@Override
	public void handle(Object req, StreamObserver<CommonCausa.Response> responseObserver) {
		ServerDiscoverCausa.AppName request = (ServerDiscoverCausa.AppName) req;
		System.out.println(("请求中的参数为msg:{},code:{}" + request.getAppName()));
		AppInfo appInfo = appInfoMapper.selectOne(new QueryWrapper<AppInfo>().lambda()
			.eq(AppInfo::getAppName, request.getAppName()));
		CommonCausa.Response response;
		if (appInfo != null) {
			Long appId = appInfo.getId();
			// split to other subgroup
			if (!request.getSubAppName().isEmpty()) {
				AppInfo build = AppInfo.builder()
					.appName(request.getAppName())
					.currentServer(request.getTargetServer())
					.subAppName(request.getSubAppName()).build();
				appInfoMapper.insert(build);
				appId = build.getId();
			}

			response = CommonCausa.Response.newBuilder()
				.setCode(RemoteConstant.SUCCESS)
				.setWorkInfo(
					ServerDiscoverCausa.WorkInfo.newBuilder().setAppId(appId).build()
				).build();
		} else {
			response = CommonCausa.Response.newBuilder()
				.setCode(RemoteConstant.FAULT)
				.build();
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();

	}
}
