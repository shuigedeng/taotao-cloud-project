package com.taotao.cloud.standalone.system.modules.sys.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.standalone.common.utils.R;
import com.taotao.cloud.standalone.log.annotation.SysOperaLog;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysDict;
import com.taotao.cloud.standalone.system.modules.sys.dto.DictDTO;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysDictService;
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
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/dict")
public class SysDictController {

    @Autowired
    private ISysDictService dictService;

    /**
     * 添加字典信息
     *
     * @param sysDict
     * @return
     */
    @SysOperaLog(descrption = "添加字典信息")
    @PreAuthorize("hasAuthority('sys:dict:add')")
    @PostMapping
    public R add(@RequestBody SysDict sysDict) {
        return R.ok(dictService.save(sysDict));
    }

    /**
     * 获取字典列表集合
     *
     * @param page
     * @param sysDict
     * @return
     */
    @SysOperaLog(descrption = "查询字典集合")
    @GetMapping
    @PreAuthorize("hasAuthority('sys:dipt:view')")
    public R getList(Page page, SysDict sysDict) {
        return R.ok(dictService.page(page, Wrappers.query(sysDict)));
    }


    /**
     * 更新字典
     *
     * @param dictDto
     * @return
     */
    @SysOperaLog(descrption = "更新字典")
    @PreAuthorize("hasAuthority('sys:dict:edit')")
    @PutMapping
    public R update(@RequestBody DictDTO dictDto) {
        return R.ok(dictService.updateDict(dictDto));
    }


    /**
     * 根据id删除字典
     *
     * @param id
     * @return //
     */
    @SysOperaLog(descrption = "根据id删除字典")
    @PreAuthorize("hasAuthority('sys:dict:del')")
    @DeleteMapping("{id}")
    public R delete(@PathVariable("id") int id) {
        return R.ok(dictService.removeById(id));
    }


    /**
     * 根据字典名称查询字段详情
     * @param dictName
     * @return
     */
    @GetMapping("/queryDictItemByDictName/{dictName}")
    public R queryDictItemByDictName(@PathVariable("dictName") String dictName) {
        return R.ok(dictService.queryDictItemByDictName(dictName));
    }

}

