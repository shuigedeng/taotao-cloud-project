package com.taotao.cloud.standalone.system.modules.sys.controller;


import com.taotao.cloud.standalone.common.utils.R;
import com.taotao.cloud.standalone.system.modules.sys.util.RedisUtil;
import com.taotao.cloud.standalone.system.modules.sys.vo.RedisVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * redis缓存管理 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/redis")
public class SysRedisController {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取所有key
     * @param pageable
     * @return
     */
    @GetMapping
    public R getAllByPage(Pageable pageable) {
        List<RedisVo> redisList = redisUtil.getAll();
        int totalElements = redisList.size();
        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }
        int fromIndex = pageable.getPageSize() * pageable.getPageNumber();
        int toIndex = pageable.getPageSize() * (pageable.getPageNumber() + 1);
        if (toIndex > totalElements) {
            toIndex = totalElements;
        }
        List<RedisVo> indexObjects = redisList.subList(fromIndex, toIndex);
        Page<RedisVo> page = new PageImpl<>(indexObjects, pageable, totalElements);
        return R.ok(page);
    }

    /**
     * 批量删除
     * @param keys
     * @return
     */
    @DeleteMapping("/delKeys")
    public R delByKeys(@RequestBody List<String> keys) {
        return R.ok(redisUtil.removeKey(keys));
    }

}

