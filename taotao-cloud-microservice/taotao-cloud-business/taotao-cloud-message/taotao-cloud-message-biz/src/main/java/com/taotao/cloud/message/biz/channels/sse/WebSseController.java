package com.taotao.cloud.message.biz.channels.sse;

import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.message.biz.service.business.SseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;


/**
 * SseEmitter 的功能和用途
 * SseEmitter 的主要功能就是允许服务器能主动将信息推送给浏览器客户端。它实现了服务器推送功能。 它的主要功能和用途有以下几个:
 *
 * 能主动向单个客户端推送消息。SseEmitter能匹配唯一的客户端请求，并与该客户端保持持久连接。通过此连接，服务器可以随时将事件推送给这个客户端。
 * 能推送重复的消息。SseEmitter允许服务器不停发送相同的消息给客户端，形成一个连续的事件流。客户端只需要监听这个事件流即可。
 * 支持延迟和定时推送。通过@Scheduled注解，服务器可以在指定时间推送指定延迟的事件。
 * 支持推送不同类型的事件。客户端通过事件的名称能区分不同类型的事件，并作出不同的响应。
 * 支持推送基本数据类型和POJO对象。服务器可以推送String、int等基本类型，也可以推送任意的Java对象。
 * 能主动通知客户端关闭。通过调用complete()或error()方法，服务器可以主动告知客户端连接已关闭。
 * 解耦服务器端和客户端。服务器端仅负责推送事件，与具体的客户端无关。
 *
 * 总的来说，SseEmitter的作用就是让服务器端能主动将信息推送给单个浏览器客户端，实现服务器推送的功能。它解耦了服务器端和客户端，给予服务器端主权主动推送事件的能力。这对实时通信、实时消息推送非常有用，能显著提高用户体验。
 *
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-14 08:55:41
 */
@RestController
@RequestMapping("/sse")
public class WebSseController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SseService sseService;

	@RequestMapping(value = "/send")
	public ResultModel send(@RequestBody MessageDTO<String> messageDTO, HttpServletRequest request) {
		logger.info("收到发往用户[{}]的文本请求;", messageDTO.getTargetUserName());
		Object userName = request.getSession().getAttribute("userName");
		if (userName == null)
			return ResultModel.error("无用户");
		messageDTO.setFromUserName((String) userName);
		messageDTO.setMessageType(MessageDTO.Type.TYPE_TEXT.getMessageType());
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
		sseEmitter.onCompletion(() -> LogUtils.info("完成！！！"));
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
		if (userName == null) {
			return ResultModel.error("无用户");
		}
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
				messageDTO.setMessageType(MessageDTO.Type.TYPE_BYTE.getMessageType());
				Chater chater = WebSSEUser.getChater(messageDTO.getTargetUserName());
				chater.addMsg(messageDTO);
			} catch (IOException e) {
				logger.error("文件原名:{}", myfile.getOriginalFilename(), e);
				LogUtils.error(e);
				count++;
				continue;
			}
		}
		return ResultModel.ok(count);
	}

	//*****************************other***************
	@GetMapping(value = "test/{clientId}", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
	@ApiOperation(value = " 建立连接")
	public SseEmitter test(@PathVariable("clientId") @ApiParam("客户端 id") String clientId) {
		final SseEmitter emitter = sseService.getConn(clientId);
		CompletableFuture.runAsync(() -> {
			try {
				sseService.send(clientId);
			} catch (Exception e) {
				throw new BusinessException("推送数据异常");
			}
		});

		return emitter;
	}

	@GetMapping("closeConn/{clientId}")
	@ApiOperation(value = " 关闭连接")
	public Result<String> closeConn(@PathVariable("clientId") @ApiParam("客户端 id") String clientId) {
		sseService.closeDialogueConn(clientId);
		return Result.success("连接已关闭");
	}





}
