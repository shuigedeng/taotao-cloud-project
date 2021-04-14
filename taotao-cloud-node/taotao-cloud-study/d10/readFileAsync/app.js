const http = require('http');
const fs = require('fs');
const common = require('./module/common.js');
const path = require('path');
const url = require('url');

http.createServer(function (req, res) {
    //1、获取地址
    let pathname = url.parse(req.url).pathname;
    pathname = pathname == '/' ? '/index.html' : pathname;
    let extname = path.extname(pathname);
    //2、通过fs模块读取文件app.js
    // data
    // module
    // package.json
    // static
    if (pathname != '/favicon.ico') {
        fs.readFile('./static' + pathname, async (err, data) => {
            if (err) {
                res.writeHead(404,
                    {'Content-Type': 'text/html;charset="utf-8"'});
                res.end('404这个页面不存在');
            }
            let mime = await common.getFileMime(extname);
            res.writeHead(200,
                {'Content-Type': '' + mime + ';charset="utf-8"'});
            res.end(data);
        })
    }

}).listen(3000);

console.log('Server running at http://127.0.0.1:3000/');
