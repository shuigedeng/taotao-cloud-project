// package com.taotao.cloud.uc.biz.service.impl;
//
// import cn.hutool.core.util.StrUtil;
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.taotao.cloud.data.mybatis.plus.service.impl.SuperServiceImpl;
// import com.taotao.cloud.uc.biz.entity.SysJob;
// import com.taotao.cloud.uc.biz.mapper.SysJobMapper;
// import com.taotao.cloud.uc.biz.service.ISysDeptService;
// import com.taotao.cloud.uc.biz.service.ISysJobService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
// import java.io.Serializable;
// import java.util.Comparator;
// import java.util.List;
// import java.util.stream.Collectors;
//
// /**
//  * 岗位管理 服务实现类
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:33
//  */
// @Service
// public class SysJobServiceImpl extends SuperServiceImpl<SysJobMapper, SysJob> implements ISysJobService {
//
//     @Autowired
//     private ISysDeptService deptService;
//
//     @Override
//     public boolean save(SysJob entity) {
//         return super.save(entity);
//     }
//
//     @Override
//     public boolean removeById(Serializable id) {
//         return super.removeById(id);
//     }
//
//     @Override
//     public boolean updateById(SysJob entity) {
//         return super.updateById(entity);
//     }
//
//     @Override
//     public IPage<SysJob> selectJobList(int page, int pageSize, String jobName) {
//         LambdaQueryWrapper<SysJob> jobLambdaQueryWrapper = Wrappers.<SysJob>lambdaQuery();
//         if (StrUtil.isNotEmpty(jobName)) {
//             jobLambdaQueryWrapper.eq(SysJob::getJobName, jobName);
//         }
//         IPage<SysJob> sysJobIPage = baseMapper.selectPage(new Page<>(page, pageSize), jobLambdaQueryWrapper);
//         List<SysJob> sysJobList = sysJobIPage.getRecords();
//         List<SysJob> collect = sysJobList.stream()
// //                .peek(sysJob -> sysJob.setDeptName(deptService.selectDeptNameByDeptId(sysJob.getDeptId())))
//                 .sorted(Comparator.comparingInt(SysJob::getSort)).collect(Collectors.toList());
//         return sysJobIPage.setRecords(collect);
//     }
//
//     @Override
//     public List<SysJob> selectJobListByDeptId(Integer deptId) {
//         return baseMapper.selectList(Wrappers.<SysJob>lambdaQuery().select(SysJob::getId, SysJob::getJobName).eq(SysJob::getDeptId, deptId));
//     }
//
//     @Override
//     public String selectJobNameByJobId(Integer jobId) {
//         return baseMapper.selectOne(Wrappers.<SysJob>lambdaQuery().select(SysJob::getJobName).eq(SysJob::getId, jobId)).getJobName();
//     }
//
//     @Override
//     public boolean batchDeleteJobByIds(List<Integer> ids) {
//         return this.removeByIds(ids);
//     }
//
// }
