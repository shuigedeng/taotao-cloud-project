/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.system.biz.entity.DictDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DictDetailMapper extends BaseMapper<DictDetail> {

    @Select("<script>SELECT d.* from dict_detail d LEFT JOIN dict t on d.dict_id = t.id where 1=1 <if test = \"label !=null\" > and d.label LIKE concat('%', #{label}, '%') </if> <if test = \"dictName != ''||dictName !=null\" > AND t.name = #{dictName} order by t.sort asc</if></script>")
    List<DictDetail> selectDictDetailList(@Param("label") String label, @Param("dictName") String dictName);
}
