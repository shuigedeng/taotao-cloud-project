

var ArticleCateModel=require('./model/articlecate.js');


var UserModel=require('./model/user.js');


var ArticleModel=require('./model/article.js');


//分类的增加
  // var cate=new ArticleCateModel({

  //   title:'国内新闻',
  //   description:'国内新闻'
  // })

  // cate.save();


  //增加用户


  // var user= new UserModel({
  //   username  :'lisi',
  //   password:'13214lkisisgfdsgsdsg',
  //   name:'李四',
  //   age:20,
  //   sex:'男',
  //   tel:124212142151
  // })
  // user.save();






  var article=new ArticleModel();
  article.title="这是一个国内新闻11111111"

  article.cid='5b7900bbf3965813d41216c1';   /*国内新闻*/
  article.author_id='5b7901332d552617b09af422';  
  article.author_name='张三';
  article.descripton='这是一个国内新闻11111111 此处省略300字';
  article.content='习近平访问美国 这是一个国内新闻11111111'
  

  article.save();