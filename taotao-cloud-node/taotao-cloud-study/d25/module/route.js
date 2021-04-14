const fs = require('fs');
const path = require('path');
const url = require('url');

//扩展res
function changeRes(res){
    res.send=(data)=>{
        res.writeHead(200, { 'Content-Type': 'text/html;charset="utf-8"' });
        res.end(data);
    }
}

//根据后缀名获取文件类型
function getFileMime(extname) {
    var data = fs.readFileSync('./data/mime.json'); //同步方法
    let mimeObj = JSON.parse(data.toString());
    return mimeObj[extname];

}
//静态web服务的方法
function initStatic(req, res, staticPath) {

    //1、获取地址
    let pathname = url.parse(req.url).pathname;
    pathname = pathname == '/' ? '/index.html' : pathname;
    let extname = path.extname(pathname);
    //2、通过fs模块读取文件
    try {
        let data = fs.readFileSync('./' + staticPath + pathname);
        if (data) {
            let mime = getFileMime(extname);
            res.writeHead(200, { 'Content-Type': '' + mime + ';charset="utf-8"' });
            res.end(data);
        }
    } catch (error) {

    }

}

let server = () => {
    let G = {
        _get:{},
        _post:{},
        staticPath:'static' //默认静态web目录
    };
   

    let app = function (req, res) {
        //扩展res的方法
        changeRes(res);
        //配置静态web服务
        initStatic(req, res,G.staticPath);

        let pathname = url.parse(req.url).pathname;
        //获取请求类型
        let method=req.method.toLowerCase();
        console.log(method);
        
        if (G['_'+method][pathname]) {

            if(method=="get"){ 
                G['_'+method][pathname](req, res);  //执行方法
            }else{
                //post  获取post的数据 把它绑定到req.body
                let postData = '';
                req.on('data',(chunk)=>{
                    postData+=chunk;
                })
                req.on('end',()=>{                  
                   req.body=postData;
                   G['_'+method][pathname](req, res);  //执行方法
                })
               
            }

        } else {
            res.writeHead(404, { 'Content-Type': 'text/html;charset="utf-8"' });
            res.end('页面不存在');
        }
    }
    //get请求
    app.get = function (str, cb) {
        //注册方法
        G._get[str] = cb;     
    }
    //post请求
    app.post = function (str, cb) {
        //注册方法
        G._post[str] = cb;      
    }
    //配置静态web服务目录
    app.static=function(staticPath){
        G.staticPath=staticPath;
    } 

    return app;
}
module.exports = server();