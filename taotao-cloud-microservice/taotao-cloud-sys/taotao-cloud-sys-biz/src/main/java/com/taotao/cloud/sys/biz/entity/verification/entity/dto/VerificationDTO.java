package com.taotao.cloud.sys.biz.entity.verification.entity.dto;

import com.taotao.cloud.sys.biz.entity.verification.entity.dos.VerificationSource;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 验证码资源缓存DTO
 */
@Data
public class VerificationDTO implements Serializable {


	/**
	 * 缓存资源
	 */
	List<VerificationSource> verificationResources;

	/**
	 * 缓存滑块资源
	 */
	List<VerificationSource> verificationSlider;

}
