const url = require("url");

function changeRes(res) {
    res.send = (data) => {
        res.writeHead(200, {'Content-Type': 'text/html;charset="utf-8"'});
        res.end(data);
    }
}

let server = () => {
    let G = {};
    //把get 和 post分开
    G._get = {};
    G._post = {};

    let app = function (req, res) {
        //扩展res的方法
        changeRes(res);

        let pathname = url.parse(req.url).pathname;
        //获取请求类型
        let method = req.method.toLowerCase();
        console.log(method);

        if (G['_' + method][pathname]) {

            if (method == "get") {
                G['_' + method][pathname](req, res);  //执行方法
            } else {
                //post  获取post的数据 把它绑定到req.body
                let postData = '';
                req.on('data', (chunk) => {
                    postData += chunk;
                })
                req.on('end', () => {
                    req.body = postData;
                    G['_' + method][pathname](req, res);  //执行方法
                })

            }

        } else {
            res.writeHead(404, {'Content-Type': 'text/html;charset="utf-8"'});
            res.end('页面不存在');
        }
    }
    app.get = function (str, cb) {
        //注册方法
        G._get[str] = cb;
    }
    app.post = function (str, cb) {
        //注册方法
        G._post[str] = cb;
    }

    return app;
}
module.exports = server();
