/*
获取post传过来的数据
1、cnpm install body-parser --save

2、var bodyParser = require('body-parser')

3、配置中间件

    app.use(bodyParser.urlencoded({ extended: false }))

    app.use(bodyParser.json())
4、接收post数据
    req.body

*/

const express = require("express");
const bodyParser = require('body-parser')
const ejs = require("ejs");
const app = express()

//配置模板引擎
app.engine("html",ejs.__express)
app.set("view engine","html")
//配置静态web目录
app.use(express.static("static"))
//配置第三方中间件
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())

app.get("/",(req,res)=>{
    res.send("首页")
})

app.get("/login",(req,res)=>{
   // req.query 获取get传值
   res.render("login",{})
})

app.post("/doLogin",(req,res)=>{
   // req.body 获取post传值
   var body = req.body;
   console.log(body)
   res.send("执行提交"+body.username)
})

//监听端口  端口号建议写成3000以上
app.listen(3000)