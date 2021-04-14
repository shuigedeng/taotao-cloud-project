var mongoose=require('./db.js');
var Schema=mongoose.Schema;

var ArticleSchema = new Schema({
    title:{ 
        
        type: String, unique: true     
    },
    cid : { 
        
        type: Schema.Types.ObjectId,   
        ref:"ArticleCate"    //cid和 文章分类建立关系。   model    
    },   /*分类 id*/

    author_id:{        
        type: Schema.Types.ObjectId ,
        ref:"User"    //author_id和 用户表建立关系。   model
    },   /*用户的id*/
    author_name:{        
        type:String      
    },
    descripton:String,   
    content   : String
});


module.exports=mongoose.model('Article',ArticleSchema,'article');