package com.taotao.cloud.uc.biz.controller;

import com.taotao.cloud.core.model.PageResult;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.uc.api.dto.role.RoleDTO;
import com.taotao.cloud.uc.api.dto.role.RoleResourceDTO;
import com.taotao.cloud.uc.api.query.role.RolePageQuery;
import com.taotao.cloud.uc.api.vo.role.RoleVO;
import com.taotao.cloud.uc.biz.entity.SysRole;
import com.taotao.cloud.uc.biz.service.ISysRoleService;
import com.taotao.cloud.uc.biz.utils.SysRoleUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色管理API
 *
 * @author dengtao
 * @date 2020-10-16 16:23:05
 * @since 1.0
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/role")
@Api(value = "角色管理API", tags = {"角色管理API"})
public class SysRoleController {

    private final ISysRoleService sysRoleService;

    @ApiOperation("根据id获取角色信息")
    @RequestOperateLog(description = "根据id获取角色信息")
    @PreAuthorize("hasAuthority('sys:role:info:id')")
    @GetMapping("/info/id/{id:[0-9]*}")
    public Result<RoleVO> findRoleById(@PathVariable(value = "id") Long id) {
        SysRole role = sysRoleService.findRoleById(id);
        RoleVO vo = SysRoleUtil.copy(role);
        return Result.succeed(vo);
    }

    @ApiOperation("根据code查询角色是否存在")
    @RequestOperateLog(description = "根据code查询角色是否存在")
    @PreAuthorize("hasAuthority('sys:role:exist:code')")
    @GetMapping("/exist/code/{code}")
    public Result<Boolean> existRoleByCode(@PathVariable(value = "code") String code) {
        Boolean result = sysRoleService.existRoleByCode(code);
        return Result.succeed(result);
    }

    @ApiOperation("根据code获取角色信息")
    @RequestOperateLog(description = "根据code获取角色信息")
    @PreAuthorize("hasAuthority('sys:role:info:code')")
    @GetMapping("/info/code")
    public Result<RoleVO> findRoleByCode(@NotNull(message = "code不能为空")
                                         @RequestParam(value = "code") String code) {
        SysRole role = sysRoleService.findRoleByCode(code);
        RoleVO vo = SysRoleUtil.copy(role);
        return Result.succeed(vo);
    }

    @ApiOperation("根据code列表获取角色信息")
    @RequestOperateLog(description = "根据code列表获取角色信息")
    @PreAuthorize("hasAuthority('sys:role:info:code')")
    @GetMapping("/info/codes")
    public Result<List<RoleVO>> findRoleByCodes(@NotNull(message = "cde列表不能为空")
                                                @RequestParam(value = "codes") Set<String> codes) {
        List<SysRole> roles = sysRoleService.findRoleByCodes(codes);
        List<RoleVO> collect = roles.stream().filter(Objects::nonNull)
                .map(SysRoleUtil::copy).collect(Collectors.toList());
        return Result.succeed(collect);
    }

    @ApiOperation("添加角色")
    @RequestOperateLog(description = "添加角色")
    //@PreAuthorize("hasAuthority('sys:role:save')")
    @PostMapping
    public Result<Boolean> saveRole(@Valid @RequestBody RoleDTO roleDTO) {
        Boolean result = sysRoleService.saveRole(roleDTO);
        return Result.succeed(result);
    }

    @ApiOperation("修改角色")
    @RequestOperateLog(description = "修改角色")
    @PreAuthorize("hasAuthority('sys:role:update')")
    @PostMapping("/{id:[0-9]*}")
    public Result<Boolean> updateRole(@PathVariable(value = "id") Long id,
                                      @Validated @RequestBody RoleDTO roleDTO) {
        Boolean result = sysRoleService.updateRole(id, roleDTO);
        return Result.succeed(result);
    }

    @ApiOperation("根据id删除角色")
    @RequestOperateLog(description = "根据id删除角色")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @DeleteMapping("/{id:[0-9]*}")
    public Result<Boolean> deleteRole(@PathVariable(value = "id") Long id) {
        Boolean result = sysRoleService.deleteRole(id);
        return Result.succeed(result);
    }

    @ApiOperation("分页查询角色集合")
    @RequestOperateLog(description = "分页查询角色集合")
    @PreAuthorize("hasAuthority('sys:role:view:page')")
    @GetMapping(value = "/page")
    public PageResult<RoleVO> findRolePage(@Validated @NotNull RolePageQuery roleQuery) {
        Pageable pageable = PageRequest.of(roleQuery.getCurrentPage(), roleQuery.getPageSize());
        Page<SysRole> page = sysRoleService.findRolePage(pageable, roleQuery);
        List<RoleVO> collect = page.stream().filter(Objects::nonNull)
                .map(SysRoleUtil::copy).collect(Collectors.toList());
        Page<RoleVO> result = new PageImpl<>(collect, pageable, page.getTotalElements());
        return PageResult.succeed(result);
    }

    @ApiOperation("查询所有角色列表")
    @RequestOperateLog(description = "查询所有角色列表")
    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping
    public Result<List<RoleVO>> findAllRoles() {
        List<SysRole> pages = sysRoleService.findAllRoles();
        List<RoleVO> collect = pages.stream().filter(Objects::nonNull)
                .map(SysRoleUtil::copy).collect(Collectors.toList());
        return Result.succeed(collect);
    }

    @ApiOperation("根据用户id获取角色列表")
    @RequestOperateLog(description = "根据用户id获取角色列表")
    //@PreAuthorize("hasAuthority('sys:role:info:userId')")
    @GetMapping("/info/userId")
    public Result<List<RoleVO>> findRoleByUserId(@NotNull(message = "用户id不能为空")
                                                 @RequestParam(value = "userId") Long userId) {
        Set<Long> userIds = new HashSet<>();
        userIds.add(userId);
        List<SysRole> roles = sysRoleService.findRoleByUserIds(userIds);
        List<RoleVO> collect = roles.stream().filter(Objects::nonNull)
                .map(SysRoleUtil::copy).collect(Collectors.toList());
        return Result.succeed(collect);
    }

    @ApiOperation("根据用户id列表获取角色列表")
    @RequestOperateLog(description = "根据用户id列表获取角色列表")
    @PreAuthorize("hasAuthority('sys:role:info:userIds')")
    @GetMapping("/info/userIds")
    public Result<List<RoleVO>> findRoleByUserIds(@NotNull(message = "用户id列表不能为空")
                                                  @RequestParam(value = "userIds") Set<Long> userIds) {
        List<SysRole> roles = sysRoleService.findRoleByUserIds(userIds);
        List<RoleVO> collect = roles.stream().filter(Objects::nonNull)
                .map(SysRoleUtil::copy).collect(Collectors.toList());
        return Result.succeed(collect);
    }

    @ApiOperation("根据角色id更新资源信息(角色分配资源)")
    @RequestOperateLog(description = "根据角色id更新资源信息(角色分配资源)")
    @PreAuthorize("hasAuthority('sys:role:resource')")
    @PutMapping("/resource")
    public Result<Boolean> saveRoleResources(@Validated @RequestBody RoleResourceDTO roleResourceDTO) {
        Boolean result = sysRoleService.saveRoleResources(roleResourceDTO);
        return Result.succeed(result);
    }
}
