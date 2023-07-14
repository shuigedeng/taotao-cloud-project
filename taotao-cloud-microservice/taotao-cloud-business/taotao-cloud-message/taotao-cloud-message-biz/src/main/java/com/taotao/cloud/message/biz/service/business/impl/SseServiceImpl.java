package com.taotao.cloud.message.biz.service.business.impl;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.message.biz.service.business.SseService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SSE(SseEmitter)与WebSocket的主要区别：
 * <p>
 * 建立连接的方式不同：
 * <p>
 * <p>
 * SSE：客户端发送一个长连接请求，然后服务端将事件通过 HTTP 响应推送给客户端。
 * WebSocket：采用双工通信,客户端和服务器建立实时的双向通信信道。
 * <p>
 * <p>
 * 传输效率不同：
 * <p>
 * <p>
 * SSE：需要经常建立和关闭连接,效率不如 WebSocket。但支持 HTTP 缓存。
 * WebSocket：建立后保持连接不断,效率高于SSE。
 * <p>
 * <p>
 * 兼容性不同：
 * <p>
 * <p>
 * SSE：原生支持的浏览器相对较少。需要Polyfill。
 * WebSocket：现代浏览器基本全面支持。
 * <p>
 * <p>
 * 传输内容不同：
 * <p>
 * <p>
 * SSE：只允许推送文本，不支持传输二进制数据。
 * WebSocket：支持传输文本以及二进制数据。
 * <p>
 * <p>
 * 功能不同：
 * <p>
 * <p>
 * SSE：只支持服务器主动推送,客户端只能被动接收。
 * WebSocket：支持双向全 duplex 通信,客户端和服务器都可以主动发送消息。
 * <p>
 * <p>
 * 使用场景不同：
 * <p>
 * <p>
 * SSE：适用于需要一对一推送事件的场景。客户端只需监听,服务器主动推送。
 * WebSocket：适用于需要实时双向交互的场景。例如聊天应用。
 * <p>
 * 总的来说：
 * <p>
 * SSE 适用于服务器单向推送文本事件的场景，兼容性稍差但效率高。
 * WebSocket 适用于实时双向通信的场景，效率更高但兼容性要求高。
 * <p>
 * 作者：基督山伯爵_Neo
 * 链接：https://juejin.cn/post/7250328942841495613
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 * <p>
 * 注意事项：
 * 推送数据结束后，不要在 finally 块中调用 emitter.complete() 来关闭连接，否则会触发一个很诡异的BUG，如果此时在很短的时间内请求别的接口，可能会收到一个502 bad Gateway 的异常信息，原因就是和这个帖子 记一次springboot应用偶发502错误的排查过程_帅帅兔子的博客-CSDN博客 差不多。
 *
 * @author shuigedeng
 * @version 2023.07
 * @see SseService
 * @since 2023-07-14 08:53:31
 */
@Service
public class SseServiceImpl implements SseService {
	private static final Map<String, SseEmitter> SSE_CACHE = new ConcurrentHashMap<>();


	@Override
	public SseEmitter getConn(String clientId) {
		final SseEmitter sseEmitter = SSE_CACHE.get(clientId);

		if (sseEmitter != null) {
			return sseEmitter;
		} else {
			// 设置连接超时时间，需要配合配置项 spring.mvc.async.request-timeout: 600000 一起使用
			final SseEmitter emitter = new SseEmitter(600_000L);
			// 注册超时回调，超时后触发
			emitter.onTimeout(() -> {
				LogUtils.info("连接已超时，正准备关闭，clientId = {}", clientId);
				SSE_CACHE.remove(clientId);
			});
			// 注册完成回调，调用 emitter.complete() 触发
			emitter.onCompletion(() -> {
				LogUtils.info("连接已关闭，正准备释放，clientId = {}", clientId);
				SSE_CACHE.remove(clientId);
				LogUtils.info("连接已释放，clientId = {}", clientId);
			});
			// 注册异常回调，调用 emitter.completeWithError() 触发
			emitter.onError(throwable -> {
				LogUtils.error("连接已异常，正准备关闭，clientId = {}", clientId, throwable);
				SSE_CACHE.remove(clientId);
			});

			SSE_CACHE.put(clientId, emitter);

			return emitter;
		}
	}

	/**
	 * 模拟类似于 chatGPT 的流式推送回答
	 *
	 * @param clientId 客户端 id
	 * @throws IOException 异常
	 */
	@Override
	public void send(String clientId) throws IOException {
		final SseEmitter emitter = SSE_CACHE.get(clientId);
		// 推流内容到客户端
		emitter.send("此去经年", org.springframework.http.MediaType.APPLICATION_JSON);
		emitter.send("此去经年，应是良辰好景虚设");
		emitter.send("此去经年，应是良辰好景虚设，便纵有千种风情");
		emitter.send("此去经年，应是良辰好景虚设，便纵有千种风情，更与何人说");
		// 结束推流
		emitter.complete();
	}

	@Override
	public void closeDialogueConn(String clientId) {
		final SseEmitter sseEmitter = SSE_CACHE.get(clientId);
		if (sseEmitter != null) {
			sseEmitter.complete();
		}
	}


}
