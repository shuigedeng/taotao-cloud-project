# ElasticSearch集成

## 介绍
ElasticSearch是一个基于Lucene的搜索服务器。它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java语言开发的，并作为Apache许可条款下的开放源码发布，是一种流行的企业级搜索引擎。
[更多详情](https://www.elastic.co/cn/products/elasticsearch)

本系统用于统一封装ES的客户端，简化业务使用。
## 依赖引入

```java 
<dependency>
	<artifactId>csx-bsf-elasticsearch</artifactId>
	<groupId>com.yh.csx.bsf</groupId>
	<version>1.7.1-SNAPSHOT</version>
</dependency>
```
## 配置说明


## 代码示例

##### 1. 定义索引对应的实体对象,并基于该对象定义索引的Mapping规则
    
   mapping build部分参考了https://github.com/gitchennan/elasticsearch-mapper的设计思路,使用声明式注解定义mapping规则

   在原作者基础上升级了ElasticSearch的版本到6.7.1,并且移除了一些新版本废弃的特性
        
   对mapper部分进行了抽象重写

```

@Document
public class OrderVo implements ElasticSearchAware {
    
    @NumberField(type = NumberType.Integer)
    private Integer orderId;
    
    @StringField(type = StringType.Keyword)
    private String orderNo;
    
    @StringField(type = StringType.Text, analyzer = "ik_smart", index = true, store = false)
    private String productName;
    
    /**
     * 下单人 没注解也会自动映射
     */
    private String createBy;
    
    @NumberField(type = NumberType.Double)
    private BigDecimal amount;
    
    /**
     *  下单地址
     */
    @GeoPointField
    private GeoPoint orderPostion;
    
    /*
     * 单字段多映射
     */
    @MultiField(
            mainField = @StringField(type = StringType.Keyword, boost = 2.0f),
            fields = {
                    @MultiNestedField(name = "cn", field = @StringField(type = StringType.Text, analyzer = "ik_smart")),
                    @MultiNestedField(name = "en", field = @StringField(type = StringType.Text, analyzer = "english")),
            },
            tokenFields = {
                    @TokenCountField(name = "cnTokenCount", analyzer = "ik_smart")
            }
    )
    private String orderDesc;
    
    /**
     *     实现ElasticSearchAware接口, 标识ID列  
     *     批量insert的时候会根据这个id来自动做新增或者更新
     */
    @Override
    public String get_id() {
        return orderId.toString();
    }
    /... getter and setter .../
 }
```

##### 2.创建ElasticSearch索引,设置mapping

```    
    @Autowired
    private ElasticSearchSqlProvider searchProvider;
    
    
    /*
     * 方式一-----创建index的时候设置mapping
     */
    public void createIndex(){
        searchProvider.createIndex("orderindex", "ordertype", OrderVo.class);
    }
    
    /*
     * 方式二第一步-----单独创建索引,不设置mapping
     */
    public void createIndexWithoutMapping(){
        searchProvider.createIndex("orderindex", "ordertype");
    }
    
    /*
     * 方式二第二步-----为已有索引设置mapping
     */
    public void putMapping(){
        searchProvider.putMapping("orderindex", "ordertype", OrderVo.class);
    }
  ```
##### 3.增删改查

```    
    @Autowired
    private ElasticSearchSqlProvider searchProvider;
    
    /*
     * 批量保存数据
     */
    public void insert() {
        List<OrderVo> orderList = new ArrayList<OrderVo>();
        
        OrderVo order = new OrderVo();
        order.setOrderId(1000001);
        order.setOrderNo("ON1000001");
        order.setProductName("苹果水果新鲜当季整箱陕西红富士应季");
        order.setCreateBy("刘某某");
        order.setAmount(new BigDecimal(1024.00));
        order.setOrderPostion(new GeoPoint(30.0f, 20.0f));
        order.setOrderDesc("哈哈我也不知道要写什么苹果,反正先来个50斤吧");
        orderList.add(order);
        
        order = new OrderVo();
        order.setOrderId(1000002);
        order.setOrderNo("ON1000002");
        order.setProductName("四川丑橘新鲜水果当季整箱10斤大果丑八怪橘子丑桔丑柑耙耙柑包邮");
        order.setCreateBy("车某某");
        order.setAmount(new BigDecimal(888.88));
        order.setOrderPostion(new GeoPoint(70.0f, 60.0f));
        order.setOrderDesc("要新鲜的甜的丑橘，不要发错了！！！！");
        orderList.add(order);
        
        searchProvider.insertData("orderindex", "ordertype", orderList);
    }
    
   /**
     * 模糊搜索商品名称匹配苹果的数据,返回的数据按相关性得分从高到低排序并且取前3条数据
     * 更多的sql语法请参考官方示例 https://github.com/NLPchina/elasticsearch-sql
     * Elasticsearch-sql官方提供了一个用于测试sql的网页客户端,非常方便测试sql的写法
     */
    public List<OrderVo> search() {
        List<OrderVo> orderList = provider.searchBySql("select * from orderindex where productName = matchQuery('苹果') ORDER BY _score DESC LIMIT 3", OrderVo.class);
        return orderList;
    }
    
   /**
     * 普通查询写法 
     */
    public List<OrderVo> selectByIndex() {
        List<OrderVo> orderList = provider.searchBySql("select * from orderindex where createBy = '车某某' and amount > 500 LIMIT 3", OrderVo.class);
        return orderList;
    }
    
   /**
     *  查询type写法
     *  SELECT * FROM indexName/type
     */
    public List<OrderVo> selectByType() {
        List<OrderVo> orderList = provider.searchBySql("select * from orderindex/ordertype where createBy = '刘某某' and amount > 500 LIMIT 3", OrderVo.class);
        return orderList;
    }
    
    /**
     * 删除写法
     */
    public void delete() {
        provider.deleteBySql("delete from orderindex where orderId = 1000001");
    }
  ```
