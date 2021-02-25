package com.taotao.cloud.standalone.system.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 select m.perms from sys_menu m LEFT JOIN sys_role_menu rm ON rm.menu_id = m.menu_id
 LEFT JOIN sys_user_role ur ON ur.role_id = rm.role_id LEFT JOIN sys_user u ON u.user_id = ur.user_id where u.user_id = 4
 * @author lihaodong
 * @since 2019-04-21
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {


    @Select("select m.perms from sys_menu m, sys_user u, sys_user_role ur, sys_role_menu rm\n" +
            "        where u.user_id = #{user_id} and u.user_id = ur.user_id\n" +
            "          and ur.role_id = rm.role_id and rm.menu_id = m.menu_id")
    List<String> findPermsByUserId(Integer userId);

}
