package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.component.ShortLinkComponent;
import net.xdclass.config.RabbitMQConfig;
import net.xdclass.constant.RedisKey;
import net.xdclass.controller.request.*;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.DomainTypeEnum;
import net.xdclass.enums.EventMessageType;
import net.xdclass.enums.ShortLinkStateEnum;
import net.xdclass.feign.TrafficFeignService;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manager.DomainManager;
import net.xdclass.manager.LinkGroupManager;
import net.xdclass.manager.ShortLinkManager;
import net.xdclass.manager.impl.GroupCodeMappingManagerImpl;
import net.xdclass.model.*;
import net.xdclass.service.ShortLinkService;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.IDUtil;
import net.xdclass.util.JsonData;
import net.xdclass.util.JsonUtil;
import net.xdclass.vo.ShortLinkVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 刘森飚
 * @since 2023-01-18
 */
@Service
@Slf4j
public class ShortLinkServiceImpl implements ShortLinkService{

    @Autowired
    private ShortLinkManager shortLinkManager;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Autowired
    private DomainManager domainManager;

    @Autowired
    private LinkGroupManager linkGroupManager;

    @Autowired
    private ShortLinkComponent shortLinkComponent;

    @Autowired
    private GroupCodeMappingManagerImpl groupCodeMappingManager;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private TrafficFeignService trafficFeignService;

    /**
     * 解析短链
     * @param shortLinkCode
     * @return
     */
    @Override
    public ShortLinkVO parseShortLinkCode(String shortLinkCode) {
        ShortLinkDO shortLinkDO = shortLinkManager.findByShortLinkCode(shortLinkCode);
        if (shortLinkDO == null) {
            return null;
        }
        ShortLinkVO shortLinkVO = new ShortLinkVO();
        BeanUtils.copyProperties(shortLinkDO,shortLinkVO);
        return shortLinkVO;
    }


