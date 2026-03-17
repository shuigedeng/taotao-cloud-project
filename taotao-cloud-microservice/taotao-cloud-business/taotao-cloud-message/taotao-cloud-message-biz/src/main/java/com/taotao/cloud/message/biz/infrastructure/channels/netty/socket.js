
// 导出socket对象
export {
    socket
}
import {Message} from 'element-ui'
// socket主要对象
var socket = {
    websock: null,
    /**
     * 这个是我们的ws的地址
     * */
    ws_url: "ws://127.0.0.1:9000/ws",

    userid: null,

    msgfunc: null,

    /**
     * 开启标识
     * */
    socket_open: false,
    /**
     * 心跳timer
     * */
    hearbeat_timer: null,
    /**
     * 心跳发送频率
     * */
    hearbeat_interval: 10000,
    /**
     * 是否开启重连
     * */
    is_reonnect: true,
    /**
     * 重新连接的次数
     * */
    reconnect_count: 3,
    /**
     * 当前重新连接的次数，默认为：1
     * */
    reconnect_current: 1,
    /**
     * 重新连接的时间类型
     * */
    reconnect_timer: null,
    /**
     * 重新连接的间隔
     * */
    reconnect_interval: 3000,

    /**
     * 登录后才进行连接
     * */

    /**
     * 初始化连接
     */
    init: () => {

        let loginToken = localStorage.getExpire("LoginToken");
        let userid = localStorage.getExpire("userid");
        if(loginToken==null && userid==null) {
            Message({
                message: '当前正在以游客身份访问',
                type: 'info',
            });
            return ;
        }
        if (!("WebSocket" in window)) {
            Message({
                message: '当前浏览器与网站不兼容丫',
                type: 'error',
            });
            console.log('浏览器不支持WebSocket')
            return null
        }

        // 已经创建过连接不再重复创建
        if (socket.websock) {
            return socket.websock
        }

        socket.websock = new WebSocket(socket.ws_url)
        socket.websock.onmessage = function (e) {
            socket.receive(e)
        }

        // 关闭连接
        socket.websock.onclose = function (e) {
            console.log('连接已断开')
            console.log('connection closed (' + e.code + ')')
            clearInterval(socket.hearbeat_interval)
            socket.socket_open = false

            // 需要重新连接
            if (socket.is_reonnect) {
                socket.reconnect_timer = setTimeout(() => {
                    // 超过重连次数
                    if (socket.reconnect_current > socket.reconnect_count) {
                        clearTimeout(socket.reconnect_timer)
                        return
                    }

                    // 记录重连次数
                    socket.reconnect_current++
                    socket.reconnect()
                }, socket.reconnect_interval)
            }
        }

        // 连接成功
        socket.websock.onopen = function () {
            Message({
                message: 'Welcome here',
                type: 'success',
            });
            let userid = localStorage.getExpire("userid");
            socket.userid = userid;
            console.log('连接成功')
            socket.socket_open = true
            socket.is_reonnect = true

            // 开启心跳
            socket.heartbeat()

            //注册用户
            let resit={
                "action": 1,
                "userid": userid
            }
            socket.send(resit)
        }
        // 连接发生错误
        socket.websock.onerror = function (err) {
            Message({
                message: '无法连接至服务器！',
                type: 'error',
            });
            console.log('WebSocket连接发生错误')
        }
    },
    /**
     * 获取websocket对象
     * */

    getSocket:()=>{
        //创建了直接返回，反之重来
        if (socket.websock) {
            return socket.websock
        }else {
            socket.init();
        }
    },

    getStatus:()=> {
        if (socket.websock.readyState === 0) {
            return "未连接";
        } else if (socket.websock.readyState === 1) {
            return "已连接";
        } else if (socket.websock.readyState === 2) {
            return "连接正在关闭";
        } else if (socket.websock.readyState === 3) {
            return "连接已关闭";
        }
    },

    /**
     * 发送消息
     * @param {*} data 发送数据
     * @param {*} callback 发送后的自定义回调函数
     */
    send: (data, callback = null) => {
        // 开启状态直接发送
        if (socket.websock.readyState === socket.websock.OPEN) {
            socket.websock.send(JSON.stringify(data))

            if (callback) {
                callback()
            }

            // 正在开启状态，则等待1s后重新调用
        } else if (socket.websock.readyState === socket.websock.CONNECTING) {
            setTimeout(function () {
                socket.send(data, callback)
            }, 1000)

            // 未开启，则等待1s后重新调用
        } else {
            socket.init()
            setTimeout(function () {
                socket.send(data, callback)
            }, 1000)
        }
    },

    /**
     * 接收消息
     * @param {*} message 接收到的消息
     */
    receive: (message) => {
        var recData = JSON.parse(message.data)
        /**
         *这部分是我们具体的对消息的处理
         * */
        if(socket.msgfunc==null){
            Message({
                message: 'receive需要传入一个func进行全局消息处理！',
                type: 'error',
            });
        }else {
            socket.msgfunc(recData)
        }
    },

    /**
     * 心跳
     */
    heartbeat: () => {
        console.log('socket', 'ping')
        if (socket.hearbeat_timer) {
            clearInterval(socket.hearbeat_timer)
        }

        socket.hearbeat_timer = setInterval(() => {
            //发送心跳包
            let data = {
                "action": 4,
                "userid": socket.userid
            }
            socket.send(data)
        }, socket.hearbeat_interval)
    },

    /**
     * 主动关闭连接
     */
    close: () => {
        console.log('主动断开连接')
        clearInterval(socket.hearbeat_interval)
        socket.is_reonnect = false
        socket.websock.close()
    },

    /**
     * 重新连接
     */
    reconnect: () => {
        console.log('发起重新连接', socket.reconnect_current)

        if (socket.websock && socket.socket_open) {
            socket.websock.close()
        }

        socket.init()
    },
}

