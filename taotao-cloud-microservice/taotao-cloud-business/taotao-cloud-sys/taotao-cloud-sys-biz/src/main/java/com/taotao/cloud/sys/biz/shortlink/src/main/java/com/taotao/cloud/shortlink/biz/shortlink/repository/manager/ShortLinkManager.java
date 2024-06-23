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

package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.shortlink.repository.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.log.api.api.dto.ShortLinkDTO;
import com.taotao.cloud.log.api.api.enums.BooleanEnum;
import com.taotao.cloud.log.api.api.request.ShortLinkTimePageRequest;
import com.taotao.cloud.shortlink.biz.shortlink.common.constants.ErrorCodeConstant;
import com.taotao.cloud.shortlink.biz.shortlink.repository.mapper.ShortLinkMapper;
import com.taotao.cloud.shortlink.biz.shortlink.repository.model.Domain;
import com.taotao.cloud.shortlink.biz.shortlink.repository.model.ShortLink;
import com.taotao.cloud.shortlink.biz.shortlink.utils.CommonBizUtil;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is Description
 *
 * @since 2022/05/03
 */
@Service
public class ShortLinkManager extends ServiceImpl<ShortLinkMapper, ShortLink> {

    @Resource
    private ShortLinkMapper shortLinkMapper;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Optional<ShortLinkDTO> saveShortLinkCode(ShortLink shortLink) {
        if (!save(shortLink)) {
            CommonBizUtil.throwBizError(ErrorCodeConstant.SHORT_LINK_CODE_GENERATE_ERROR);
        }

        ShortLinkDTO shortLinkDTO = ShortLinkDTO.builder()
                .id(shortLink.getId())
                .domain(shortLink.getDomain())
                .accountNo(shortLink.getAccountNo())
                .code(shortLink.getCode())
                .expired(shortLink.getExpired())
                .groupId(shortLink.getGroupId())
                .title(shortLink.getTitle())
                .originUrl(shortLink.getOriginUrl())
                .state(BooleanEnum.TRUE.getCode())
                .build();
        return Optional.of(shortLinkDTO);
    }

    public Optional<ShortLinkDTO> getShortLinkBySign(String sign) {
        QueryWrapper<ShortLink> queryWrapper = new QueryWrapper<ShortLink>()
                .eq(ShortLink.COL_SIGN, sign)
                .eq(Domain.COL_IS_DELETED, BooleanEnum.FALSE.getCode());

        List<ShortLink> shortLinks = shortLinkMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(shortLinks)) {
            return Optional.empty();
        }

