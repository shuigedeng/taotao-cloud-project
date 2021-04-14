var mongoose=require('./db.js');
var Schema=mongoose.Schema;

var ArticleSchema = new Schema({
    title:{ 
        
        type: String, unique: true 
    
    },
    cid : { 
        
        type: Schema.Types.ObjectId
    
    },   /*分类 id*/

    author_id:{        
        type: Schema.Types.ObjectId
    },   /*用户的id*/
    author_name:{        
        type:String      
    },
    descripton:String,   
    content   : String
});


module.exports=mongoose.model('Article',ArticleSchema,'article');