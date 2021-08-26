$(function(){
	socket.init();
});
var basePath = "ws://localhost:8080/seckill/";
socket = {
	webSocket : "",
	init : function() {
		//userId：自行追加
		if ('WebSocket' in window) {
			webSocket = new WebSocket(basePath+'websocket/1'); 
		} 
		else if ('MozWebSocket' in window) {
			webSocket = new MozWebSocket(basePath+"websocket/1");
		} 
		else {
			webSocket = new SockJS(basePath+"sockjs/websocket");
		}
		webSocket.onerror = function(event) {
			alert("websockt连接发生错误，请刷新页面重试!")
		};
		webSocket.onopen = function(event) {
			
		};
		webSocket.onmessage = function(event) {
			var message = event.data;
			alert(message)//判断秒杀是否成功、自行写逻辑
		};
	}
}