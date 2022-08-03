package com.taotao.cloud.gateway.springcloud.anti_reptile;

import com.taotao.cloud.gateway.springcloud.anti_reptile.module.VerifyImageDTO;
import com.taotao.cloud.gateway.springcloud.anti_reptile.module.VerifyImageVO;
import com.taotao.cloud.gateway.springcloud.anti_reptile.rule.RuleActuator;
import com.taotao.cloud.gateway.springcloud.anti_reptile.util.VerifyImageUtil;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

public class ValidateFormService {

	@Autowired
	private RuleActuator actuator;

	@Autowired
	private VerifyImageUtil verifyImageUtil;

	public String validate(ServerWebExchange exchange) throws UnsupportedEncodingException {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		List<FileItem> items = new ArrayList<>();

		//try {
		//	items = upload.parseRequest(exchange.getRequest().);
		//} catch (FileUploadException e) {
		//	e.printStackTrace();
		//}

		Map<String, String> params = new HashMap<>();
		params.putAll(Objects.requireNonNull(exchange.getFormData().block()).toSingleValueMap());

		for (Object object : items) {
			FileItem fileItem = (FileItem) object;
			if (fileItem.isFormField()) {
				params.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
			}
		}

		String verifyId = params.get("verifyId");
		String result = params.get("result");
		String realRequestUri = params.get("realRequestUri");
		String actualResult = verifyImageUtil.getVerifyCodeFromRedis(verifyId);

		if (actualResult != null && actualResult.equals(result.toLowerCase())) {
			actuator.reset(exchange, realRequestUri);
			return "{\"result\":true}";
		}
		return "{\"result\":false}";
	}

	public String refresh(ServerRequest request) {
		String verifyId = request.queryParam("verifyId").get();
		verifyImageUtil.deleteVerifyCodeFromRedis(verifyId);
		VerifyImageDTO verifyImage = verifyImageUtil.generateVerifyImg();
		verifyImageUtil.saveVerifyCodeToRedis(verifyImage);
		VerifyImageVO verifyImageVO = new VerifyImageVO();
		BeanUtils.copyProperties(verifyImage, verifyImageVO);
		String result =
			"{\"verifyId\": \"" + verifyImageVO.getVerifyId() + "\",\"verifyImgStr\": \""
				+ verifyImageVO.getVerifyImgStr() + "\"}";
		return result;
	}
}
