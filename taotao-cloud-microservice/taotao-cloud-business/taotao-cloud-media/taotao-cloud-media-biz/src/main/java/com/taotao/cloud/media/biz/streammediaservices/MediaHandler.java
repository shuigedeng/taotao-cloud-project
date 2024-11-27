package com.taotao.cloud.media.biz.streammediaservices;

import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 媒体请求处理Handler
 *
 * @author LGC
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MediaHandler extends SimpleChannelInboundHandler<Object> {


	private final static String MEDIA_NAME = "media-serve";

	private final MediaService mediaService;
	/**
	 * http-flv
	 */
	private CameraDTO httpCamera = null;
	/**
	 * ws-flv
	 */
	private CameraDTO wsCamera = null;


	public MediaHandler(MediaService mediaService) {
		this.mediaService = mediaService;
	}


	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("连接新增");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("连接关闭");
		close(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("连接异常", cause);
		close(ctx);
	}

	private void close(ChannelHandlerContext ctx) {
		if (httpCamera != null) {
			log.info("http-flv 关闭播放，url:{}", httpCamera.getUrl());
			mediaService.closeForHttp(httpCamera, ctx);
		}
		if (wsCamera != null) {
			log.info("http-ws 关闭播放，url:{}", wsCamera.getUrl());
			mediaService.closeForWs(wsCamera, ctx);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof FullHttpRequest) {
			log.info("处理http请求");
			FullHttpRequest req = (FullHttpRequest) msg;
			// 请求解析
			QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
			log.info("【HttpRequest-PATH:" + decoder.path() + "】");
			log.info("【HttpRequest-URI:" + decoder.uri() + "】");
			log.info("【HttpRequest-Parameters:" + decoder.parameters() + "】");
			log.info("【HttpRequest-Method:" + req.method().name() + "】");

			Iterator<Map.Entry<String, String>> iterator = req.headers().iteratorAsString();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				log.info(
					"【Header-Key:" + entry.getKey() + ";Header-Value:" + entry.getValue() + "】");
			}
			// http请求及非转换升级为ws请求
			if (!req.decoderResult().isSuccess() || (!"websocket".equals(
				req.headers().get("Upgrade")))) {
				log.info("http请求 响应构建");
				// 判断请求uri 是否是转流指定url
				if ("/live".equals(decoder.path())) {
					HttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
						HttpResponseStatus.OK);
					rsp.headers()
						.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)
						.set(HttpHeaderNames.CONTENT_TYPE, "video/x-flv")
						.set(HttpHeaderNames.ACCEPT_RANGES, "bytes")
						.set(HttpHeaderNames.PRAGMA, "no-cache")
						.set(HttpHeaderNames.CACHE_CONTROL, "no-cache")
						.set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED)
						.set(HttpHeaderNames.SERVER, MEDIA_NAME);
					ctx.writeAndFlush(rsp);
					httpCamera = buildCamera(req.uri());
					mediaService.playForHttp(httpCamera, ctx);
				}
				// 非转流指定url 关闭连接
				else {
					ByteBuf content = Unpooled.copiedBuffer(MEDIA_NAME, CharsetUtil.UTF_8);
					FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
						HttpResponseStatus.OK, content);
					response.headers()
						.set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
					response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
					ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
				}
			}
			// http请求升级为ws
			else {
				// 升级为ws握手
				upgradeWsHandshaker(ctx, req, decoder);
				// 判断请求uri 是否是转流指定url
				if ("/live".equals(decoder.path())) {
					wsCamera = buildCamera(req.uri());
					mediaService.playForWs(wsCamera, ctx);
				}
			}
		}

		if (msg instanceof WebSocketFrame) {
			log.info("处理ws请求");
			WebSocketFrame webSocketFrame = (WebSocketFrame) msg;
			handlerWebSocketRequest(ctx, webSocketFrame);
		}
	}


	/**
	 * ws://localhost:9998/live?url=rtsp://admin:fangjia301@192.168.88.22:554/Streaming/Channels/1
	 */
	private CameraDTO buildCamera(String url) {
		CameraDTO cameraDto = new CameraDTO();
		String[] split = url.split("url=");
		cameraDto.setUrl(split[1]);
		cameraDto.setMediaKey(SecureUtil.md5(cameraDto.getUrl()));
		if (isLocalFile(cameraDto.getUrl())) {
			cameraDto.setType(1);
		}
		return cameraDto;
	}

	/**
	 * url 地址类型
	 */
	private boolean isLocalFile(String streamUrl) {
		// 协议
		List<String> protocols = Arrays.asList("http", "https", "ws", "wss", "rtsp", "rtmp");
		String[] split = streamUrl.trim().split("\:");
		if (split.length > 0) {
			if (protocols.contains(split[0])) {
				return false;
			}
		}
		return true;
	}


	/**
	 * 升级为ws握手 参考摘自 WebSocketServerProtocolHandshakeHandler
	 */
	private static void upgradeWsHandshaker(ChannelHandlerContext ctx, FullHttpRequest req,
		QueryStringDecoder decoder) {
		log.info("websocket握手，请求升级");
		log.info("getWebSocketLocation:{}",
			getWebSocketLocation(ctx.pipeline(), req, decoder.path()));
		// 参数分别是ws地址，子协议，是否扩展，最大frame负载长度
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
			getWebSocketLocation(ctx.pipeline(), req, decoder.path()),
			null, true, 3 * 1024 * 1024);
		WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		}
		else {
			// 握手
			HttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
			rsp.headers().set(HttpHeaderNames.SERVER, MEDIA_NAME);
			ChannelPromise channelPromise = ctx.channel().newPromise();
//                    ChannelPromise channelPromise = new DefaultChannelPromise(ctx.channel());
			final ChannelFuture handshakeFuture = handshaker.handshake(ctx.channel(), req,
				rsp.headers(), channelPromise);
			handshakeFuture.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) {
					if (!future.isSuccess()) {
						channelPromise.tryFailure(future.cause());
						log.info("握手失败");
						ctx.fireExceptionCaught(future.cause());
					}
					else {
						channelPromise.trySuccess();
						log.info("握手成功");
						// 发送握手成功事件，后面handler可监听
						// Kept for compatibility
//                                ctx.fireUserEventTriggered(
//                                        WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
//                                ctx.fireUserEventTriggered(
//                                         new WebSocketServerProtocolHandler.HandshakeComplete(
//                                                req.uri(), req.headers(), handshaker.selectedSubprotocol()));
					}
				}
			});
		}
	}

	/**
	 * 摘自 WebSocketServerProtocolHandshakeHandler
	 */
	private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
		String protocol = "ws";
		if (cp.get(SslHandler.class) != null) {
			// SSL in use so use Secure WebSockets
			protocol = "wss";
		}
		String host = req.headers().get(HttpHeaderNames.HOST);
		return protocol + "://" + host + path;
	}

	/**
	 * 处理ws 消息
	 */
	private static void handlerWebSocketRequest(ChannelHandlerContext ctx, WebSocketFrame frame) {
		// 文本消息
		if (frame instanceof TextWebSocketFrame) {
			log.info("文本消息");
			return;
		}
		// 握手PING/PONG信息
		if (frame instanceof PingWebSocketFrame) {
			log.info("PING消息");
			ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 二进制信息
		if (frame instanceof BinaryWebSocketFrame) {
			log.info("二进制信息");
			return;
		}

		// 请求关闭连接信息
		if (frame instanceof CloseWebSocketFrame) {
			log.info("关闭ws信息");
		}
	}

}
