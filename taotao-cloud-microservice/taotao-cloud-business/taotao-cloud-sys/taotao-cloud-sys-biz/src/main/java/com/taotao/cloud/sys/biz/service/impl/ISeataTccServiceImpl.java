package com.taotao.cloud.sys.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.sys.api.model.dto.QuartzJobDTO;
import com.taotao.cloud.sys.biz.controller.ISeataTccService;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ISeataTccServiceImpl implements ISeataTccService {

	@Override
	public String tryInsert(QuartzJobDTO quartzJobDTO, Long jobId) {
		return "";
	}

	@Override
	public boolean commitTcc(BusinessActionContext context) {
		return false;
	}

	@Override
	public boolean cancel(BusinessActionContext context) {
		return false;
	}

//    private final TenantServiceApi tenantServiceApi;
//    private final IFeignQuartzJobApi feignQuartzJobApi;
//    private final IFileMapper fileMapper;
//
//    @GlobalTransactional
//    public String test(Long fileId) {
//        return tryInsert(fileId);
//
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//    public String tryInsert(Long fileId) {
//
//        LogUtils.info("try------------------> xid = " + RootContext.getXID());
//
//        //添加文件
//        File file = new File();
//        file.setId(fileId);
//        file.setCreateTime(LocalDateTime.now());
//        file.setCreateBy(1L);
//        file.setUpdateTime(LocalDateTime.now());
//        file.setCreateBy(1L);
//        file.setVersion(1);
//        file.setDelFlag(false);
//        file.setCreateName("xxx");
//        file.setBizType("asdfasf");
//        file.setDataType("DOC");
//        file.setOriginal("sdfasf");
//        file.setUrl("sdfasdf");
//        file.setMd5("sdfasf");
//        file.setType("sadf");
//        file.setContextType("sdf");
//        file.setName("sdf");
//        file.setExt("sdfa");
//        file.setLength(50L);
//        fileMapper.insert(file);
//
//        //远程创建
//        QuartzJobDTO quartzJobDTO = new QuartzJobDTO();
//        quartzJobDTO.setId(1L);
//        quartzJobDTO.setJobName("demoJob");
//        quartzJobDTO.setConcurrent(0);
//        quartzJobDTO.setJobClassName("com.taotao.cloud.xx.job.demoJob");
//        quartzJobDTO.setRemark("demo");
//        quartzJobDTO.setParams("sdfasf");
//        quartzJobDTO.setGroupName("demoJobGroup");
//        quartzJobDTO.setCronExpression("0 0 0 0 0 0");
//        quartzJobDTO.setMethodName("handleMessage");
//        quartzJobDTO.setBeanName("demoJob");
//        Boolean result = feignQuartzJobApi.addQuartzJobDTOTestSeata(quartzJobDTO);
//        if (result == null) {
//            throw new BusinessException(500, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//        }
//
//        //远程创建
//        TenantDTO tenantDTO = new TenantDTO();
//        tenantDTO.setId(1L);
//        tenantDTO.setDelFlag(false);
//        tenantDTO.setName("123");
//        tenantDTO.setCreateTime(LocalDateTime.now());
//        tenantDTO.setExpireTime(LocalDateTime.now());
//        tenantDTO.setUpdateTime(LocalDateTime.now());
//        tenantDTO.setStatus(1);
//        tenantDTO.setAccountCount(1);
//        tenantDTO.setPackageId(1L);
//        tenantDTO.setPassword("sdfasf");
//        tenantDTO.setTenantAdminId(1L);
//        tenantDTO.setTenantAdminMobile("sdfsa");
//        tenantDTO.setTenantAdminName("sdfsa");
//        String s = tenantServiceApi.addTenantWithTestSeata(tenantDTO);
//        if (s == null) {
//            throw new BusinessException(500, "qqqqqqqqq");
//        }
//
//        return "success";
//    }
//
//
//    @Override
//    public boolean commitTcc(BusinessActionContext context) {
//        LogUtils.info("xid = " + context.getXid() + "提交成功");
//        return true;
//    }
//
//
//    @Override
//    public boolean cancel(BusinessActionContext context) {
//        // 获取下单时的提交参数
//        Integer fileId = (Integer) context.getActionContext("fileId");
//
//        // 进行分支事务扣掉的金额回滚
//        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(File::getId, fileId);
//        fileMapper.delete(lambdaQueryWrapper);
//
//        LogUtils.info("xid = " + context.getXid() + "进行回滚操作");
//        return true;
//    }

}
