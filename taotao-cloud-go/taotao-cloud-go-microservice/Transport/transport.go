// Transport/transport.go
package Transport

import (
	"Songzhibin/go-kit-demo/v0/EndPoint"
	"context"
	"encoding/json"
	"errors"
	"net/http"
)

// Transport/transport.go 主要负责HTTP、gRpc、thrift等相关的逻辑

// 这里有两个关键函数
// DecodeRequest & EncodeResponse 函数签名是固定的哟
// func DecodeRequest(c context.Context, request *http.Request) (interface{}, error)
// func EncodeResponse(c context.Context, w http.ResponseWriter, response interface{}) error

// HelloDecodeRequest 解码 后封装至 EndPoint中定义的 Request格式中
func HelloDecodeRequest(c context.Context, request *http.Request) (interface{}, error) {
	// 这里主要就是通过 request 拿到对应的参数构造成在 EndPoint中定义的 Request结构体即可

	name := request.URL.Query().Get("name")
	if name == "" {
		return nil, errors.New("无效参数")
	}
	// 这里返回的是
	return EndPoint.HelloRequest{Name: name}, nil
}

// HelloEncodeResponse 通过响应封装成 EndPoint中定义的 Response结构体即可
func HelloEncodeResponse(c context.Context, w http.ResponseWriter, response interface{}) error {
	// 这里将Response返回成有效的json格式给http

	// 设置请求头信息
	w.Header().Set("Content-Type", "application/json;charset=utf-8")
	// 使用内置json包转换
	return json.NewEncoder(w).Encode(response)
}

// ByeDecodeRequest 解码 后封装至 EndPoint中定义的 Request格式中
func ByeDecodeRequest(c context.Context, request *http.Request) (interface{}, error) {
	// 这里主要就是通过 request 拿到对应的参数构造成在 EndPoint中定义的 Request结构体即可

	name := request.URL.Query().Get("name")
	if name == "" {
		return nil, errors.New("无效参数")
	}
	// 这里返回的是
	return EndPoint.ByeRequest{Name: name}, nil
}

// sayEncodeResponse 通过响应封装成 EndPoint中定义的 Response结构体即可
func sayEncodeResponse(c context.Context, w http.ResponseWriter, response interface{}) error {
	// 这里将Response返回成有效的json格式给http

	// 设置请求头信息
	w.Header().Set("Content-Type", "application/json;charset=utf-8")
	// 使用内置json包转换
	return json.NewEncoder(w).Encode(response)
}
