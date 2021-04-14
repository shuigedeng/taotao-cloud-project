//1.引入mongoose

const mongoose = require('mongoose');


//2、建立连接  
mongoose.connect('mongodb://127.0.0.1:27017/eggcms');



//3、操作users表（集合）   定义一个Schema   Schema里面的对象和数据库表里面的字段需要一一对应

var UserSchema=mongoose.Schema({

    name:String,
    age:Number,
    status:Number

})



//4、定义数据库模型  操作数据库

// model里面的第一个参数 要注意：1首字母大写  2、要和数据库表（集合 ）名称对应  这个模型会和模型名称相同的复数的数据库表建立连接

// var User=mongoose.model('User',UserSchema);    // 默认会操作 users表（集合）

var User=mongoose.model('User',UserSchema,'user');  //默认会操作第三个参数配置的表  user表（集合）

//5、查询users表的数据


    // User.find({},function(err,doc){ 

    //     if(err){

    //         console.log(err);
    //         return;
    //     }
    //     console.log(doc);
    // })  



//6、增加数据

    // 6.1实例化 Model     通过实例化User Molde 创建增加的数据

    //6.2 实例.save()


    // var u=new User({
    //     name:'李四',
    //     age:20,
    //     status:1
    // });

    // u.save(function(err){
    //     if(err){
    //         console.log(err);

    //         return;
    //     }

    //     console.log('成功')

    // });  //执行增加操作





//6、更新数据


    

