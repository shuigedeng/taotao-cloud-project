// EndPoint/endpoint.go
package EndPoint

import (
	"Songzhibin/go-kit-demo/v0/Server"
	"context"
	"github.com/go-kit/kit/endpoint"
)

// endpoint.go 定义 Request、Response 格式, 并且可以使用闭包来实现各种中间件的嵌套
// 这里了解 protobuf 的比较好理解点
// 就是声明 接收数据和响应数据的结构体 并通过构造函数创建 在创建的过程当然可以使用闭包来进行一些你想要的操作啦

// 这里根据我们Demo来创建一个响应和请求
// 当然你想怎么创建怎么创建 也可以共用 这里我分开写 便于大家看的清楚

// Hello 业务使用的请求和响应格式
// HelloRequest 请求格式
type HelloRequest struct {
	Name string `json:"name"`
}

// HelloResponse 响应格式
type HelloResponse struct {
	Reply string `json:"reply"`
}

// Bye 业务使用的请求和响应格式
// ByeRequest 请求格式
type ByeRequest struct {
	Name string `json:"name"`
}

// ByeResponse 响应格式
type ByeResponse struct {
	Reply string `json:"reply"`
}

// ------------ 当然 也可以通用的写 ----------
// Request 请求格式
type Request struct {
	Name string `json:"name"`
}

// Response 响应格式
type Response struct {
	Reply string `json:"reply"`
}

// 这里创建构造函数 hello方法的业务处理
// MakeServerEndPointHello 创建关于业务的构造函数
// 传入 Server/server.go 定义的相关业务接口
// 返回 go-kit/endpoint.Endpoint (实际上就是一个函数签名)
func MakeServerEndPointHello(s Server.IServer) endpoint.Endpoint {
	// 这里使用闭包,可以在这里做一些中间件业务的处理
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		// request 是在对应请求来时传入的参数(这里的request 实际上是等下我们要将的Transport中一个decode函数中处理获得的参数)
		// 这里进行以下断言
		r, ok := request.(HelloRequest)
		if !ok {
			return Response{}, nil
		}
		// 这里实际上就是调用我们在Server/server.go中定义的业务逻辑
		// 我们拿到了 Request.Name 那么我们就可以调用我们的业务 Server.IServer 中的方法来处理这个数据并返回
		// 具体的业务逻辑具体定义....
		return HelloResponse{Reply: s.Hello(r.Name)}, nil
		// response 这里返回的response 可以返回任意的 不过根据规范是要返回我们刚才定义好的返回对象

	}
}

// 这里创建构造函数 Bye方法的业务处理
// MakeServerEndPointBye 创建关于业务的构造函数
// 传入 Server/server.go 定义的相关业务接口
// 返回 go-kit/endpoint.Endpoint (实际上就是一个函数签名)
func MakeServerEndPointBye(s Server.IServer) endpoint.Endpoint {
	// 这里使用闭包,可以在这里做一些中间件业务的处理
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		// request 是在对应请求来时传入的参数(这里的request 实际上是等下我们要将的Transport中一个decode函数中处理获得的参数)
		// 这里进行以下断言
		r, ok := request.(ByeRequest)
		if !ok {
			return Response{}, nil
		}
		// 这里实际上就是调用我们在Server/server.go中定义的业务逻辑
		// 我们拿到了 Request.Name 那么我们就可以调用我们的业务 Server.IServer 中的方法来处理这个数据并返回
		// 具体的业务逻辑具体定义....
		return ByeResponse{Reply: s.Bye(r.Name)}, nil
		// response 这里返回的response 可以返回任意的 不过根据规范是要返回我们刚才定义好的返回对象
	}
}
