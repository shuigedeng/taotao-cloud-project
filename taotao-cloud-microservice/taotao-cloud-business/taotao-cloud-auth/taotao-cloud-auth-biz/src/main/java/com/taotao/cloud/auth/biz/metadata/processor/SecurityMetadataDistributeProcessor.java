/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.auth.biz.metadata.processor;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.ImmutableList;
import com.taotao.boot.security.spring.access.security.SecurityAttribute;
import com.taotao.boot.security.spring.authorization.SecurityMetadataSourceAnalyzer;
import com.taotao.boot.security.spring.event.ApplicationStrategyEvent;
import com.taotao.boot.security.spring.event.domain.RequestMapping;
import com.taotao.cloud.auth.biz.strategy.user.SysAttribute;
import com.taotao.cloud.auth.biz.strategy.user.SysPermission;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>SecurityMetadata数据处理器 </p>
 */
@Component
public class SecurityMetadataDistributeProcessor
        implements ApplicationStrategyEvent<List<SecurityAttribute>> {

    private static final Logger log =
            LoggerFactory.getLogger(SecurityMetadataDistributeProcessor.class);

    //	private final SysAttributeService sysAttributeService;
    //	private final SysInterfaceService sysInterfaceService;
    private final SecurityMetadataSourceAnalyzer securityMetadataSourceAnalyzer;

    public SecurityMetadataDistributeProcessor(
            SecurityMetadataSourceAnalyzer securityMetadataSourceAnalyzer) {
        this.securityMetadataSourceAnalyzer = securityMetadataSourceAnalyzer;
    }

    //
    //	public SecurityMetadataDistributeProcessor(SysAttributeService sysAttributeService,
    // SysInterfaceService
    // sysInterfaceService, SecurityMetadataSourceAnalyzer securityMetadataSourceAnalyzer) {
    //		this.sysAttributeService = sysAttributeService;
    //		this.sysInterfaceService = sysInterfaceService;
    //		this.securityMetadataSourceAnalyzer = securityMetadataSourceAnalyzer;
    //	}

    @Override
    public void postLocalProcess(List<SecurityAttribute> data) {
        securityMetadataSourceAnalyzer.processSecurityAttribute(data);
    }

    @Override
    public void postRemoteProcess(String data, String originService, String destinationService) {
        // ServiceContext.getInstance().publishEvent(new RemoteSecurityMetadataSyncEvent(data,
        // originService,
        // destinationService));
    }

    /**
     * 将SysAuthority表中存在，但是SysSecurityAttribute中不存在的数据同步至SysSecurityAttribute，保证两侧数据一致
     */
    @Transactional(rollbackFor = Exception.class)
    public void postRequestMappings(List<RequestMapping> requestMappings) {
        //		List<SysInterface> storedInterfaces =
        // sysInterfaceService.storeRequestMappings(requestMappings);
        //		if (CollectionUtils.isNotEmpty(storedInterfaces)) {
        //			log.debug(" [5] Request mapping store success, start to merge security metadata!");
        //
        //			List<SysInterface> sysInterfaces = sysInterfaceService.findAllocatable();
        //
        //			if (CollectionUtils.isNotEmpty(sysInterfaces)) {
        //				List<SysAttribute> elements = this.convertSysInterfacesToSysAttributes(sysInterfaces);
        //				List<SysAttribute> result = sysAttributeService.saveAllAndFlush(elements);
        //				if (CollectionUtils.isNotEmpty(result)) {
        //					log.debug(" Merge security attribute SUCCESS and FINISHED!");
        //				} else {
        //					log.error(" Merge Security attribute failed!, Please Check!");
        //				}
        //			} else {
        //				log.debug(" No security attribute requires merge, SKIP!");
        //			}
        //
        //			log.debug(" [6] Synchronization current permissions to every service!");
        //
        //			List<SysAttribute> sysAttributes = sysAttributeService.findAll();
        //			this.postGroupProcess(sysAttributes);
        //		}
    }

    private void postGroupProcess(List<SysAttribute> sysAttributes) {
        if (CollectionUtils.isNotEmpty(sysAttributes)) {
            Map<String, List<SecurityAttribute>> grouped =
                    sysAttributes.stream()
                            .map(this::convertSysAttributeToSecurityAttribute)
                            .collect(Collectors.groupingBy(SecurityAttribute::getServiceId));
            log.debug(" Grouping SysInterface and distribute to every server.");
            grouped.forEach(this::postProcess);
        }
    }

    public void distributeChangedSecurityAttribute(SysAttribute sysAttribute) {
        SecurityAttribute securityAttribute = convertSysAttributeToSecurityAttribute(sysAttribute);
        postProcess(securityAttribute.getServiceId(), ImmutableList.of(securityAttribute));
    }

    //	private List<SysAttribute> convertSysInterfacesToSysAttributes(Collection<SysInterface>
    // sysInterfaces) {
    //		if (CollectionUtils.isNotEmpty(sysInterfaces)) {
    //			return
    // sysInterfaces.stream().map(this::convertSysInterfaceToSysAttribute).collect(Collectors.toList());
    //		}
    //		return new ArrayList<>();
    //	}

    //	private SysAttribute convertSysInterfaceToSysAttribute(SysInterface sysInterface) {
    //		SysAttribute sysAttribute = new SysAttribute();
    //		sysAttribute.setAttributeId(sysInterface.getInterfaceId());
    //		sysAttribute.setAttributeCode(sysInterface.getInterfaceCode());
    //		sysAttribute.setRequestMethod(sysInterface.getRequestMethod());
    //		sysAttribute.setServiceId(sysInterface.getServiceId());
    //		sysAttribute.setClassName(sysInterface.getClassName());
    //		sysAttribute.setMethodName(sysInterface.getMethodName());
    //		sysAttribute.setUrl(sysInterface.getUrl());
    //		sysAttribute.setStatus(sysInterface.getStatus());
    //		sysAttribute.setReserved(sysInterface.getReserved());
    //		sysAttribute.setDescription(sysInterface.getDescription());
    //		sysAttribute.setReversion(sysInterface.getReversion());
    //		sysAttribute.setCreateTime(sysInterface.getCreateTime());
    //		sysAttribute.setUpdateTime(sysInterface.getUpdateTime());
    //		sysAttribute.setRanking(sysInterface.getRanking());
    //		return sysAttribute;
    //	}
    //
    private SecurityAttribute convertSysAttributeToSecurityAttribute(SysAttribute sysAttribute) {
        SecurityAttribute securityAttribute = new SecurityAttribute();
        securityAttribute.setAttributeId(sysAttribute.getAttributeId());
        securityAttribute.setAttributeCode(sysAttribute.getAttributeCode());
        securityAttribute.setWebExpression(sysAttribute.getWebExpression());
        securityAttribute.setPermissions(
                convertPermissionToCommaDelimitedString(sysAttribute.getPermissions()));
        securityAttribute.setUrl(sysAttribute.getUrl());
        securityAttribute.setRequestMethod(sysAttribute.getRequestMethod());
        securityAttribute.setServiceId(sysAttribute.getServiceId());
        //		securityAttribute.setAttributeName(sysAttribute.getDescription());
        return securityAttribute;
    }

    private String convertPermissionToCommaDelimitedString(Set<SysPermission> sysAuthorities) {
        if (CollectionUtils.isNotEmpty(sysAuthorities)) {
            List<String> codes =
                    sysAuthorities.stream().map(SysPermission::getPermissionCode).toList();
            return StringUtils.collectionToCommaDelimitedString(codes);
        } else {
            return "";
        }
    }
}
