let websocket: WebSocket
// let lockReconnect = false
// let errorIndex = 1;

let createWebSocket = (url: string) => {
  websocket = new WebSocket(url)
  websocket.onopen = function () {
    heartCheck.reset().start();
  }
  websocket.onclose = function () {
    // closeWebSocket()
    heartCheck.clear();
  }
  websocket.onerror = function () {
    // reconnect(url);
  }
  websocket.onmessage = function (event) {
    // lockReconnect = true;
    let data = event.data;
    if (!"HeartBeat" === data) {
      //event 为服务端传输的消息，在这里可以处理
      console.log(event.data)
      // let data=JSON.parse(event.data);//把获取到的消息处理成字典，方便后期使用
      // PubSub.publish('message',data); //发布接收到的消息 'message' 为发布消息的名称，data 为发布的消息
    }
  }
}

// let reconnect = (url: string) => {
//   if (lockReconnect) return;
//   //没连接上会一直重连，设置延迟避免请求过多
//   setTimeout(function () {
//     createWebSocket(url);
//     lockReconnect = false;
//     errorIndex += 1
//   }, 5000);
//
//   if (errorIndex > 3) {
//     closeWebSocket()
//     heartCheck.clear();
//     return
//   }
// }

let heartCheck = {
  timeout: 2000, //6秒
  timeoutObj: null,
  reset: function () {
    // @ts-ignore
    clearInterval(this.timeoutObj);
    return this;
  },
  start: function () {
    // @ts-ignore
    this.timeoutObj = setInterval(function () {
      //这里发送一个心跳，后端收到后，返回一个心跳消息，
      //onmessage拿到返回的心跳就说明连接正常
      console.log(websocket.CLOSING)
      !(websocket.CLOSING !== 2 || websocket.CLOSED !== 3) && websocket.send("HeartBeat");
    }, this.timeout)
  },
  clear: function () {
    // @ts-ignore
    this.timeoutObj && clearInterval(this.timeoutObj);
  }
}

//关闭连接
let closeWebSocket = () => {
  websocket && websocket.close();
}

export {
  websocket,
  createWebSocket,
  closeWebSocket
};
