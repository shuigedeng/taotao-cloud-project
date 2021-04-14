const url=require('url');

var api='http://www.itying.com?name=zhangsan&age=20';

// console.log(url.parse(api,true));

var getValue=url.parse(api,true).query;

console.log(getValue);

console.log(`姓名：${getValue.name}--年龄:${getValue.age}`);