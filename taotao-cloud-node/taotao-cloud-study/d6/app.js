//练习：wwwroot文件夹下面有images css js 以及index.html , 找出 wwwroot目录下面的所有的目录，然后放在一个数组中

const fs = require('fs');

//错误的写法  注意:fs里面的方法是异步
/*
    var path='./wwwroot';
    var dirArr=[];
    fs.readdir(path,(err,data)=>{
        if(err){
            console.log(err);
            return;
        }    
        for(var i=0;i<data.length;i++){
            fs.stat(path+'/'+data[i],(error,stats)=>{
                if(stats.isDirectory()){
                    dirArr.push(data[i]);
                }
            })
        }
        console.log(dirArr);
    })
    console.log(dirArr);
*/

//打印出 3个3
// for(var i=0;i<3;i++){
// setTimeout(function(){
//     console.log(i);
// },100)
// }

//1、改造for循环  递归实现      2、nodejs里面的新特性  async await

var path = './wwwroot';
var dirArr = [];
fs.readdir(path, (err, data) => {
    if (err) {
        console.log(err);
        return;
    }
    (function getDir(i) {
        if (i == data.length) { //执行完成
            console.log(dirArr);
            return;
        }
        fs.stat(path + '/' + data[i], (error, stats) => {
            if (stats.isDirectory()) {
                dirArr.push(data[i]);
            }
            getDir(i + 1)
        })
    })(0)
})
