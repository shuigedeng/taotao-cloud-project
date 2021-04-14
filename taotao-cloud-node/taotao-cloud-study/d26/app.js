const express = require('express')

const app=express()

app.get("/",(req,res)=>{
    res.send("你好 express")
})

app.get("/article",(req,res)=>{
    res.send("新闻页面")
})

app.get("/login",(req,res)=>{
    res.send("登录")
})

app.get("/register",(req,res)=>{  //get:显示数据
    res.send("注册页面")
})

app.post("/doLogin",(req,res)=>{   //post:增加数据
    console.log("执行登录")
    res.send("执行登录")
})

app.put("/editUser",(req,res)=>{  //put：主要用于修改数据
    console.log("修改用户")
    res.send("修改用户")
})

app.delete("/deleteUser",(req,res)=>{  //delete：主要用于删除数据
    console.log("执行删除")
    res.send("执行删除")
})

//路由里面配置多级目录  http://localhost:3000/admin/user/edit
app.get("/admin/user/add",(req,res)=>{
    res.send("admin user add")
})

app.get("/admin/user/edit",(req,res)=>{
    res.send("admin user  edit")
})

//动态路由  配置路由的时候也要注意顺序
app.get("/article/add",(req,res)=>{
    res.send("article add")
})

app.get("/article/:id",(req,res)=>{
    var id=req.params["id"]    //获取动态路由
    res.send("动态路由"+id)
})

//get 传值  http://localhost:3000/product?id=123&cid=123
app.get("/product",(req,res)=>{
    let query = req.query   //获取get传值
    console.log(query)
    res.send("product-"+query.id)
})

app.listen(3000)