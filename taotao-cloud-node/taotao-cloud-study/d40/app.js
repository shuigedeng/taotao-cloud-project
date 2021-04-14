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

/*查询order_item，找出商品名称是酸奶的商品，酸奶这个商品对应的订单的订单号以及订单的总价格*/

var OrderItemModel = require('./model/order_item.js');

var OrderModel = require('./model/order.js');

var mongoose = require('mongoose');

//第一种实现方式
/*
    OrderItemModel.find({"_id":"5b743da92c327f8d1b360546"},function(err,docs){

        // console.log(docs);

        var order_item=JSON.parse(JSON.stringify(docs));

        var order_id=order_item[0].order_id;


        OrderModel.find({"order_id":order_id},function(err,order){

            //    console.log(order);

            order_item[0].order_info=order[0];


            console.log(order_item)
        })
    })

*/

//第二种方式 

//mongoose中获取ObjectId           mongoose.Types.ObjectId

OrderItemModel.aggregate([

    {
        $lookup:
            {
                from: "order",
                localField: "order_id",
                foreignField: "order_id",
                as: "order_info"
            }
    }, {
        $match: {_id: mongoose.Types.ObjectId('5b743da92c327f8d1b360546')}

    }

], function (err, docs) {

    if (err) {

        console.log(err)
        return;
    }

    console.log(JSON.stringify(docs))

})
