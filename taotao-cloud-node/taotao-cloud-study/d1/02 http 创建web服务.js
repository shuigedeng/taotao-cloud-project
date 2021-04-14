const http =require('http');

/*
    req   获取客户端传过来的信息
    res  给浏览器响应信息
*/

http.createServer((req,res)=>{

    console.log(req.url);  //获取url

    //设置响应头
    //状态码是 200，文件类型是 html，字符集是 utf-8
    res.writeHead(200,{"Content-type":"text/html;charset='utf-8'"}); //解决乱码

    res.write("<head> <meta charset='UTF-8'></head>");  //解决乱码

    res.write('你好 nodejs');

    res.write('<h2>你好 nodejs</h2>');

    res.end();  //结束响应

}).listen(3000);