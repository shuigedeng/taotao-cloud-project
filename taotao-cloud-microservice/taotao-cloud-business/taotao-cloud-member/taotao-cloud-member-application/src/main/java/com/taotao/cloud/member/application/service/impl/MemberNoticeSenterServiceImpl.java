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

package com.taotao.cloud.member.application.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.member.application.service.IMemberNoticeSenterService;
import com.taotao.cloud.member.application.service.IMemberNoticeService;
import com.taotao.cloud.member.application.service.IMemberService;
import com.taotao.cloud.member.infrastructure.persistent.mapper.IMemberNoticeSenterMapper;
import com.taotao.cloud.member.infrastructure.persistent.po.MemberNotice;
import com.taotao.cloud.member.infrastructure.persistent.po.MemberNoticeSenter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员消息业务层实现
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class MemberNoticeSenterServiceImpl extends
	ServiceImpl<IMemberNoticeSenterMapper, MemberNoticeSenter>
	implements IMemberNoticeSenterService {

	/**
	 * 会员
	 */
	@Autowired
	private IMemberService memberService;
	/**
	 * 会员站内信
	 */
	@Autowired
	private IMemberNoticeService memberNoticeService;

	@Override
	public boolean customSave(MemberNoticeSenter memberNoticeSenter) {
		if (this.saveOrUpdate(memberNoticeSenter)) {
			List<MemberNotice> memberNotices = new ArrayList<>();
			// 如果是选中会员发送
			if (memberNoticeSenter.getSendType().equals(SendTypeEnum.SELECT.name())) {
				// 判定消息是否有效
				if (!StringUtils.isEmpty(memberNoticeSenter.getMemberIds())) {
					String[] ids = memberNoticeSenter.getMemberIds().split(",");
					MemberNotice memberNotice;
					for (String id : ids) {
						memberNotice = new MemberNotice();
						memberNotice.setRead(false);
						memberNotice.setContent(memberNoticeSenter.getContent());
						memberNotice.setMemberId(Long.valueOf(id));
						memberNotice.setTitle(memberNoticeSenter.getTitle());
						memberNotices.add(memberNotice);
					}
				}
				else {
					return true;
				}
			} // 否则是全部会员发送
			else {
				List<Member> members = memberService.list();
				MemberNotice memberNotice;
				for (Member member : members) {
					memberNotice = new MemberNotice();
					memberNotice.setRead(false);
					memberNotice.setContent(memberNoticeSenter.getContent());
					memberNotice.setMemberId(member.getId());
					memberNotice.setTitle(memberNoticeSenter.getTitle());
					memberNotices.add(memberNotice);
				}
			}
			// 防止没有会员导致报错
			if (memberNotices.size() > 0) {
				// 批量保存
				if (memberNoticeService.saveBatch(memberNotices)) {
					return true;
				}
				else {
					throw new BusinessException(ResultEnum.NOTICE_SEND_ERROR);
				}
			}
		}
		return true;
	}
}
