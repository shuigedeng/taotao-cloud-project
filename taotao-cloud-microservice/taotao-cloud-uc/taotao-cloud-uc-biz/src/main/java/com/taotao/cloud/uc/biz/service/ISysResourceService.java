package com.taotao.cloud.uc.biz.service;

import com.taotao.cloud.uc.api.dto.resource.ResourceDTO;
import com.taotao.cloud.uc.api.query.resource.ResourcePageQuery;
import com.taotao.cloud.uc.api.vo.resource.ResourceTree;
import com.taotao.cloud.uc.api.vo.resource.ResourceVO;
import com.taotao.cloud.uc.biz.entity.SysResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * 资源表服务接口
 *
 * @author dengtao
 * @date 2020-10-16 16:23:05
 * @since 1.0
 */
public interface ISysResourceService {

	/**
	 * 添加资源
	 *
	 * @param resourceDTO
	 * @return java.lang.Boolean
	 * @author dengtao
	 * @date 2020/10/21 10:11
	 * @since v1.0
	 */
	SysResource saveResource(SysResource resource);

	/**
	 * 根据id删除资源
	 *
	 * @param id
	 * @return java.lang.Boolean
	 * @author dengtao
	 * @date 2020/10/21 10:13
	 * @since v1.0
	 */
	Boolean deleteResource(Long id);

	/**
	 * 修改资源
	 *
	 * @param id
	 * @param resourceDTO
	 * @return java.lang.Boolean
	 * @author dengtao
	 * @date 2020/10/21 10:15
	 * @since v1.0
	 */
	SysResource updateResource(SysResource sysResource);

	/**
	 * 根据id获取资源信息
	 *
	 * @param id
	 * @return com.taotao.cloud.uc.biz.entity.SysResource
	 * @author dengtao
	 * @date 2020/10/21 10:16
	 * @since v1.0
	 */
	SysResource findResourceById(Long id);

	/**
	 * 分页查询资源集合
	 *
	 * @param pageable
	 * @param resourceQuery
	 * @return org.springframework.data.domain.Page<com.taotao.cloud.uc.biz.entity.SysResource>
	 * @author dengtao
	 * @date 2020/10/21 10:19
	 * @since v1.0
	 */
	Page<SysResource> findResourcePage(Pageable pageable, ResourcePageQuery resourceQuery);

	/**
	 * 查询所有资源列表
	 *
	 * @param
	 * @return java.util.List<com.taotao.cloud.uc.biz.entity.SysResource>
	 * @author dengtao
	 * @date 2020/10/21 10:21
	 * @since v1.0
	 */
	List<SysResource> findAllResources();

	/**
	 * 根据角色id列表获取角色列表
	 *
	 * @param roleIds
	 * @return java.util.List<com.taotao.cloud.uc.biz.entity.SysResource>
	 * @author dengtao
	 * @date 2020/10/21 10:24
	 * @since v1.0
	 */
	List<SysResource> findResourceByRoleIds(Set<Long> roleIds);

	/**
	 * 根据角色cde列表获取角色列表
	 *
	 * @param codes
	 * @return java.util.List<com.taotao.cloud.uc.biz.entity.SysResource>
	 * @author dengtao
	 * @date 2020/10/21 10:29
	 * @since v1.0
	 */
	List<SysResource> findResourceByCodes(Set<String> codes);

	/**
	 * 根据parentId获取角色列表
	 *
	 * @param parentId
	 * @return java.util.List<com.taotao.cloud.uc.biz.entity.SysResource>
	 * @author dengtao
	 * @date 2020/10/21 10:41
	 * @since v1.0
	 */
	List<SysResource> findResourceByParentId(Long parentId);

	/**
	 * 获取树形菜单集合 1.false-非懒加载，查询全部 " +
	 * "2.true-懒加载，根据parentId查询 2.1 父节点为空，则查询parentId=0
	 *
	 * @param lazy
	 * @param parentId
	 * @return java.util.List<com.taotao.cloud.uc.api.vo.resource.ResourceTree>
	 * @author dengtao
	 * @date 2020/10/21 11:14
	 * @since v1.0
	 */
	List<ResourceTree> findResourceTree(boolean lazy, Long parentId);

	/**
	 * 获取当前用户树形菜单列表
	 *
	 * @param resourceVOList
	 * @param parentId
	 * @return java.util.List<com.taotao.cloud.uc.api.vo.resource.ResourceTree>
	 * @author dengtao
	 * @date 2020/10/21 11:35
	 * @since v1.0
	 */
	List<ResourceTree> findCurrentUserResourceTree(List<ResourceVO> resourceVOList, Long parentId);

	/**
	 * 根据id列表查询资源信息
	 *
	 * @param idList id列表
	 * @return java.util.List<com.taotao.cloud.uc.biz.entity.SysResource>
	 * @author dengtao
	 * @date 2020/11/11 16:54
	 * @since v1.0
	 */
	List<SysResource> findResourceByIdList(List<Long> idList);

	Boolean testSeata();

	/**
	 * 根据名称获取资源信息
	 *
	 * @param name 名称
	 * @return com.taotao.cloud.uc.biz.entity.SysResource
	 * @author dengtao
	 * @date 2020/11/11 17:06
	 * @since v1.0
	 */
	SysResource findResourceByName(String name);

	/**
	 * 根据id查询资源是否存在
	 *
	 * @param id id
	 * @return java.lang.Boolean
	 * @author dengtao
	 * @date 2020/11/11 17:18
	 * @since v1.0
	 */
	Boolean existsById(Long id);

	/**
	 * 根据名称查询资源是否存在
	 *
	 * @param name 名称
	 * @return java.lang.Boolean
	 * @author dengtao
	 * @date 2020/11/11 17:19
	 * @since v1.0
	 */
	Boolean existsByName(String name);
}
