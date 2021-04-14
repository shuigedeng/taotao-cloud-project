const http = require('http');
const tools = require('./module/tools.js');
console.log(tools);

http.createServer((req, res) => {
    res.writeHead(200, { "Content-type": "text/html;charset='utf-8'" });
    res.write("<head> <meta charset='UTF-8'></head>");
    res.write('你好 nodejs<br>');
    var api = tools.formatApi('api/focus');
    res.write(api);
    res.end();
}).listen(3000);


/*
1.我们可以把公共的功能抽离成为一个单独的 js 文件作为一个模块，
默认情况下面这 个模块里面的方法或者属性，外面是没法访问的。
如果要让外部可以访问模块里面的方法或 者属性，
就必须在模块里面通过 exports 或者 module.exports 暴露属性或者方法。

2. 在需要使用这些模块的文件中，通过 require 的方式引入这个模块。
这个时候就可 以使用模块里面暴露的属性和方法。
*/