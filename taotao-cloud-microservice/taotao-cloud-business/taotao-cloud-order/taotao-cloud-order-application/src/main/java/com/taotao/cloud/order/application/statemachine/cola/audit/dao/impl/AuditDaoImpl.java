package com.taotao.cloud.order.application.statemachine.cola.audit.dao.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.taotao.cloud.order.application.statemachine.cola.audit.dao.AuditDao;
import com.taotao.cloud.order.application.statemachine.cola.audit.mapper.AuditMapper;
import com.taotao.cloud.order.application.statemachine.cola.audit.pojo.domain.AuditDO;
import com.taotao.cloud.order.application.statemachine.cola.audit.pojo.dto.AuditDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @date 2023/7/12 16:46
 */
@Component
public class AuditDaoImpl implements AuditDao {

	@Autowired
	private AuditMapper auditMapper;


	@Override
	public AuditDTO selectById(Long id) {
		try {
			AuditDO auditDO = auditMapper.selectById(id);
			AuditDTO auditDTO = new AuditDTO();
			BeanUtils.copyProperties(auditDO, auditDTO);
			return auditDTO;
		} catch (Exception e) {
			throw new RuntimeException("未知异常", e);
		}
	}

	@Override
	public void updateAuditStatus(String auditStatus, Long id) {
		try {
			UpdateWrapper<AuditDO> updateWrapper = new UpdateWrapper<>();
			updateWrapper.lambda()
				.set(AuditDO::getAuditState, auditStatus)
				.eq(AuditDO::getId, id);
			int row = auditMapper.update(null, updateWrapper);
			Assert.isTrue(row > 0, "更新状态失败");
		} catch (Exception e) {
			throw new RuntimeException("未知异常", e);
		}
	}
}
