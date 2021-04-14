var mongoose=require('./db.js');


var UserSchema = new mongoose.Schema({
    username  : { type: String, unique: true },
    password:String,
    name:String,
    age:Number,
    sex:String,
    tel:Number,
    status:{
        type:Number,
        default:1
    }
});

module.exports=mongoose.model('User',UserSchema,'user');


