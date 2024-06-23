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

package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.shortlink.rpc;

import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.log.api.api.dto.DomainDTO;
import com.taotao.cloud.log.api.api.dto.ShortLinkDTO;
import com.taotao.cloud.log.api.api.enums.BooleanEnum;
import com.taotao.cloud.log.api.api.request.ShortLinkCreateRequest;
import com.taotao.cloud.log.api.api.request.ShortLinkListRequest;
import com.taotao.cloud.log.api.api.request.ShortLinkTimePageRequest;
import com.taotao.cloud.log.api.api.request.ShortLinkUrlQueryRequest;
import com.taotao.cloud.log.api.api.service.ShortLinkService;
import com.taotao.cloud.shortlink.biz.shortlink.adapter.ShortLinkGeneratorAdapter;
import com.taotao.cloud.shortlink.biz.shortlink.common.constants.ErrorCodeConstant;
import com.taotao.cloud.shortlink.biz.shortlink.repository.manager.DomainManager;
import com.taotao.cloud.shortlink.biz.shortlink.repository.manager.LinkGroupManager;
import com.taotao.cloud.shortlink.biz.shortlink.repository.manager.ShortLinkManager;
import com.taotao.cloud.shortlink.biz.shortlink.repository.model.ShortLink;
import com.taotao.cloud.shortlink.biz.shortlink.utils.CommonBizUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * This is Description
 *
 * @since 2022/05/03
 */
@Slf4j
@DubboService(version = "1.0.0", timeout = 3000)
public class ShortLinkServiceImpl implements ShortLinkService {

    @Resource
    private DomainManager domainManager;

    @Resource
    private LinkGroupManager linkGroupManager;

    @Resource
    private ShortLinkManager shortLinkManager;

    @Resource
    private RedissonClient redissonClient;

    @Resource(name = "hashShortLinkGenerator")
    private ShortLinkGeneratorAdapter shortLinkGeneratorAdapter;

    @Override
    public CommonResponse<Boolean> createShortLinkCode(ShortLinkCreateRequest request) {
        // TODO 幂等

        // 域名合法校验
        DomainDTO domainDTO = domainManager
                .findDomain(request.getAccountId(), request.getDomainId(), request.getDomainType())
                .orElseThrow(() -> new BusinessException(ErrorCodeConstant.DOMAIN_NOT_FOUND));
        // 分组合法校验
        linkGroupManager
                .findLinkGroup(request.getAccountId(), request.getGroupId())
                .orElseThrow(() -> new BusinessException(ErrorCodeConstant.LINK_GROUP_NOT_FOUND));

        // 生成短链
        String newGenerateShortCode = shortLinkGeneratorAdapter.createShortLinkCode(request.getOriginalUrl());

        RLock lock = redissonClient.getLock("lock_create_link:" + newGenerateShortCode);
        if (!lock.tryLock()) {
            CommonBizUtil.throwBizError(ErrorCodeConstant.SHORT_LINK_CODE_GENERATE_ERROR);
        }

        AtomicBoolean createCodeResult = new AtomicBoolean();
        try {

            //            mysql数据库，默认编码集是不区分大小写的，可能出现：根据code1查出code2的记录的情况：
            //            1.修改表的编码方式，从而支持大小写区分。再加上唯一索引 2.另外的方法，则是在代码层面重新check，避免重复
            shortLinkManager.getShortLinkByCode(newGenerateShortCode).ifPresent(shortLinkDTO -> {
                if (shortLinkDTO.getCode().equals(newGenerateShortCode)) {
                    log.warn(
                            "createShortLinkCode: 短链生成重复，shortLink -> {},originUrl" + " -> {},newUrl -> {}",
                            shortLinkDTO.getCode(),
                            shortLinkDTO.getOriginUrl(),
                            request.getOriginalUrl());
                    CommonBizUtil.throwBizError(ErrorCodeConstant.SHORT_LINK_CODE_GENERATE_ERROR);
                }
            });

            // 新生成的短链入库
            CommonBizUtil.md5(request.getOriginalUrl()).ifPresent(originalUrlMd5 -> {
                // 直接为原生URL创建索引，由于字段太大，会造成空间开销
                // 原生URL通过md5生成sign，为sign字段创建索引，避免空间开销
                ShortLink shortLink = ShortLink.builder()
                        .domain(domainDTO.getValue())
                        .accountNo(request.getAccountId())
                        .code(newGenerateShortCode)
                        .expired(request.getExpired())
                        .groupId(request.getGroupId())
                        .title(request.getTitle())
                        .originUrl(request.getOriginalUrl())
                        .sign(originalUrlMd5)
                        .state(BooleanEnum.TRUE.getCode())
                        .build();
                createCodeResult.set(shortLinkManager.save(shortLink));
            });

        } catch (Exception e) {
            log.warn("createShortLinkCode: 创建短链失败,e -> {}", e.toString());
            CommonBizUtil.throwBizError(ErrorCodeConstant.SHORT_LINK_CODE_GENERATE_ERROR);
        } finally {
            lock.unlock();
        }

        return CommonResponse.successWithData(createCodeResult.get());
    }

    @Override
    public CommonResponse<List<ShortLinkDTO>> listShortLinkCode(ShortLinkListRequest request) {
        return CommonResponse.successWithData(shortLinkManager.listShortLinkByCode(request.getShortLinkCodeSet()));
    }

    @Override
    public CommonResponse<List<ShortLinkDTO>> getShortLinkCodeByOriginUrl(ShortLinkUrlQueryRequest request) {

        //        List<ShortLinkDTO> shortLinkDTOList = CommonBizUtil.md5(request.getOriginUrl())
        //                .map(originUrlMd5 -> shortLinkManager.getShortLinkBySign(originUrlMd5))
        //                .map(Optional::get)
        //                .map(Collections::singletonList)
        //                .orElse(Collections::emptyList);

        return CommonResponse.successWithData(null);
    }

    @Override
    public CommonResponse<PageResult<ShortLinkDTO>> pageShortLinkByTime(ShortLinkTimePageRequest request) {
        // TODO: 2022/5/3 分页接口实现
        return CommonResponse.successWithData(null);
    }

    public void tempBatchCreateCode(List<ShortLinkCreateRequest> requestList) {
        List<ShortLink> shortLinkList = requestList.stream()
                .map(request -> {
                    String shortLinkCode = shortLinkGeneratorAdapter.createShortLinkCode(request.getOriginalUrl());

                    //            Optional<ShortLinkDTO> shortLinkOpt =
                    // shortLinkManager.getShortLinkByCode(shortLinkCode);
                    //            if (shortLinkOpt.isPresent()) {
                    //                if
                    // (shortLinkOpt.get().getCode().equals(shortLinkCode)) {
                    //                    return null;
                    //                }
                    //            }

                    Optional<String> originUrlOptional = CommonBizUtil.md5(request.getOriginalUrl());
                    return ShortLink.builder()
                            .domain("www.zc.cn")
                            .accountNo(request.getAccountId())
                            .code(shortLinkCode)
                            .expired(request.getExpired())
                            .groupId(request.getGroupId())
                            .title(request.getTitle())
                            .originUrl(request.getOriginalUrl())
                            .sign(originUrlOptional.get())
                            .state(BooleanEnum.TRUE.getCode())
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();

        shortLinkManager.saveBatch(shortLinkList);
        log.info("create: size -> {}", shortLinkList.size());
    }
}