        ShortLink shortLink = shortLinks.get(0);
        return Optional.of(ShortLinkDTO.builder()
                .id(shortLink.getId())
                .accountNo(shortLink.getAccountNo())
                .groupId(shortLink.getGroupId())
                .title(shortLink.getTitle())
                .code(shortLink.getCode())
                .domain(shortLink.getDomain())
                .expired(shortLink.getExpired())
                .originUrl(shortLink.getOriginUrl())
                .state(shortLink.getState())
                .build());
    }

    public Optional<ShortLinkDTO> getShortLinkByCode(String shortLinkCode) {
        QueryWrapper<ShortLink> queryWrapper = new QueryWrapper<ShortLink>()
                .eq(ShortLink.COL_CODE, shortLinkCode)
                .eq(Domain.COL_IS_DELETED, BooleanEnum.FALSE.getCode());

        List<ShortLink> shortLinks = shortLinkMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(shortLinks)) {
            return Optional.empty();
        }

        ShortLink shortLink = shortLinks.get(0);
        return Optional.of(ShortLinkDTO.builder()
                .id(shortLink.getId())
                .accountNo(shortLink.getAccountNo())
                .groupId(shortLink.getGroupId())
                .title(shortLink.getTitle())
                .code(shortLink.getCode())
                .domain(shortLink.getDomain())
                .expired(shortLink.getExpired())
                .originUrl(shortLink.getOriginUrl())
                .state(shortLink.getState())
                .build());
    }

    public List<ShortLinkDTO> listShortLinkByCode(Set<String> shortLinkCodeSet) {
        QueryWrapper<ShortLink> queryWrapper = new QueryWrapper<ShortLink>()
                .in(ShortLink.COL_CODE, shortLinkCodeSet)
                .eq(Domain.COL_IS_DELETED, BooleanEnum.FALSE.getCode());

        return CommonBizUtil.notNullList(shortLinkMapper.selectList(queryWrapper)).stream()
                .map(shortLink -> ShortLinkDTO.builder()
                        .id(shortLink.getId())
                        .accountNo(shortLink.getAccountNo())
                        .groupId(shortLink.getGroupId())
                        .title(shortLink.getTitle())
                        .code(shortLink.getCode())
                        .domain(shortLink.getDomain())
                        .expired(shortLink.getExpired())
                        .originUrl(shortLink.getOriginUrl())
                        .state(shortLink.getState())
                        .build())
                .toList();
    }

    public PageResult<ShortLinkDTO> pageShortLinkByCreateTime(ShortLinkTimePageRequest request) {
        QueryWrapper<ShortLink> queryWrapper = new QueryWrapper<ShortLink>()
                .ge(ShortLink.COL_CREATE_TIME, request.getDateRange().getLower())
                .le(ShortLink.COL_CREATE_TIME, request.getDateRange().getUpper())
                .eq(Domain.COL_IS_DELETED, BooleanEnum.FALSE.getCode())
                .orderByDesc(BooleanEnum.TRUE.getCode().equals(request.getDesc()), ShortLink.COL_CREATE_TIME);

        IPage<ShortLink> page = page(new Page<>(request.getPageNo(), request.getPageSize()), queryWrapper);
        List<ShortLinkDTO> shortLinkDtoList = CommonBizUtil.notNullList(page.getRecords()).stream()
                .map(shortLink -> {
                    return ShortLinkDTO.builder()
                            .id(shortLink.getId())
                            .accountNo(shortLink.getAccountNo())
                            .groupId(shortLink.getGroupId())
                            .title(shortLink.getTitle())
                            .code(shortLink.getCode())
                            .domain(shortLink.getDomain())
                            .expired(shortLink.getExpired())
                            .originUrl(shortLink.getOriginUrl())
                            .state(shortLink.getState())
                            .build();
                })
                .toList();

        PageResult<ShortLinkDTO> pageResult = new PageResult<>();
        pageResult.setPage(request.getPageNo());
        pageResult.setSize(request.getPageSize());
        pageResult.setData(shortLinkDtoList);

        return pageResult;
    }

    public PageResult<ShortLinkDTO> pageShortLinkByUpdateTime(ShortLinkTimePageRequest request) {
        QueryWrapper<ShortLink> queryWrapper = new QueryWrapper<ShortLink>()
                .ge(ShortLink.COL_UPDATE_TIME, request.getDateRange().getLower())
                .le(ShortLink.COL_UPDATE_TIME, request.getDateRange().getUpper())
                .eq(Domain.COL_IS_DELETED, BooleanEnum.FALSE.getCode())
                .orderByDesc(BooleanEnum.TRUE.getCode().equals(request.getDesc()), ShortLink.COL_UPDATE_TIME);

        IPage<ShortLink> page = page(new Page<>(request.getPageNo(), request.getPageSize()), queryWrapper);
        List<ShortLinkDTO> shortLinkDtoList = CommonBizUtil.notNullList(page.getRecords()).stream()
                .map(shortLink -> {
                    return ShortLinkDTO.builder()
                            .id(shortLink.getId())
                            .accountNo(shortLink.getAccountNo())
                            .groupId(shortLink.getGroupId())
                            .title(shortLink.getTitle())
                            .code(shortLink.getCode())
                            .domain(shortLink.getDomain())
                            .expired(shortLink.getExpired())
                            .originUrl(shortLink.getOriginUrl())
                            .state(shortLink.getState())
                            .build();
                })
                .toList();

        PageResult<ShortLinkDTO> pageResult = new PageResult<>();
        pageResult.setPage(request.getPageNo());
        pageResult.setSize(request.getPageSize());
        pageResult.setData(shortLinkDtoList);

        return pageResult;
    }
}
