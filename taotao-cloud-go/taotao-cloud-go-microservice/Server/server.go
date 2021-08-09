// Server/server.go
package Server

import "fmt"

// server.go 实现业务

// IServer 用于定义业务方法的接口
type IServer interface {
	// 这里只需要关注我 IServer 对业务所需要的方法即可
	// 例如: 我这里要实现一个问候的方法 和一个 bye的方法 比较简单 传入一个名字 返回一个名字
	Hello(name string) string
	Bye(name string) string
}

// Server 用于实现上面定义的接口
type Server struct {
	// 根据业务需求填充结构体...
}

// 实现上方定义的业务方法

func (s Server) Hello(name string) string {
	return fmt.Sprintf("%s:Hello", name)
}

func (s Server) Bye(name string) string {
	return fmt.Sprintf("%s:Bye", name)
}
