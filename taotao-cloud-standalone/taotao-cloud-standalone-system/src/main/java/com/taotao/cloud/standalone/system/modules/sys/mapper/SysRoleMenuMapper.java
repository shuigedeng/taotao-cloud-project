package com.taotao.cloud.standalone.system.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysRoleMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色菜单表 Mapper 接口
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * @Author 李号东
     * @Description 根据userId获取菜单id
     * @since 11:21 2019-05-10
     **/
    @Select("SELECT rm.menu_id FROM sys_role_menu rm,sys_user_role ur,sys_user u WHERE u.user_id = #{userId} AND u.user_id = ur.user_id AND rm.role_id = ur.role_id")
    List<Integer> getMenuIdByUserId(Integer userId);

}
