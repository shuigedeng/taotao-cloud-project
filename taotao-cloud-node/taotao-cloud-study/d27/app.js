/*
1.在 app.js 的头上定义 ejs:,代码如下: 

var ejs = require('ejs'); 

2.注册 html 模板引擎代码如下： 

app.engine('html',ejs.__express);

3.将模板引擎换成 html 代码如下:

app.set('view engine', 'html'); 

4.修改模板文件的后缀为.html。

*/

const express = require("express");
const ejs = require("ejs");
const app = express()
//配置模板引擎
app.engine("html",ejs.__express)
app.set("view engine","html")
//配置静态web目录
app.use(express.static("static"))

app.get("/",(req,res)=>{
    let title = "你好ejs";
    res.render("index",{
        title:title
    })
})

app.get("/news",(req,res)=>{
    let userinfo={
        username:"张三",
        age:20
    }
    let article="<h3>我是一个h3</h3>"

    let list=["1111","22222","3333333"]

    let newsList=[
        {
            title:"新闻1111",          
        },
        {
            title:"新闻122222",          
        },
        {
            title:"新闻33331",          
        },
        {
            title:"新闻44444",          
        }
    ]

    res.render("news",{
        userinfo:userinfo,
        article:article,
        flag:true,
        score:60,
        list:list,
        newsList:newsList
    })
})


//监听端口  端口号建议写成3000以上
app.listen(3000)