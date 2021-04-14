/*
1、https://www.npmjs.com/package/silly-datetime

2、npm i silly-datetime --save

3、var sd = require('silly-datetime');

4、看文档使用

*/


var sd = require('silly-datetime');
var d=sd.format(new Date(), 'YYYY-MM-DD HH:mm');

console.log(d);