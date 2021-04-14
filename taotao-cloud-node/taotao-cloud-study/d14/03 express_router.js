const http = require("http");
const app=require('./module/route')

//注册web服务
http.createServer(app).listen(3000);

//配置路由
app.get('/',function(req,res){
    res.writeHead(200, { 'Content-Type': 'text/html;charset="utf-8"' });
    res.end('首页');
})

//配置路由
app.get('/login',function(req,res){
    res.writeHead(200, { 'Content-Type': 'text/html;charset="utf-8"' });
    res.end('执行登录操作');
})

//配置路由
app.get('/news',function(req,res){
    res.writeHead(200, { 'Content-Type': 'text/html;charset="utf-8"' });
    res.end('新闻页面');
})