
var UserModel=require('./model/user.js');


// var user = new UserModel({
//     name: '赵六',
//     sn:'123456781',
//     age: 29
// });
// user.save();




// UserModel.findBySn('123456781',function(){})



UserModel.findBySn('123456782',function(err,docs){

    if(err){

        console.log(err);

        return;
    }
    console.log(docs)
})




var user = new UserModel({
    name: '赵六',
    sn:'123456781',
    age: 29
});
// user.save();


user.print();   //自定义的实例方法