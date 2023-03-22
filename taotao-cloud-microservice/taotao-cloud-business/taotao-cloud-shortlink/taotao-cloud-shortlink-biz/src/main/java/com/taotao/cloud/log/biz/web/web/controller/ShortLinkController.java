package com.taotao.cloud.log.biz.web.web.controller;

import com.taotao.cloud.log.biz.web.service.IShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This is Description
 *
 * @since 2022/05/06
 */
@Slf4j
@Controller
public class ShortLinkController {

//    @Resource
//    private ShortLinkBiz shortLinkBiz;

	@Resource(name = "shortLinkServiceSimpleImpl")
	private IShortLinkService shortLinkService;

	// TODO 临时使用该方法，方便测试
	@GetMapping(path = "/{shortLinkCode}")
	@ResponseBody
	public String dispatch(@PathVariable(name = "shortLinkCode") String shortLinkCode,
		HttpServletRequest request, HttpServletResponse response) {
		try {
			Optional<String> originUrlOpt = shortLinkService.parseShortLinkCode(shortLinkCode);
			if (!originUrlOpt.isPresent()) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				return "查找不到，code -> " + shortLinkCode;
			}

			return originUrlOpt.get();
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			log.info("dispatch: 请求短链时异常, ,code -> {},e -> {}", shortLinkCode, e.toString());
		}

		return "";
	}

//    @GetMapping(path = "/{shortLinkCode}")
//    public void dispatch(@PathVariable(name = "shortLinkCode") String shortLinkCode,
//                         HttpServletRequest request, HttpServletResponse response) {
//        try {
//            Optional<String> originUrlOpt = shortLinkBiz.parseShortLinkCode(shortLinkCode);
//            if (!originUrlOpt.isPresent()){
//                response.setStatus(HttpStatus.NOT_FOUND.value());
//                return;
//            }
//
//            // TODO test
//            response.setHeader("Location", originUrlOpt.get());
//            response.setStatus(HttpStatus.FOUND.value());
//        } catch (Exception e) {
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            log.info("dispatch: 请求短链时异常, ,code -> {},e -> {}",shortLinkCode,e.toString());
//        }
//    }

	@GetMapping(path = "")
	@ResponseBody
	public String test() {
		return "跳转成功！！！";
	}

}
