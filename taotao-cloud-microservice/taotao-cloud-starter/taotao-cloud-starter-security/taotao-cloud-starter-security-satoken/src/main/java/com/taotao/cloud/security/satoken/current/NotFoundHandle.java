package com.taotao.cloud.security.satoken.current;

import cn.dev33.satoken.util.SaResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理 404
 *
 * @author kong
 */
@RestController
public class NotFoundHandle implements ErrorController {

	@RequestMapping("/error")
	public Object error(HttpServletRequest request, HttpServletResponse response)
		throws IOException {
		response.setStatus(200);
		return SaResult.get(404, "not found", null);
	}

}
