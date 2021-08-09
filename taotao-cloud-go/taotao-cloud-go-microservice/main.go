// main.go
package main

import (
	EndPoint1 "Songzhibin/go-kit-demo/v0/EndPoint"
	"Songzhibin/go-kit-demo/v0/Server"
	"Songzhibin/go-kit-demo/v0/Transport"
	httpTransport "github.com/go-kit/kit/transport/http"
	"net/http"
)

// 服务发布

func main() {
	// 1.先创建我们最开始定义的Server/server.go
	s := Server.Server{}

	// 2.在用EndPoint/endpoint.go 创建业务服务
	hello := EndPoint1.MakeServerEndPointHello(s)
	Bye := EndPoint1.MakeServerEndPointBye(s)

	// 3.使用 kit 创建 handler
	// 固定格式
	// 传入 业务服务 以及 定义的 加密解密方法
	helloServer := httpTransport.NewServer(hello, Transport.HelloDecodeRequest, Transport.HelloEncodeResponse)
	sayServer := httpTransport.NewServer(Bye, Transport.ByeDecodeRequest, Transport.ByeEncodeResponse)

	// 使用http包启动服务
	go http.ListenAndServe("0.0.0.0:8000", helloServer)

	go http.ListenAndServe("0.0.0.0:8001", sayServer)
	select {}
}
