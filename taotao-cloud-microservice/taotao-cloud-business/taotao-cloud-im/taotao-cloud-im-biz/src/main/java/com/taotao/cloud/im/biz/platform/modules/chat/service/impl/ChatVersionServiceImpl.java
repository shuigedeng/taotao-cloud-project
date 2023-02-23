package com.taotao.cloud.im.biz.platform.modules.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.version.VersionUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatVersionDao;
import com.platform.modules.chat.domain.ChatVersion;
import com.platform.modules.chat.enums.VersionTypeEnum;
import com.platform.modules.chat.service.ChatVersionService;
import com.platform.modules.chat.vo.VersionVo;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 版本 服务层实现 q3z3
 * </p>
 */
@Service("chatVersionService")
public class ChatVersionServiceImpl extends BaseServiceImpl<ChatVersion> implements
		ChatVersionService {

	@Resource
	private ChatVersionDao chatVersionDao;

	@Autowired
	public void setBaseDao() {
		super.setBaseDao(chatVersionDao);
	}

	@Override
	public List<ChatVersion> queryList(ChatVersion t) {
		List<ChatVersion> dataList = chatVersionDao.queryList(t);
		return dataList;
	}

	@Override
	public String getAgreement() {
		ChatVersion obj = this.findById(VersionTypeEnum.AGREEMENT.getCode());
		return obj.getUrl();
	}

	@Value("${platform.version}")
	private String version;

	@Override
	public VersionVo getVersion(String version, String device) {
		VersionTypeEnum versionType = initDevice(device);
		ChatVersion chatVersion = this.findById(versionType.getCode());
		YesOrNoEnum upgrade =
				VersionUtils.compareTo(version, chatVersion.getVersion()) < 0 ? YesOrNoEnum.YES
						: YesOrNoEnum.NO;
		YesOrNoEnum forceUpgrade =
				VersionUtils.compareTo(version, this.version) < 0 ? YesOrNoEnum.YES
						: YesOrNoEnum.NO;
		return BeanUtil.toBean(chatVersion, VersionVo.class)
				.setUpgrade(upgrade)
				.setForceUpgrade(forceUpgrade);
	}

	/**
	 * 计算版本
	 */
	private VersionTypeEnum initDevice(String device) {
		if (VersionTypeEnum.ANDROID.getName().equalsIgnoreCase(device)) {
			return VersionTypeEnum.ANDROID;
		}
		return VersionTypeEnum.IOS;
	}

}
