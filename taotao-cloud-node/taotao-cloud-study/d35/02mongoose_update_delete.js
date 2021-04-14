//1.引入mongoose

const mongoose = require('mongoose');


//2、建立连接  
mongoose.connect('mongodb://127.0.0.1:27017/eggcms');


//3、定义一个Schema 


var NewsSchema=mongoose.Schema({
    title:"string",
    author:String,
    pic:String,
    content:String,
    status:Number
})


//4、定义操作数据库的Model


var News=mongoose.model('News',NewsSchema,'news');



//5、增加数据

//通过实例化 Model 创建增加的数据
    // var news=new News({
    //     title:"我是一个新闻11111",
    //     author:'张三1',
    //     content:'我是新闻的内容',
    //     status:1

    // });

    // news.save(function(err){
    //     if(err){
    //         return console.log(err);
    //     }

    //      console.log('成功')
    // });


//6、修改数据

// News.updateOne(
//     {"_id":"5b7563e2ba3c6747d0612204"},   
//     {"title":"我是一个新闻2222"},
//     function(err,doc){
//         if(err){
//                 return console.log(err);
//             }

//             console.log(doc)

//     })


//删除数据


News.deleteOne({"_id":"5b7563e2ba3c6747d0612204"}, (err,result)=>{

      
            if(err){
                return console.log(err);
            }

            console.log(result)
})