    /**
     * 创建短链
     * @param request
     * @return
     */
    @Override
    public JsonData createShortLink(ShortLinkAddRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        //需要预先检查下是否有足够多的可以进行创建
        String cacheKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);
        // 检查key是否存在，然后-1，是否大于等于0，使用lua脚本
        // 如果key不存在，则未使用过，lua返回值是0；
        // 新增流量包的时候，不用重新计算次数，直接删除key,消费的时候回计算更新
        //这一步属于预扣减阶段
        String script = "if redis.call('get',KEYS[1]) then return redis.call('decr',KEYS[1]) else return 0 end";
        Long leftTimes = redisTemplate.execute
                (new DefaultRedisScript<>(script,Long.class),Arrays.asList(cacheKey),"");
        log.info("今日流量包剩余次数:{}",leftTimes);
        if(leftTimes >= 0){
            String newOriginalUrl = CommonUtil.addUrlPrefix(request.getOriginalUrl());
            request.setOriginalUrl(newOriginalUrl);
            EventMessage eventMessage = EventMessage.builder().accountNo(accountNo)
                    .content(JsonUtil.obj2Json(request))
                    .messageId(IDUtil.geneSnowFlakeID().toString())
                    .eventMessageType(EventMessageType.SHORT_LINK_ADD.name())
                    .build();
            rabbitTemplate.convertAndSend
                    (rabbitMQConfig.getShortLinkEventExchange(), rabbitMQConfig.getShortLinkAddRoutingKey(), eventMessage);
            return JsonData.buildSuccess();
        }else {
            //流量包不足
            return JsonData.buildResult(BizCodeEnum.TRAFFIC_REDUCE_FAIL);
        }
    }




    /**
     * 处理短链新增消息
     * @param eventMessage
     * @return
     */
    @Override
    public boolean handlerAddShortLink(EventMessage eventMessage) {
        long accountNo = eventMessage.getAccountNo();
        String messageType = eventMessage.getEventMessageType();
        ShortLinkAddRequest addRequest = JsonUtil.json2Obj(eventMessage.getContent(), ShortLinkAddRequest.class);

        //判断短链域名是否合法
        DomainDO domainDO = checkDomain(addRequest.getDomainType(), addRequest.getDomainId(), accountNo);
        // 判断组名是否合法
        LinkGroupDO linkGroupDO = checkLinkGroup(addRequest.getGroupId(), accountNo);
        // 生成长链摘要
        String originalUrlDigest = CommonUtil.MD5(addRequest.getOriginalUrl());
        //短链码重复标记
        boolean duplicateCodeFlag = false;
        // 生成短链码
        String shortLinkCode = shortLinkComponent.createShortLinkCode(addRequest.getOriginalUrl());
        // 加锁
        //key1是短链码，ARGV[1]是accountNo,ARGV[2]是过期时间
        //可重入锁
        //return 0 表示加锁失败
        //return 1 表示没有人上锁，可以插入数据库
        //return 2 然后加入可重入锁(只要是同一个业务进程就行)
        String script = "if redis.call('EXISTS',KEYS[1])==0 then redis.call('set',KEYS[1],ARGV[1]); redis.call('expire',KEYS[1],ARGV[2]); return 1;" +
                " elseif redis.call('get',KEYS[1]) == ARGV[1] then return 2;" +
                " else return 0; end;";

        Long result = redisTemplate.execute(new
                DefaultRedisScript<>(script, Long.class), Arrays.asList(shortLinkCode), accountNo, 100);

        if (result > 0) {
            //加锁成功
            //C端处理
            if (EventMessageType.SHORT_LINK_ADD_LINK.name().equalsIgnoreCase(messageType)) {
                // 查询短链码是否存在
                ShortLinkDO shortLinkCodeDOInDB = shortLinkManager.findByShortLinkCode(shortLinkCode);
                if (shortLinkCodeDOInDB == null) {
                    //流量包扣减成功，在创建短链码
                    boolean reduceFlag = reduceTraffic(eventMessage,shortLinkCode);
                    if(reduceFlag){
                        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                                .accountNo(accountNo).code(shortLinkCode)
                                .title(addRequest.getTitle()).originalUrl(addRequest.getOriginalUrl())
                                .domain(domainDO.getValue()).groupId(linkGroupDO.getId())
                                .expired(addRequest.getExpired()).sign(originalUrlDigest)
                                .state(ShortLinkStateEnum.ACTIVE.name()).del(0).build();
                        shortLinkManager.addShortLink(shortLinkDO);
                        return true;
                    }
                }else {
                    log.error("C端短链码重复:{}",eventMessage);
                    duplicateCodeFlag = true;
                }
            }else if (EventMessageType.SHORT_LINK_ADD_MAPPING.name().equalsIgnoreCase(messageType)) {
                //B端处理
                GroupCodeMappingDO groupCodeMappingDOInDB = groupCodeMappingManager
                        .findByCodeAndGroupId(shortLinkCode, linkGroupDO.getId(), accountNo);
                if (groupCodeMappingDOInDB == null) {
                    GroupCodeMappingDO groupCodeMappingDO = GroupCodeMappingDO.builder()
                            .accountNo(accountNo)
                            .code(shortLinkCode)
                            .title(addRequest.getTitle())
                            .originalUrl(addRequest.getOriginalUrl())
                            .domain(domainDO.getValue())
                            .groupId(linkGroupDO.getId())
                            .expired(addRequest.getExpired())
                            .sign(originalUrlDigest)
                            .state(ShortLinkStateEnum.ACTIVE.name())
                            .del(0)
                            .build();
                    //保存数据库
                    groupCodeMappingManager.add(groupCodeMappingDO);
                    return true;
                }else {
                    log.error("B端短链码重复:{}",eventMessage);
                    duplicateCodeFlag = true;
                }
            }
        }else {
            //加锁失败，自旋100毫秒，再调用；
            // 失败的可能是短链码已经被占用，需要重新生成
            log.error("加锁失败:{}",eventMessage);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
            }
            duplicateCodeFlag = true;
        }
        if (duplicateCodeFlag) {
            String newOriginalUrl = CommonUtil.addUrlPrefixVersion(addRequest.getOriginalUrl());
            addRequest.setOriginalUrl(newOriginalUrl);
            eventMessage.setContent(JsonUtil.obj2Json(addRequest));
            log.warn("短链码报错失败，重新生成:{}", eventMessage);
            handlerAddShortLink(eventMessage);
        }
        return false;
    }



    /**
     * 扣减流量包
     * @param eventMessage
     * @param shortLinkCode
     * @return
     */
    private boolean reduceTraffic(EventMessage eventMessage, String shortLinkCode) {
        UseTrafficRequest request = UseTrafficRequest.builder()
                .accountNo(eventMessage.getAccountNo())
                .bizId(shortLinkCode)
                .build();
        JsonData jsonData = trafficFeignService.useTraffic(request);
        if(jsonData.getCode() != 0){
            log.error("流量包不足，扣减失败:{}",eventMessage);
            return false;
        }
        return true;
    }



    /**
     * 处理短链更新消息
     * @param eventMessage
     * @return
     */
    @Override
    public boolean handlerUpdateShortLink(EventMessage eventMessage) {
        long accountNo = eventMessage.getAccountNo();
        String messageType = eventMessage.getEventMessageType();
        ShortLinkUpdateRequest request = JsonUtil.json2Obj(eventMessage.getContent(), ShortLinkUpdateRequest.class);
        //校验短链域名
        DomainDO domainDO = checkDomain(request.getDomainType(), request.getDomainId(), accountNo);
        if(EventMessageType.SHORT_LINK_UPDATE_LINK.name().equalsIgnoreCase(messageType)){
            //C端处理
            ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                    .code(request.getCode())
                    .title(request.getTitle())
                    .domain(domainDO.getValue())
                    .accountNo(accountNo)
                    .build();
            int rows = shortLinkManager.update(shortLinkDO);
            log.debug("更新C端短链，rows={}",rows);
            return true;
        } else if(EventMessageType.SHORT_LINK_UPDATE_MAPPING.name().equalsIgnoreCase(messageType)){
            //B端处理
            GroupCodeMappingDO groupCodeMappingDO = GroupCodeMappingDO.builder()
                    .id(request.getMappingId())
                    .groupId(request.getGroupId())
                    .accountNo(accountNo)
                    .title(request.getTitle())
                    .domain(domainDO.getValue())
                    .build();

            int rows = groupCodeMappingManager.update(groupCodeMappingDO);
            log.debug("更新B端短链，rows={}",rows);
            return true;
        }
        return false;
    }



    /**
     * 处理短链删除消息
     * @param eventMessage
     * @return
     */
    @Override
    public boolean handlerDelShortLink(EventMessage eventMessage) {
        Long accountNo = eventMessage.getAccountNo();
        String messageType = eventMessage.getEventMessageType();
        ShortLinkDelRequest request = JsonUtil.json2Obj(eventMessage.getContent(), ShortLinkDelRequest.class);
        if(EventMessageType.SHORT_LINK_DEL_LINK.name().equalsIgnoreCase(messageType)){
        //C端解析
            ShortLinkDO shortLinkDO = ShortLinkDO.builder().code(request.getCode()).accountNo(accountNo).build();
            int rows = shortLinkManager.del(shortLinkDO);
            log.debug("删除C端短链:{}",rows);
            return true;
        }else if(EventMessageType.SHORT_LINK_DEL_MAPPING.name().equalsIgnoreCase(messageType)){
            //B端处理
            GroupCodeMappingDO groupCodeMappingDO = GroupCodeMappingDO.builder()
                    .id(request.getMappingId()).accountNo(accountNo)
                    .groupId(request.getGroupId()).build();
            int rows = groupCodeMappingManager.del(groupCodeMappingDO);
            log.debug("删除B端短链:{}",rows);
            return true;
        }
        return false;
    }


    /**
     * 分页查找短链
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> pageByGroupId(ShortLinkPageRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        Map<String, Object> result = groupCodeMappingManager.pageShortLinkByGroupId(request.getPage(),
                request.getSize(), accountNo, request.getGroupId());
        return result;

    }


    /**
     * 短链删除
     * @param request
     * @return
     */
    @Override
    public JsonData del(ShortLinkDelRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        EventMessage eventMessage = EventMessage.builder().accountNo(accountNo)
                .content(JsonUtil.obj2Json(request))
                .messageId(IDUtil.geneSnowFlakeID().toString())
                .eventMessageType(EventMessageType.SHORT_LINK_DEL.name())
                .build();
        rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(),
                rabbitMQConfig.getShortLinkDelRoutingKey(),
                eventMessage);
        return JsonData.buildSuccess();
    }


    /**
     * 短链更新
     * @param request
     * @return
     */
    @Override
    public JsonData update(ShortLinkUpdateRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        EventMessage eventMessage = EventMessage.builder().accountNo(accountNo)
                .content(JsonUtil.obj2Json(request))
                .messageId(IDUtil.geneSnowFlakeID().toString())
                .eventMessageType(EventMessageType.SHORT_LINK_UPDATE.name())
                .build();
        rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(),
                rabbitMQConfig.getShortLinkUpdateRoutingKey(),
                eventMessage);
        return JsonData.buildSuccess();

    }


    /**
     * 校验域名
     * @param domainType
     * @param domainId
     * @param accountNo
     * @return
     */
    private DomainDO checkDomain(String domainType,Long domainId,Long accountNo) {

        DomainDO domainDO;
        if (DomainTypeEnum.CUSTOM.name().equalsIgnoreCase(domainType)) {
            domainDO = domainManager.findById(domainId, accountNo);
        }else {
            domainDO = domainManager.findByDomainTypeAndID(domainId, DomainTypeEnum.OFFICIAL);
        }
        Assert.notNull(domainDO,"短链域名不合法");

        return domainDO;
    }

    /**
     * 校验组名
     *
     * @param groupId
     * @param accountNo
     * @return
     */
    private LinkGroupDO checkLinkGroup(Long groupId, Long accountNo) {

        LinkGroupDO linkGroupDO = linkGroupManager.detail(groupId, accountNo);
        Assert.notNull(linkGroupDO, "组名不合法");
        return linkGroupDO;
    }
}
