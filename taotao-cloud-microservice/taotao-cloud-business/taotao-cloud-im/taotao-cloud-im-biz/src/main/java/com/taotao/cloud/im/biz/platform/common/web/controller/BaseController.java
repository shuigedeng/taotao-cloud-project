package com.taotao.cloud.im.biz.platform.common.web.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.common.enums.ResultCodeEnum;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.PageDomain;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.page.TableSupport;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * web层通用数据处理
 */
@Slf4j
public class BaseController {

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.getPageDomain();
        startPage(escapeOrderBySql(pageDomain.getOrderBy()));
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage(String orderBy) {
        PageDomain pageDomain = TableSupport.getPageDomain();
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize(), orderBy);
    }

    /**
     * 设置排序分页数据
     */
    protected void orderBy(String orderBy) {
        PageHelper.orderBy(orderBy);
    }

    /**
     * 检查字符，防止注入绕过
     */
    private static String escapeOrderBySql(String value) {
        // 仅支持字母、数字、下划线、空格、逗号（支持多个字段排序）
        String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,]+";
        if (!StringUtils.isBlank(value) && !value.matches(SQL_PATTERN)) {
            return StringUtils.EMPTY;
        }
        return value;
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        return formatData(list, new PageInfo(list).getTotal());
    }

    protected TableDataInfo getDataTable(List<?> list, PageDomain pageDomain) {
        return getDataTable(CollUtil.sub(list, pageDomain.getPageStart(), pageDomain.getPageEnd()));
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(PageInfo<?> list) {
        return formatData(list.getList(), list.getTotal());
    }

    /**
     * 格式化分页
     */
    private TableDataInfo formatData(List<?> list, Long total) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(ResultCodeEnum.SUCCESS.getCode());
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(total);
        return rspData;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.fail();
    }

}
