var mongoose=require('./db.js');


var NewsSchema=mongoose.Schema({
    title:"string",
    author:String,
    pic:String,
    content:String,
    status:{

        type:Number,
        default:1

    }
})


module.exports=mongoose.model('News',NewsSchema,'news');
