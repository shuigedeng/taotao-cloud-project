const http = require("http");
const app = require('./module/route');
const ejs = require("ejs");
const querystring = require('querystring');
const { MongoClient } = require('mongodb');
const url = 'mongodb://localhost:27017';
const dbName = 'itying';
// const client = new MongoClient(url,{ useUnifiedTopology: true });
//注册web服务
http.createServer(app).listen(3000);
// app.static("public");    //修改默认静态web目录
//配置路由
app.get('/', function (req, res) {

    MongoClient.connect(url,{ useUnifiedTopology: true }, (err, client) => {
        if (err) {
            console.log(err);
            return;
        }
        let db = client.db(dbName);

        //查询数据
        db.collection("user").find({}).toArray((err, result) => {
            if (err) {
                console.log(err);
                return;
            }            
            client.close();
            ejs.renderFile("./views/index.ejs", {
                list: result
            }, (err, data) => {
                res.send(data);
            })
        })

    })


})


app.get('/register', function (req, res) {
    ejs.renderFile("./views/register.ejs",{},(err,data)=>{
        res.send(data);
    })
})

app.post('/doRegister', function (req, res) {
    // name=zhangsan&age=13
    // {
    //     "name":"zhangsan",
    //     "age":13
    // }    
    let body=querystring.parse(req.body);
    MongoClient.connect(url,{ useUnifiedTopology: true },(err,client)=>{

        if(err){
            console.log(err);
            return;
        }
        let db=client.db(dbName);

        db.collection("user").insertOne(body,(err,result)=>{
            if(err){
                console.log(err);
                return;
            }
            console.log("增加数据成功");
            res.send("增加数据成功");
        })

    })
   
})