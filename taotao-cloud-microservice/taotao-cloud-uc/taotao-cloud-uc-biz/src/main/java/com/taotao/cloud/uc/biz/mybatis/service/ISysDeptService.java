// package com.taotao.cloud.uc.biz.service;
//
//
// import com.taotao.cloud.uc.api.dto.DeptDTO;
// import com.taotao.cloud.uc.api.vo.SysDeptTreeVo;
// import com.taotao.cloud.uc.biz.entity.SysDept;
//
// import java.util.List;
//
// /**
//  * 部门管理 服务类
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:10
//  */
// public interface ISysDeptService {
//
//     /**
//      * 查询部门信息
//      *
//      * @param
//      * @return java.util.List<com.taotao.cloud.uc.api.entity.SysDept>
//      * @author dengtao
//      * @date 2020/4/30 11:10
//      */
//     List<SysDept> selectDeptList();
//
//     /**
//      * 更新部门
//      *
//      * @param entity
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 11:10
//      */
//     boolean updateDeptById(DeptDTO entity);
//
//     /**
//      * 批量删除部门
//      *
//      * @param ids
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 11:11
//      */
//     boolean batchDeleteDeptByIds(List<Integer> ids);
//
//     /**
//      * 根据部门id查询部门名称
//      *
//      * @param deptId
//      * @return java.lang.String
//      * @author dengtao
//      * @date 2020/4/30 11:11
//      */
//     String selectDeptNameByDeptId(int deptId);
//
//     /**
//      * 根据部门名称查询
//      *
//      * @param deptName
//      * @return java.util.List<com.taotao.cloud.uc.api.entity.SysDept>
//      * @author dengtao
//      * @date 2020/4/30 11:11
//      */
//     List<SysDept> selectDeptListBydeptName(String deptName);
//
//     /**
//      * 通过部门id查询于此相关的部门ids
//      *
//      * @param deptId
//      * @return java.util.List<java.lang.Integer>
//      * @author dengtao
//      * @date 2020/4/30 11:11
//      */
//     List<Integer> selectDeptIds(int deptId);
//
//     /**
//      * 查询部门信息 部门树
//      *
//      * @param
//      * @return java.util.List<com.taotao.cloud.uc.api.vo.SysDeptTreeVo>
//      * @author dengtao
//      * @date 2020/4/30 11:11
//      */
//     List<SysDeptTreeVo> queryDepartTreeList();
//
//
// }
