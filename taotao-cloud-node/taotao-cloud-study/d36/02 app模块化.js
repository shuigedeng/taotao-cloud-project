var UserModel=require('./model/user.js');


var NewsModel=require('./model/news.js');



var user=new UserModel({

    name:"李四666",
    age:40
})


user.save(function(err){

    if(err){

        console.log(err);
        return;
    }

    //获取user表的数据


    UserModel.find({},function(err,docs){

        if(err){
    
            console.log(err);
            return;
        }
        console.log(docs);
    })
})




// NewsModel.find({},function(err,docs){

//     if(err){

//         console.log(err);
//         return;
//     }
//     console.log(docs);
// })


