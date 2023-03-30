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

package com.taotao.cloud.message.biz.austin.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.taotao.cloud.message.biz.austin.common.constant.AustinConstant;
import com.taotao.cloud.message.biz.austin.common.domain.SimpleAnchorInfo;
import com.taotao.cloud.message.biz.austin.common.enums.AnchorState;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.support.dao.MessageTemplateDao;
import com.taotao.cloud.message.biz.austin.support.dao.SmsRecordDao;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
import com.taotao.cloud.message.biz.austin.support.domain.SmsRecord;
import com.taotao.cloud.message.biz.austin.support.utils.RedisUtils;
import com.taotao.cloud.message.biz.austin.support.utils.TaskInfoUtils;
import com.taotao.cloud.message.biz.austin.web.service.DataService;
import com.taotao.cloud.message.biz.austin.web.utils.Convert4Amis;
import com.taotao.cloud.message.biz.austin.web.vo.DataParam;
import com.taotao.cloud.message.biz.austin.web.vo.amis.EchartsVo;
import com.taotao.cloud.message.biz.austin.web.vo.amis.SmsTimeLineVo;
import com.taotao.cloud.message.biz.austin.web.vo.amis.UserTimeLineVo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据链路追踪获取接口 实现类
 *
 * @author 3y
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired private RedisUtils redisUtils;

    @Autowired private MessageTemplateDao messageTemplateDao;

    @Autowired private SmsRecordDao smsRecordDao;

    @Override
    public UserTimeLineVo getTraceUserInfo(String receiver) {
        List<String> userInfoList = redisUtils.lRange(receiver, 0, -1);
        if (CollUtil.isEmpty(userInfoList)) {
            return UserTimeLineVo.builder().items(new ArrayList<>()).build();
        }

        // 0. 按时间排序
        List<SimpleAnchorInfo> sortAnchorList =
                userInfoList.stream()
                        .map(s -> JSON.parseObject(s, SimpleAnchorInfo.class))
                        .sorted((o1, o2) -> Math.toIntExact(o1.getTimestamp() - o2.getTimestamp()))
                        .collect(Collectors.toList());

        // 1. 对相同的businessId进行分类
        // {"businessId":[{businessId,state,timeStamp},{businessId,state,timeStamp}]}
        Map<String, List<SimpleAnchorInfo>> map = MapUtil.newHashMap();
        for (SimpleAnchorInfo simpleAnchorInfo : sortAnchorList) {
            List<SimpleAnchorInfo> simpleAnchorInfos =
                    map.get(String.valueOf(simpleAnchorInfo.getBusinessId()));
            if (CollUtil.isEmpty(simpleAnchorInfos)) {
                simpleAnchorInfos = new ArrayList<>();
            }
            simpleAnchorInfos.add(simpleAnchorInfo);
            map.put(String.valueOf(simpleAnchorInfo.getBusinessId()), simpleAnchorInfos);
        }

        // 2. 封装vo 给到前端渲染展示
        List<UserTimeLineVo.ItemsVO> items = new ArrayList<>();
        for (Map.Entry<String, List<SimpleAnchorInfo>> entry : map.entrySet()) {
            Long messageTemplateId =
                    TaskInfoUtils.getMessageTemplateIdFromBusinessId(Long.valueOf(entry.getKey()));
            MessageTemplate messageTemplate =
                    messageTemplateDao.findById(messageTemplateId).orElse(null);
            if (Objects.isNull(messageTemplate)) {
                continue;
            }

            StringBuilder sb = new StringBuilder();
            for (SimpleAnchorInfo simpleAnchorInfo : entry.getValue()) {
                if (AnchorState.RECEIVE.getCode().equals(simpleAnchorInfo.getState())) {
                    sb.append(StrPool.CRLF);
                }
                String startTime =
                        DateUtil.format(
                                new Date(simpleAnchorInfo.getTimestamp()),
                                DatePattern.NORM_DATETIME_PATTERN);
                String stateDescription =
                        AnchorState.getDescriptionByCode(simpleAnchorInfo.getState());
                sb.append(startTime).append(StrPool.C_COLON).append(stateDescription).append("==>");
            }

            for (String detail : sb.toString().split(StrPool.CRLF)) {
                if (StrUtil.isNotBlank(detail)) {
                    UserTimeLineVo.ItemsVO itemsVO =
                            UserTimeLineVo.ItemsVO.builder()
                                    .businessId(entry.getKey())
                                    .sendType(
                                            ChannelType.getEnumByCode(
                                                            messageTemplate.getSendChannel())
                                                    .getDescription())
                                    .creator(messageTemplate.getCreator())
                                    .title(messageTemplate.getName())
                                    .detail(detail)
                                    .build();
                    items.add(itemsVO);
                }
            }
        }
        return UserTimeLineVo.builder().items(items).build();
    }

    @Override
    public EchartsVo getTraceMessageTemplateInfo(String businessId) {

        // 获取businessId并获取模板信息
        businessId = getRealBusinessId(businessId);
        Optional<MessageTemplate> optional =
                messageTemplateDao.findById(
                        TaskInfoUtils.getMessageTemplateIdFromBusinessId(Long.valueOf(businessId)));
        if (!optional.isPresent()) {
            return null;
        }

        /** 获取redis清洗好的数据 key：state value:stateCount */
        Map<Object, Object> anchorResult = redisUtils.hGetAll(getRealBusinessId(businessId));

        return Convert4Amis.getEchartsVo(anchorResult, optional.get().getName(), businessId);
    }

    @Override
    public SmsTimeLineVo getTraceSmsInfo(DataParam dataParam) {

        Integer sendDate =
                Integer.valueOf(
                        DateUtil.format(
                                new Date(dataParam.getDateTime() * 1000L),
                                DatePattern.PURE_DATE_PATTERN));
        List<SmsRecord> smsRecordList =
                smsRecordDao.findByPhoneAndSendDate(
                        Long.valueOf(dataParam.getReceiver()), sendDate);
        if (CollUtil.isEmpty(smsRecordList)) {
            return SmsTimeLineVo.builder()
                    .items(Arrays.asList(SmsTimeLineVo.ItemsVO.builder().build()))
                    .build();
        }

        Map<String, List<SmsRecord>> maps =
                smsRecordList.stream()
                        .collect(Collectors.groupingBy((o) -> o.getPhone() + o.getSeriesId()));
        return Convert4Amis.getSmsTimeLineVo(maps);
    }

    /**
     * 如果传入的是模板ID，则生成【当天】的businessId进行查询 如果传入的是businessId，则按默认的businessId进行查询
     * 判断是否为businessId则判断长度是否为16位（businessId长度固定16)
     */
    private String getRealBusinessId(String businessId) {
        if (AustinConstant.BUSINESS_ID_LENGTH == businessId.length()) {
            return businessId;
        }
        Optional<MessageTemplate> optional = messageTemplateDao.findById(Long.valueOf(businessId));
        if (optional.isPresent()) {
            MessageTemplate messageTemplate = optional.get();
            return String.valueOf(
                    TaskInfoUtils.generateBusinessId(
                            messageTemplate.getId(), messageTemplate.getTemplateType()));
        }
        return businessId;
    }
}
