/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.job.server.service.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Sets;
import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.common.exception.TtcJobException;
import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.ServerDiscoverCausa;
import com.taotao.cloud.job.server.common.config.TtcJobServerConfig;
import com.taotao.cloud.job.server.common.grpc.PingServerRpcClient;
import com.taotao.cloud.job.server.extension.lock.LockService;
import com.taotao.cloud.job.server.persistence.domain.AppInfo;
import com.taotao.cloud.job.server.persistence.mapper.AppInfoMapper;
import io.grpc.stub.StreamObserver;

import java.util.Objects;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * HeartbeatHandler
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Component
@Slf4j
public class HeartbeatHandler implements RpcHandler {

    private static final int RETRY_TIMES = 10;
    private static final String SERVER_ELECT_LOCK = "server_elect_%d";
    private String ownerIp;

    @Autowired
    private LockService lockService;
    @Autowired
    AppInfoMapper appInfoMapper;
    @Autowired
    PingServerRpcClient pingServerRpcService;
    @Autowired
    TtcJobServerConfig ttcJobServerConfig;

    @Override
    public void handle( Object req, StreamObserver<CommonCausa.Response> responseObserver ) {
        ServerDiscoverCausa.HeartbeatCheck request = (ServerDiscoverCausa.HeartbeatCheck) req;
        // if is local ,return now
        if (checkLocalServer(request.getCurrentServer())) {
            parseResponse(request.getCurrentServer(), responseObserver);
            return;
        }
        log.info("no local, req ip is {}", request.getCurrentServer());

        // origin server

        // cache for avoid ask down server
        Set<String> downServerCache = Sets.newHashSet();
        Long appId = request.getAppId();
        for (int i = 0; i < RETRY_TIMES; i++) {

            // get server in db
            AppInfo appInfo =
                    appInfoMapper.selectOne(
                            new QueryWrapper<AppInfo>().lambda().eq(AppInfo::getId, appId));
            if (appInfo == null) {
                throw new TtcJobException(appId + " is not registered!");
            }
            String appName = appInfo.getAppName();
            String originServer = appInfo.getCurrentServer();
            // check the server in db
            String activeAddress = activeAddress(originServer, downServerCache);
            if (StringUtils.isNotEmpty(activeAddress)) {
                parseResponse(activeAddress, responseObserver);
                return;
            }

            // the server in db is no available, so elect new server
            // need lock for avoiding other server elect the different master for the same appId
            String lockName = String.format(SERVER_ELECT_LOCK, appId);
            boolean lockStatus = lockService.tryLock(lockName, 30000);
            if (!lockStatus) {
                try {
                    Thread.sleep(500);
                } catch (Exception ignore) {
                }
                continue;
            }
            try {

                // double check
                appInfo =
                        appInfoMapper.selectOne(
                                new QueryWrapper<AppInfo>().lambda().eq(AppInfo::getId, appId));
                String address = activeAddress(appInfo.getCurrentServer(), downServerCache);
                if (StringUtils.isNotEmpty(address)) {
                    parseResponse(address, responseObserver);
                    return;
                }

                // this machine be the server of the appid

                appInfo.setCurrentServer(ownerIp);
                appInfoMapper.updateById(appInfo);
                log.info(
                        "[ServerElection] this server({}) become the new server for app(appId={}).",
                        appInfo.getCurrentServer(),
                        appId);
                parseResponse(ownerIp, responseObserver);
                return;

            } catch (Exception e) {
                log.error("[ServerElection] write new server to db failed for app {}.", appName, e);
            } finally {
                lockService.unlock(lockName);
            }
        }
        throw new TtcJobException("server elect failed for app " + appId);
    }

    private String activeAddress( String serverAddress, Set<String> downServerCache ) {

        if (downServerCache.contains(serverAddress)) {
            return null;
        }
        if (StringUtils.isEmpty(serverAddress)) {
            return null;
        }

        ServerDiscoverCausa.Ping ping =
                ServerDiscoverCausa.Ping.newBuilder().setTargetServer(serverAddress).build();

        // ping the targetServer
        CommonCausa.Response response = (CommonCausa.Response) pingServerRpcService.call(ping);
        if (response.getCode() == RemoteConstant.SUCCESS) {
            return serverAddress;
        }
        downServerCache.add(serverAddress);
        return null;
    }

    private void parseResponse(
            String currentServer, StreamObserver<CommonCausa.Response> responseObserver ) {
        CommonCausa.Response response =
                CommonCausa.Response.newBuilder()
                        .setCode(RemoteConstant.SUCCESS)
                        .setAvailableServer(
                                ServerDiscoverCausa.AvailableServer.newBuilder()
                                        .setAvailableServer(currentServer)
                                        .build())
                        .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private boolean checkLocalServer( String currentServer ) {
        // 获取本机的InetAddress对象
        ownerIp = ttcJobServerConfig.getAddress();
        return Objects.equals(ownerIp, currentServer);
    }
}
