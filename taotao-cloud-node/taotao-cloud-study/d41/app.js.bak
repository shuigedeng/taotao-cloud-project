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


var ArticleModel=require('./model/article.js');
//查询文章信息

  /*
  ArticleModel.find({},function(err,docs){

    console.log(docs);
  })

  */

//查询文章信息 并显示文章的分类 以及文章的作者信息


//两个表关联查询
  /*
    ArticleModel.aggregate([

      {

        $lookup: {
          from: "articlecate",
          localField: "cid",
          foreignField: "_id",
          as: "cate"
        }
      }

    ],function(err,docs){

      console.log(docs[2].cate)
    })
  */

//三个表关联查询
 ArticleModel.aggregate([
  {

    $lookup: {
      from: "articlecate",
      localField: "cid",
      foreignField: "_id",
      as: "cate"
    }
  },
  {

    $lookup: {
      from: "user",
      localField: "author_id",
      foreignField: "_id",
      as: "user"
    }
  }

],function(err,docs){

  console.log(JSON.stringify(docs));
})