package com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.service;

import cn.bootx.common.core.exception.BizException;
import cn.bootx.common.core.exception.DataNotExistException;
import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.dto.KeyValue;
import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.payment.code.paymodel.WeChatPayCode;
import cn.bootx.payment.code.paymodel.WeChatPayWay;
import cn.bootx.payment.core.paymodel.wechat.dao.WeChatPayConfigManager;
import cn.bootx.payment.core.paymodel.wechat.entity.WeChatPayConfig;
import cn.bootx.payment.dto.paymodel.wechat.WeChatPayConfigDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* 微信支付配置
* @author xxm
* @date 2021/3/5
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayConfigService {
    private final WeChatPayConfigManager weChatPayConfigManager;

    /**
     * 添加微信支付配置
     */
    @Transactional(rollbackFor = Exception.class)
    public WeChatPayConfigDto add(WeChatPayConfigDto param){
        WeChatPayConfig weChatPayConfig = WeChatPayConfig.init(param);
        WeChatPayConfig save = weChatPayConfigManager.save(weChatPayConfig);
        return save.toDto();
    }

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public WeChatPayConfigDto update(WeChatPayConfigDto param){
        WeChatPayConfig weChatPayConfig = weChatPayConfigManager.findById(param.getId())
                .orElseThrow(() -> new BizException("微信支付配置不存在"));
        BeanUtil.copyProperties(param,weChatPayConfig, CopyOptions.create().ignoreNullValue());
        // 支付方式
        if (CollUtil.isNotEmpty(param.getPayWayList())){
            weChatPayConfig.setPayWays(String.join(",", param.getPayWayList()));
        } else {
            weChatPayConfig.setPayWays(null);
        }
        return weChatPayConfigManager.updateById(weChatPayConfig).toDto();
    }

    /**
     * 分页
     */
    public PageResult<WeChatPayConfigDto> page(PageParam pageParam){
        return MpUtil.convert2DtoPageResult(weChatPayConfigManager.page(pageParam));
    }

    /**
     * 设置启用的支付宝配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void setUpActivity(Long id){
        WeChatPayConfig weChatPayConfig = weChatPayConfigManager.findById(id).orElseThrow(() -> new BizException("微信支付配置不存在"));
        if (Objects.equals(weChatPayConfig.getActivity(),Boolean.TRUE)){
            return;
        }
        weChatPayConfigManager.removeAllActivity();
        weChatPayConfig.setActivity(true);
        weChatPayConfigManager.updateById(weChatPayConfig);
    }

    /**
     * 清除启用状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearActivity(Long id){
        WeChatPayConfig weChatPayConfig = weChatPayConfigManager.findById(id).orElseThrow(() -> new BizException("微信支付配置不存在"));
        if (Objects.equals(weChatPayConfig.getActivity(),Boolean.TRUE)){
            return;
        }
        weChatPayConfig.setActivity(false);
        weChatPayConfigManager.updateById(weChatPayConfig);
    }


    /**
     * 获取
     */
    public WeChatPayConfigDto findById(Long id){
        return weChatPayConfigManager.findById(id)
                .map(WeChatPayConfig::toDto)
                .orElseThrow(DataNotExistException::new);
    }

    /**
     * 微信支持支付方式
     */
    public List<KeyValue> findPayWayList() {
        return WeChatPayWay.getPayWays().stream()
                .map(e->new KeyValue(e.getCode(),e.getName()))
                .collect(Collectors.toList());
    }

    /**
     * 初始化
     */
    public static void initApiConfig(WeChatPayConfig weChatPayConfig) {
        WxPayApiConfig wxPayApiConfig;
        // 公钥方式
        if (Objects.equals(weChatPayConfig.getAuthType(), WeChatPayCode.AUTH_TYPE_KEY)){
            wxPayApiConfig = WxPayApiConfig.builder()
                    .appId(weChatPayConfig.getAppId())
                    .mchId(weChatPayConfig.getMchId())
                    .apiKey(weChatPayConfig.getApiKey())
                    .certPath(weChatPayConfig.getCertPath())
                    .domain(weChatPayConfig.getDomain())
                    .build();
        }
        // 证书
        else if (Objects.equals(weChatPayConfig.getAuthType(), WeChatPayCode.AUTH_TYPE_CART)){
            wxPayApiConfig = WxPayApiConfig.builder()
                    .appId(weChatPayConfig.getAppId())
                    .mchId(weChatPayConfig.getMchId())
                    .apiKey(weChatPayConfig.getApiKey())
                    .certPath(weChatPayConfig.getCertPath())
                    .keyPemPath(weChatPayConfig.getDomain())
                    .build();
        } else {
            throw new BizException("微信支付认证类型配置不存在");
        }
        WxPayApiConfigKit.setThreadLocalWxPayApiConfig(wxPayApiConfig);
    }

}
