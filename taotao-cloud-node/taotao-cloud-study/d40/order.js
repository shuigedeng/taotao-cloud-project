/*
db.order.aggregate([
    {
      $lookup:
        {
          from: "order_item",
          localField: "order_id",
          foreignField: "order_id",
          as: "items"
        }
   },
{
    $match:{"all_price":{$gte:90}}
}

])

*/







var OrderModel=require('./model/order.js');


//查询order 表的数据

/*
    OrderModel.find({},function(err,docs){

        console.log(docs);

    })
*/



//order表关联order_item
OrderModel.aggregate([

    {
        $lookup:
          {
            from: "order_item",
            localField: "order_id",
            foreignField: "order_id",
            as: "items"
          }
     },
     {
        $match:{"all_price":{$gte:90}}
     }

],function(err,docs){

    if(err){

        console.log(err);
        return;
    }

    console.log(JSON.stringify(docs))
})