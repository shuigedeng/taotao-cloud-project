var mongoose=require('./db.js');


var OrderSchema=mongoose.Schema({

    order_id:String,
    uid:Number,
    trade_no:String,
    all_price:Number,
    all_num:Number    
})

module.exports=mongoose.model('Order',OrderSchema,'order');


