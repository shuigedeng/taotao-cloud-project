const fs = require('fs');
const path = require('path');
const url = require('url');
const ejs = require('ejs');

//私有方法
let getFileMime = function (extname) {

    var data = fs.readFileSync('./data/mime.json'); //同步方法
    let mimeObj = JSON.parse(data.toString());
    return mimeObj[extname];

}

let app = {
    static: (req, res,staticPath) => {
        //1、获取地址
        let pathname = url.parse(req.url).pathname;      
        let extname = path.extname(pathname);
      
        //2、通过fs模块读取文件
        if (pathname != '/favicon.ico' && extname) {
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

    },
    login: (req, res) => {
      ejs.renderFile('./views/form.ejs',{},(err,data)=>{
        res.writeHead(200, { 'Content-Type': 'text/html;charset="utf-8"' });
        res.end(data)
      })
    },
    news: (req, res) => {       
        res.end('news');
    },
    doLogin:(req, res) => {       
         //获取post传值        
         let postData = '';
         req.on('data',(chunk)=>{
             postData+=chunk;
         })
         req.on('end',()=>{
            console.log(postData);
            res.end(postData);
         })
         
    },error:(req, res) => {
        res.end('404');
    }
}


// app.login('req','res')
// app['login']('req','res')

module.exports=app;
