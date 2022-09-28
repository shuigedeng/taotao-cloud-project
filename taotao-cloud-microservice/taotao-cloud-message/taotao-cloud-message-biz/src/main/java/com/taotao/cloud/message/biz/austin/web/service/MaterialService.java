package com.taotao.cloud.message.biz.austin.web.service;


import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 素材接口
 *
 * @author 3y
 */
public interface MaterialService {


	/**
	 * 钉钉素材上传
	 *
	 * @param file
	 * @param sendAccount
	 * @param fileType
	 * @return
	 */
	BasicResultVO dingDingMaterialUpload(MultipartFile file, String sendAccount, String fileType);


}
