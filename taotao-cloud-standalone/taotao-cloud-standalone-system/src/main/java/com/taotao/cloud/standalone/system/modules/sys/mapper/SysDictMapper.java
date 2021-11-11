package com.taotao.cloud.standalone.system.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysDict;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysDictItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 */
public interface SysDictMapper extends BaseMapper<SysDict> {



    @Select("SELECT sdi.id,sdi.item_text,sdi.item_value FROM sys_dict AS sd LEFT JOIN sys_dict_item AS sdi ON sd.id = sdi.dict_id WHERE sd.dict_name=#{dictName}")
    List<SysDictItem> queryDictItemByDictName(String dictName);
}
