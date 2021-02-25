package com.taotao.cloud.standalone.system.modules.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.standalone.common.utils.R;
import com.taotao.cloud.standalone.log.annotation.SysOperaLog;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysDictItem;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname SysDictItemController
 * @Description
 * @Author Created by Lihaodong (alias:小东啊) im.lihaodong@gmail.com
 * @Date 2019-09-02 18:14
 * @Version 1.0
 */
@RestController
@RequestMapping("/dictItem")
public class SysDictItemController {


    @Autowired
    private ISysDictItemService dictItemService;

    /**
     * 分页查询字典详情内容
     *
     * @param page        分页对象
     * @param sysDictItem
     * @return
     */
    @SysOperaLog(descrption = "查询字典详情集合")
    @GetMapping
    public R getDictItemPage(Page page, SysDictItem sysDictItem) {
        return R.ok(dictItemService.page(page, Wrappers.query(sysDictItem)));
    }

    /**
     * 添加字典详情
     * @param sysDictItem
     * @return
     */
    @SysOperaLog(descrption = "添加字典详情")
    @PreAuthorize("hasAuthority('sys:dictItem:add')")
    @PostMapping
    public R add(@RequestBody SysDictItem sysDictItem) {
        return R.ok(dictItemService.save(sysDictItem));
    }

    /**
     * 更新字典详情
     * @param sysDictItem
     * @return
     */
    @SysOperaLog(descrption = "更新字典详情")
    @PreAuthorize("hasAuthority('sys:dictItem:edit')")
    @PutMapping
    public R update(@RequestBody SysDictItem sysDictItem) {
        return R.ok(dictItemService.updateById(sysDictItem));
    }

    /**
     * 删除字典详情
     * @param id
     * @return
     */
    @SysOperaLog(descrption = "删除字典详情")
    @PreAuthorize("hasAuthority('sys:dictItem:del')")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable("id") String id) {
        return R.ok(dictItemService.removeById(id));
    }


}
