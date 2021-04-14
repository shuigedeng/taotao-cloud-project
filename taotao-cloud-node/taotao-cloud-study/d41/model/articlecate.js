var mongoose=require('./db.js');


var ArticleCateSchema = new mongoose.Schema({
    title  : { type: String, unique: true },
    descripton:String,
    addtime:{
        type:Date       
    }
});


module.exports=mongoose.model('ArticleCate',ArticleCateSchema,'articlecate');


