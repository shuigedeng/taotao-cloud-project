// package com.taotao.cloud.uc.biz.service.impl;
//
// import cn.hutool.core.bean.BeanUtil;
// import cn.hutool.core.collection.CollUtil;
// import cn.hutool.core.util.ObjectUtil;
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import com.taotao.cloud.common.exception.BaseException;
// import com.taotao.cloud.data.mybatis.plus.service.impl.SuperServiceImpl;
// import com.taotao.cloud.uc.api.dto.DeptDTO;
// import com.taotao.cloud.uc.api.vo.SysDeptTreeVo;
// import com.taotao.cloud.uc.biz.entity.SysDept;
// import com.taotao.cloud.uc.biz.entity.SysUser;
// import com.taotao.cloud.uc.biz.mapper.SysDeptMapper;
// import com.taotao.cloud.uc.biz.service.ISysDeptService;
// import com.taotao.cloud.uc.biz.service.ISysUserService;
// import com.taotao.cloud.uc.biz.utils.UcUtil;
// import org.springframework.beans.BeanUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import java.io.Serializable;
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;
//
// /**
//  * 部门管理 服务实现类
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:11
//  */
// @Service
// public class SysDeptServiceImpl extends SuperServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {
//
//     @Autowired
//     private ISysUserService iSysUserService;
//
//     @Override
//     public boolean save(SysDept entity) {
//         return super.save(entity);
//     }
//
//     @Override
//     public List<SysDept> selectDeptList() {
//         List<SysDept> depts = baseMapper.selectList(Wrappers.<SysDept>lambdaQuery().select(SysDept::getId, SysDept::getName, SysDept::getParentId, SysDept::getSort, SysDept::getCreateTime));
//         List<SysDept> sysDepts = depts.stream()
//                 .filter(sysDept -> sysDept.getParentId() == 0 || ObjectUtil.isNull(sysDept.getParentId()))
// //                .peek(sysDept -> sysDept.setLevel(0))
//                 .collect(Collectors.toList());
//
//         UcUtil.findChildren(sysDepts, depts);
//         return sysDepts;
//     }
//
//     @Override
//     public boolean updateDeptById(DeptDTO entity) {
//         SysDept sysDept = new SysDept();
//         BeanUtils.copyProperties(entity, sysDept);
//         sysDept.setUpdateTime(LocalDateTime.now());
//         return this.updateById(sysDept);
//     }
//
//     @Transactional(rollbackFor = Exception.class)
//     @Override
//     public boolean removeById(Serializable id) {
//         // 部门层级删除
//         List<Integer> idList = new ArrayList<>();
//         checkChildrenExists((Integer) id, idList);
//         // 删除自己
//         idList.add((Integer) id);
//         //查询部门下有人员不允许删除
// //        List<SysUser> userInfo = iSysUserService.listByIds(idList);
//         List<SysUser> userInfo = new ArrayList<>();
//         if (userInfo.size() > 0) {
//             throw new BaseException("部门下有用户无法删除");
//         }
//         idList.forEach(deptId -> {
//             this.update(Wrappers.<SysDept>lambdaUpdate().set(SysDept::getDelFlag, 1).eq(SysDept::getId, deptId));
//         });
//         return true;
//     }
//
//     @Transactional(rollbackFor = Exception.class)
//     @Override
//     public boolean batchDeleteDeptByIds(List<Integer> ids) {
//         List<Integer> idList = new ArrayList<>();
//         for (Integer id : ids) {
//             idList.add(id);
//             this.checkChildrenExists(id, idList);
//         }
//         //查询部门下有人员不允许删除
// //        List<SysUser> userInfo = iSysUserService.listByIds(idList);
//         List<SysUser> userInfo = new ArrayList<>();
//         if (userInfo.size() > 0) {
//             throw new BaseException("部门下有用户无法删除");
//         }
//         idList.forEach(deptId -> {
//             this.update(Wrappers.<SysDept>lambdaUpdate().set(SysDept::getDelFlag, "1").eq(SysDept::getId, deptId));
//         });
//         return true;
//     }
//
//     @Override
//     public String selectDeptNameByDeptId(int deptId) {
//         return baseMapper.selectOne(Wrappers.<SysDept>query().lambda().select(SysDept::getName).eq(SysDept::getId, deptId)).getName();
//     }
//
//     @Override
//     public List<SysDept> selectDeptListBydeptName(String deptName) {
//         return null;
//     }
//
//     @Override
//     public List<Integer> selectDeptIds(int deptId) {
//         SysDept department = this.getDepartment(deptId);
//         List<Integer> deptIdList = new ArrayList<>();
//         if (department != null) {
// //            deptIdList.add(department.getDeptId());
//             addDeptIdList(deptIdList, department);
//         }
//         return deptIdList;
//     }
//
//     @Override
//     public List<SysDeptTreeVo> queryDepartTreeList() {
//         List<SysDept> depts = baseMapper.selectList(Wrappers.<SysDept>lambdaQuery().select(SysDept::getId, SysDept::getName, SysDept::getParentId, SysDept::getSort, SysDept::getRemark, SysDept::getCreateTime));
//
//         List<SysDeptTreeVo> collect = depts.stream()
//                 .filter(sysDept -> sysDept.getParentId() == 0 || ObjectUtil.isNull(sysDept.getParentId()))
// //                .peek(sysDept -> sysDept.setLevel(0))
//                 .map(sysDept -> {
//                     SysDeptTreeVo sysDeptTreeVo = new SysDeptTreeVo();
//                     BeanUtil.copyProperties(sysDept, sysDeptTreeVo);
// //                    sysDeptTreeVo.setKey(sysDept.getId());
//                     sysDeptTreeVo.setValue(String.valueOf(sysDept.getId()));
//                     sysDeptTreeVo.setTitle(sysDept.getName());
//                     return sysDeptTreeVo;
//                 })
//                 .collect(Collectors.toList());
//
//         UcUtil.findDeptTreeChildren(collect, depts);
//         return collect;
//     }
//
//     /**
//      * 根据部门ID获取该部门及其下属部门树
//      */
//     private SysDept getDepartment(Integer deptId) {
//         List<SysDept> departments = baseMapper.selectList(Wrappers.<SysDept>query().select("dept_id", "name", "parent_id", "sort", "create_time"));
// //        Map<Integer, SysDept> map = departments.stream().collect(
// //                Collectors.toMap(SysDept::getId, department -> department));
//
//         Map<Integer, SysDept> map = new HashMap<>();
//         for (SysDept dept : map.values()) {
//             SysDept parent = map.get(dept.getParentId());
//             if (parent != null) {
// //                List<SysDept> children = parent.getChildren() == null ? new ArrayList<>() : parent.getChildren();
// //                children.add(dept);
// //                parent.setChildren(children);
//             }
//         }
//         return map.get(deptId);
//     }
//
//     private void addDeptIdList(List<Integer> deptIdList, SysDept department) {
// //        List<SysDept> children = department.getChildren();
// //
// //        if (children != null) {
// //            for (SysDept dept : children) {
// //                deptIdList.add(dept.getId());
// //                addDeptIdList(deptIdList, dept);
// //            }
// //        }
//     }
//
//     /**
//      * delete 方法调用
//      *
//      * @param id
//      * @param idList
//      * @return void
//      * @author dengtao
//      * @date 2020/4/30 11:12
//      */
//     private void checkChildrenExists(int id, List<Integer> idList) {
//         List<SysDept> deptList = this.list(Wrappers.<SysDept>query().lambda().eq(SysDept::getParentId, id));
//         if (CollUtil.isNotEmpty(deptList)) {
//             for (SysDept dept : deptList) {
// //                idList.add(dept.getId());
// //                this.checkChildrenExists(dept.getId(), idList);
//             }
//         }
//     }
//
// }
