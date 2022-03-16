package com.taotao.cloud.sys.biz.entity.verification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.sys.biz.entity.system.mapper.VerificationSourceMapper;
import com.taotao.cloud.sys.biz.entity.verification.entity.dos.VerificationSource;
import com.taotao.cloud.sys.biz.entity.verification.entity.dto.VerificationDTO;
import com.taotao.cloud.sys.biz.entity.verification.entity.enums.VerificationSourceEnum;
import com.taotao.cloud.sys.biz.entity.verification.service.VerificationSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证码资源维护 业务层实现
 *
 */
@Service
public class VerificationSourceServiceImpl extends ServiceImpl<VerificationSourceMapper, VerificationSource> implements
	VerificationSourceService {

    @Autowired
    private RedisRepository redisRepository;

    @Override
    public VerificationDTO initCache() {
        List<VerificationSource> dbList = this.list();
        List<VerificationSource> resourceList = new ArrayList<>();
        List<VerificationSource> sliderList = new ArrayList<>();
        for (VerificationSource item : dbList) {
            if (item.getType().equals(VerificationSourceEnum.RESOURCE.name())) {
                resourceList.add(item);
            } else if (item.getType().equals(VerificationSourceEnum.SLIDER.name())) {
                sliderList.add(item);
            }
        }
        VerificationDTO verificationDTO = new VerificationDTO();
        verificationDTO.setVerificationResources(resourceList);
        verificationDTO.setVerificationSlider(sliderList);
        //cache.put(VERIFICATION_CACHE, verificationDTO);
        return verificationDTO;
    }

    @Override
    public VerificationDTO getVerificationCache() {
        //VerificationDTO verificationDTO;
        //try {
        //    verificationDTO = cache.get(VERIFICATION_CACHE);
        //} catch (ClassCastException cce) {
        //    verificationDTO = null;
        //}
        //if (verificationDTO == null) {
        //    return initCache();
        //}
        //return verificationDTO;
	    return null;
    }
}
