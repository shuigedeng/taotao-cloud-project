var mongoose=require('./db.js');


//mongoose数据校验:用户通过mongoose给mongodb数据库增加数据的时候，对数据的合法性进行的验证

//mongoose里面定义Schema:字段类型，修饰符、默认参数 、数据校验都是为了数据库数据的一致性



//Schema，为数据库对象的集合,每个schema会映射到mongodb中的一个collection,定义Schema可以理解为表结构的定义

var UserSchema=mongoose.Schema({
    name:{
        type:String,//指定类型
        trim:true,   //修饰符         
        required:true      
    },
    sn:{
        type:String,
        index:true,  //索引.
        set(val){  //自定义修饰符
            return val;
        },

        // maxlength:20,
        // minlength:10
        // match:/^sn(.*)/ ,
        validate: function(sn) {
            return sn.length >= 10;
        }        
        
    },   
    age:{
        type:Number,
        min:0,    //用在number类型上面
        max:150
    },       
    status:{
        type:String, 
        default:'success', //默认值
        enum:['success','error']   //status的值必须在 对应的数组里面  注意枚举是用在String
    }
})


module.exports=mongoose.model('User',UserSchema,'user');


