package com.taotao.cloud.message.biz.sse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/im")
public class WebSseController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/send")
	public ResultModel send(@RequestBody MessageDTO<String> messageDTO, HttpServletRequest request) {
		logger.info("收到发往用户[{}]的文本请求;", messageDTO.getTargetUserName());
		Object userName = request.getSession().getAttribute("userName");
		if (userName == null)
			return ResultModel.error("无用户");
		messageDTO.setFromUserName((String) userName);
		messageDTO.setMessageType(Type.TYPE_TEXT.getMessageType());
		Chater chater = WebSSEUser.getChater(messageDTO.getTargetUserName());
		chater.addMsg(messageDTO);
		return ResultModel.ok();
	}

	@GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter to(HttpServletRequest request) {
		String userName = (String) request.getSession().getAttribute("userName");
		// 超时时间设置为3分钟
		SseEmitter sseEmitter = new SseEmitter(180000L);
		Chater chater = WebSSEUser.getChater(userName);
		sseEmitter.onTimeout(() -> chater.setSseEmitter(null));
		sseEmitter.onCompletion(() -> System.out.println("完成！！！"));
		chater.setSseEmitter(sseEmitter);
		return sseEmitter;
	}

	@RequestMapping(value = "/setUser")
	public ResultModel setUser(@RequestParam("userName") String userName, HttpServletRequest request) {
		logger.info("设置用户[{}]", userName);
		request.getSession().setAttribute("userName", userName);
		Chater chater = new Chater();
		chater.setUserName(userName);
		WebSSEUser.add(userName, chater);
		return ResultModel.ok();
	}

	@RequestMapping(value = "/user")
	public ResultModel user(HttpServletRequest request) {
		Object userName = request.getSession().getAttribute("userName");
		if (userName == null)
			return ResultModel.error("无用户");
		return ResultModel.ok(userName);
	}

	@RequestMapping(value = "/userList")
	public ResultModel userList() {
		return ResultModel.ok(WebSSEUser.getUserList());
	}

	@RequestMapping(value = "/fileUpload")
	public ResultModel fileUpload(@RequestParam("userName") String userName, @RequestParam MultipartFile[] myfiles,
			HttpServletRequest request) {
		logger.info("收到发往用户[{}]的文件上传请求;文件数量:{}", userName, myfiles.length);

		int count = 0;
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				count++;
			}
			logger.info("文件原名:{};文件类型:", myfile.getOriginalFilename(), myfile.getContentType());
			try (ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
					InputStream is = myfile.getInputStream();) {
				byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
				int rc = 0;
				while ((rc = is.read(buff, 0, 100)) > 0) {
					swapStream.write(buff, 0, rc);
				}
				byte[] in_b = swapStream.toByteArray(); // in_b为转换之后的结果
				logger.info("正在发送文件: ");
				MessageDTO<ByteBuffer> messageDTO = new MessageDTO<>();
				messageDTO.setFromUserName(userName);
				messageDTO.setMessage(ByteBuffer.wrap(in_b, 0, in_b.length));
				messageDTO.setMessageType(Type.TYPE_BYTE.getMessageType());
				Chater chater = WebSSEUser.getChater(messageDTO.getTargetUserName());
				chater.addMsg(messageDTO);
			} catch (IOException e) {
				logger.error("文件原名:{}", myfile.getOriginalFilename(), e);
				e.printStackTrace();
				count++;
				continue;
			}
		}
		return ResultModel.ok(count);
	}
}
