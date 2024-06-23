package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.other1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/urlmap")
public class UrlMapController {

	private static final String DOMAIN = "http://127.0.0.1:8888/urlmap/";

	@Autowired
	private UrlMapService urlMapService;

	/**
	 * 前端传入一个长链接,后端根据长链接生成对应的短链接键值,并且将短链接键值接入到对应的 url 后面
	 *
	 * @param longUrl 对应的长链接 http://localhost:8888/urlmap/shorten?longUrl="www.baidu.com"
	 * @return 对应生成的短链接 http://127.0.0.1:8888/000001
	 */
	@PostMapping("/shorten")
	public ResponseResult<Map> shorten(@RequestParam("longUrl") String longUrl) {
		//非空检验,避免传入空的长链接参数导致错误
		if (longUrl == null) {
			throw new RuntimeException("Link parameter exception: The passed long link parameter is abnormal.");
		}

		String encode = urlMapService.encode(longUrl);
		return ResultUtils.success(Map.of("shortKey", encode, "shortUrl", DOMAIN + encode));
	}

	@GetMapping("/{shortKey}")
	public RedirectView redirect(@PathVariable("shortKey") String shortKey) {
		return urlMapService.decode(shortKey).map(RedirectView::new)
			.orElse(new RedirectView(DOMAIN + "/sorry"));
	}

	@GetMapping("/sorry")
	public String sorry() {
		return "抱歉，未找到页面！";
	}

}
