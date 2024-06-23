package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.service;

import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.controller.request.ShortLinkDelRequest;
import net.xdclass.controller.request.ShortLinkPageRequest;
import net.xdclass.controller.request.ShortLinkUpdateRequest;
import net.xdclass.model.EventMessage;
import net.xdclass.util.JsonData;
import net.xdclass.vo.ShortLinkVO;

import java.util.Map;

/**
 * @author 刘森飚
 * @since 2023-01-18
 */
public interface ShortLinkService {

    /**
     * 解析短链
     * @param shortLinkCode
     * @return
     */
    ShortLinkVO parseShortLinkCode(String shortLinkCode);

    /**
     * 创建短链
     * @param request
     * @return
     */
    JsonData createShortLink(ShortLinkAddRequest request);

    /**
     * 处理短链新增消息
     * @param eventMessage
     * @return
     */
    boolean handlerAddShortLink(EventMessage eventMessage);

    /**
     * 处理短链更新消息
     * @param eventMessage
     * @return
     */
    boolean handlerUpdateShortLink(EventMessage eventMessage);

    /**
     * 处理短链删除消息
     * @param eventMessage
     * @return
     */
    boolean handlerDelShortLink(EventMessage eventMessage);

    /**
     * 分页查找短链
     * @param request
     * @return
     */
    Map<String, Object> pageByGroupId(ShortLinkPageRequest request);

    /**
     * 删除短链
     * @param request
     * @return
     */
    JsonData del(ShortLinkDelRequest request);

    /**
     * 更新短链
     * @param request
     * @return
     */
    JsonData update(ShortLinkUpdateRequest request);
}
