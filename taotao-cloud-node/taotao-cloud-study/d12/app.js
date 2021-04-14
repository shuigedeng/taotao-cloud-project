const http = require('http');
const url = require('url');
const path = require('path');
const ejs = require('ejs');
const routes = require('./module/routes');


http.createServer(function (req, res) {
    //创建静态web服务
    routes.static(req, res, 'static');
    //路由
    let pathname = url.parse(req.url).pathname;
    //http://127.0.0.1:3000/news?page=2&id=1

    //获取请求类型
    console.log(req.method);

    let extname = path.extname(pathname);
    if (!extname) {   //如果有后缀名的话让静态web服务去处理 
        if (pathname == '/') {
            //获取get传值
            res.writeHead(200, { 'Content-Type': 'text/html;charset="utf-8"' });
            res.end('首页');

        }else if (pathname == '/news') {
            //获取get传值
            var query = url.parse(req.url, true).query;
            console.log(query.page);
            res.writeHead(200, { 'Content-Type': 'text/html;charset="utf-8"' });
            res.end('get传值获取成功');

        } else if (pathname == '/login') {
            //post演示

            ejs.renderFile("./views/form.ejs", {}, (err, data) => {
                res.writeHead(200, { 'Content-Type': 'text/html;charset="utf-8"' });
                res.end(data)
            })

        } else if (pathname == '/doLogin') {
            //获取post传值        
            let postData = '';
            req.on('data', (chunk) => {
                postData += chunk;
            })
            req.on('end', () => {
                console.log(postData);
                res.end(postData);
            })
        } else {
            res.writeHead(404, { 'Content-Type': 'text/html;charset="utf-8"' });
            res.end('404');
        }
    }

}).listen(3000);

console.log('Server running at http://127.0.0.1:3000/');