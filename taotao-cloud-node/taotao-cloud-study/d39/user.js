
var UserModel=require('./model/user.js');


var user = new UserModel({  
    name:'赵四',
    sn:'sn12232421433',
    age: 30,   
    status:'error'
  
});
user.save(function(err){

    if(err){

        console.log(err);
        return;
    }
    console.log('成功')
});









// UserModel.findBySn('123456781',function(){})

        /*
        UserModel.findBySn('123456782',function(err,docs){

            if(err){

                console.log(err);

                return;
            }
            console.log(docs)
        })
    */




    /*
        var user = new UserModel({
            name: '赵六',
            sn:'123456781',
            age: 29
        });


        user.print();   //自定义的实例方法
    */