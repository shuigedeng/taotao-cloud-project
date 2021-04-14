const express = require("express");
const bodyParser = require('body-parser')
const ejs = require("ejs");
//引入外部模块
const admin = require("./routes/admin")
const index = require("./routes/index")
const api = require("./routes/api")

const app = express()
//配置模板引擎
app.engine("html",ejs.__express)
app.set("view engine","html")
//配置静态web目录
app.use(express.static("static"))
//配置第三方中间件获取post提交的数据
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())

//配置外部路由模块
app.use("/admin",admin)
app.use("/api",api)
app.use("/",index)

//监听端口  端口号建议写成3000以上
app.listen(3000)