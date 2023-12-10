
#  责任链模式

责任链模式是开发过程中常用的一种设计模式，在SpringMVC、Netty等许多框架中都有实现。

我们日常的开发中要使用责任链模式，通常需要自己来实现。但自己临时实现的责任链既不通用，也很容易产生框架与业务代码耦合不清的问题，增加Code Review的成本。

Netty的代码向来以优雅著称，早年我在阅读Netty的源码时，萌生出将其责任链的实现，应用到业务开发中的想法。之后花了点时间将Netty中责任链的实现代码抽取出来，形成了本项目，也就是`pie`。

pie的核心代码均来自Netty，绝大部分的API与Netty是一致的。

pie是一个可快速上手的责任链框架，开发者只需要专注业务，开发相应的handler，即可完成业务的责任链落地。

一分钟学会、三分钟上手、五分钟应用，欢迎star。

pie源码地址：https://github.com/feiniaojin/pie.git

pie案例工程源码地址：https://github.com/feiniaojin/pie-example.git

[![vK8vJs.png](https://s1.ax1x.com/2022/08/07/vK8vJs.png)](https://imgtu.com/i/vK8vJs)

# 2. 快速入门


## 2.2 实现出参工厂

出参也就是执行结果，一般的执行过程都要求有执行结果返回。实现OutboundFactory接口，用于产生接口默认返回值。

例如：

```java
public class OutFactoryImpl implements OutboundFactory {
    @Override
    public Object newInstance() {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("ok");
        return result;
    }
}
```

## 2.3 实现handler接口完成业务逻辑

在pie案例工程( https://github.com/feiniaojin/pie-example.git )的**Example1**中，为了展示pie的使用方法，实现了一个虚拟的业务逻辑：CMS类项目修改文章标题、正文，大家不要关注修改操作放到两个handler中是否合理。

三个Hadnler功能如下：

**CheckParameterHandler**：用于参数校验。

**ArticleModifyTitleHandler**：用于修改文章的标题。

**ArticleModifyContentHandler**：用于修改文章的正文。

CheckParameterHandler的代码如下：

```java
public class CheckParameterHandler implements ChannelHandler {

    private Logger logger = LoggerFactory.getLogger(CheckParameterHandler.class);

    @Override
    public void channelProcess(ChannelHandlerContext ctx,
                               Object in,
                               Object out) throws Exception {

        logger.info("参数校验:开始执行");

        if (in instanceof ArticleTitleModifyCmd) {
            ArticleTitleModifyCmd cmd = (ArticleTitleModifyCmd) in;
            String articleId = cmd.getArticleId();
            Objects.requireNonNull(articleId, "articleId不能为空");
            String title = cmd.getTitle();
            Objects.requireNonNull(title, "title不能为空");
            String content = cmd.getContent();
            Objects.requireNonNull(content, "content不能为空");
        }
        logger.info("参数校验:校验通过,即将进入下一个Handler");
        ctx.fireChannelProcess(in, out);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause,
                                Object in,
                                Object out) throws Exception {
        logger.error("参数校验:异常处理逻辑", cause);
        Result re = (Result) out;
        re.setCode(400);
        re.setMsg("参数异常");
    }
}
```

ArticleModifyTitleHandler的代码如下：

```java
public class ArticleModifyTitleHandler implements ChannelHandler {

    private Logger logger = LoggerFactory.getLogger(ArticleModifyTitleHandler.class);

    @Override
    public void channelProcess(ChannelHandlerContext ctx,
                               Object in,
                               Object out) throws Exception {

        logger.info("修改标题:进入修改标题的Handler");

        ArticleTitleModifyCmd cmd = (ArticleTitleModifyCmd) in;

        String title = cmd.getTitle();
        //修改标题的业务逻辑
        logger.info("修改标题:title={}", title);

        logger.info("修改标题:执行完成,即将进入下一个Handler");
        ctx.fireChannelProcess(in, out);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause,
                                Object in,
                                Object out) throws Exception {
        logger.error("修改标题:异常处理逻辑");
        Result re = (Result) out;
        re.setCode(1501);
        re.setMsg("修改标题发生异常");
    }
}
```

ArticleModifyContentHandler的代码如下：
```java
public class ArticleModifyContentHandler implements ChannelHandler {

    private Logger logger = LoggerFactory.getLogger(ArticleModifyContentHandler.class);

    @Override
    public void channelProcess(ChannelHandlerContext ctx,
                               Object in,
                               Object out) throws Exception {

        logger.info("修改正文:进入修改正文的Handler");
        ArticleTitleModifyCmd cmd = (ArticleTitleModifyCmd) in;
        logger.info("修改正文,content={}", cmd.getContent());
        logger.info("修改正文:执行完成,即将进入下一个Handler");
        ctx.fireChannelProcess(in, out);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause,
                                Object in,
                                Object out) throws Exception {

        logger.error("修改标题:异常处理逻辑");

        Result re = (Result) out;
        re.setCode(1502);
        re.setMsg("修改正文发生异常");
    }
}

```

## 2.4 通过BootStrap拼装并执行 

```java
public class ArticleModifyExample1 {

    private final static Logger logger = LoggerFactory.getLogger(ArticleModifyExample1.class);

    public static void main(String[] args) {
        //构造入参
        ArticleTitleModifyCmd dto = new ArticleTitleModifyCmd();
        dto.setArticleId("articleId_001");
        dto.setTitle("articleId_001_title");
        dto.setContent("articleId_001_content");
        
        //创建引导类
        BootStrap bootStrap = new BootStrap();

		//拼装
        Result result = (Result) bootStrap
                .inboundParameter(dto)//入参
                .outboundFactory(new ResultFactory())//出参工厂
                .channel(new ArticleModifyChannel())//自定义channel
                .addChannelHandlerAtLast("checkParameter", new CheckParameterHandler())//第一个handler
                .addChannelHandlerAtLast("modifyTitle", new ArticleModifyTitleHandler())//第二个handler
                .addChannelHandlerAtLast("modifyContent", new ArticleModifyContentHandler())//第三个handler
                .process();//执行
        //result为执行结果
        logger.info("result:code={},msg={}", result.getCode(),result.getMsg());
    }
｝
```
## 2.5执行结果

以下是运行ArticleModifyExample1的main方法打出的日志，可以看到我们定义的handler被逐个执行了。

[![vKSLpq.png](https://s1.ax1x.com/2022/08/07/vKSLpq.png)](https://imgtu.com/i/vKSLpq)

# 3. 异常处理

在pie案例工程( https://github.com/feiniaojin/pie-example.git )的example2包中，展示了某个Handler抛出异常时的处理方式。

我们可将其异常处理逻辑实现在当前Handler的exceptionCaught方法中。

假设ArticleModifyTitleHandler的业务逻辑会抛出异常，实例代码如下：

```java
public class ArticleModifyTitleHandler implements ChannelHandler {

    private Logger logger = LoggerFactory.getLogger(ArticleModifyTitleHandler.class);

    @Override
    public void channelProcess(ChannelHandlerContext ctx,
                               Object in,
                               Object out) throws Exception {

        logger.info("修改标题:进入修改标题的Handler");

        ArticleTitleModifyCmd cmd = (ArticleTitleModifyCmd) in;

        String title = cmd.getTitle();
        
        //此处的异常用于模拟执行过程中出现异常的场景
        throw new RuntimeException("修改title发生异常");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause,
                                Object in,
                                Object out) throws Exception {
        logger.error("修改标题:异常处理逻辑");
        Result re = (Result) out;
        re.setCode(1501);
        re.setMsg("修改标题发生异常");
    }
}
```
此时ArticleModifyTitleHandler的`channelProcess`方法一定会抛出异常，
在当前Handler的`exceptionCaught`方法中对异常进行了处理。

运行**ArticleModifyExample2**的main方法，输出如下：

[![vKGnQx.png](https://s1.ax1x.com/2022/08/07/vKGnQx.png)](https://imgtu.com/i/vKGnQx)

# 4. 全局异常处理

有时候，我们不想每个handler都处理一遍异常，我们希望在执行链的最后统一进行处理。

在**ArticleModifyExample3**中，我们展示了通过一个全局异常进行最后的异常处理，其实现主要分为以下几步：

## 4.1 业务Handler传递异常

如果实现了Handler接口，那么需要手工调用 **ctx.fireExceptionCaught**方法向下传递异常。

例如**CheckParameterHandler**捕获到异常时的示例如下：

```java
@Override
public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause,
        Object in,
        Object out) throws Exception {
    
    logger.info("参数校验的异常处理逻辑:不处理直接向后传递");
    ctx.fireExceptionCaught(cause, in, out);
}
```

如果业务Handler继承了**ChannelHandlerAdapter**，如果没有重写`fireExceptionCaught`方法，则默认将异常向后传递。

## 4.2 实现全局异常处理的Handler

我们把业务异常处理逻辑放到最后的Handler中进行处理，该Handler继承了**ChannelHandlerAdapter**，只需要重写异常处理的`exceptionCaught`方法。

示例代码如下：

```java
public class ExceptionHandler extends ChannelHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause,
                                Object in,
                                Object out) throws Exception {

        logger.error("异常处理器中的异常处理逻辑");

        Result re = (Result) out;
        re.setCode(500);
        re.setMsg("系统异常");
    }
}
```

## 4.3 将ExceptionHandler加入到执行链中

直接通过BootStrap加入到执行链最后即可，示例代码如下;

```java
//创建引导类
BootStrap bootStrap = new BootStrap();

Result result = (Result) bootStrap
        .inboundParameter(dto)//入参
        .outboundFactory(new ResultFactory())//出参工厂
        .channel(new ArticleModifyChannel())//自定义channel
        .addChannelHandlerAtLast("checkParameter", new CheckParameterHandler())//第一个handler
        .addChannelHandlerAtLast("modifyTitle", new ArticleModifyTitleHandler())//第二个handler
        .addChannelHandlerAtLast("modifyContent", new ArticleModifyContentHandler())//第三个handler
        .addChannelHandlerAtLast("exception", new ExceptionHandler())//异常处理handler
        .process();//执行
        
//result为执行结果
logger.info("result:code={},msg={}", result.getCode(), result.getMsg());
```

## 4.4 运行ArticleModifyExample3

运行ArticleModifyExample3的main方法，控制台输出如下，可以看到异常被传递到最后的ExceptionHandler中进行处理。

[![vKGCLT.png](https://s1.ax1x.com/2022/08/07/vKGCLT.png)](https://imgtu.com/i/vKGCLT)

---

使用过程中如遇到问题，可以通过公众号或者作者微信联系作者。

公众号: MarkWord，欢迎关注
<div><img src="https://s1.ax1x.com/2022/08/01/vA1OFU.jpg" width="50%" height="50%" alt="pi1rmB6.jpg" border="0"/></div>
微信号：
<div><img src="https://gingoimg.oss-cn-beijing.aliyuncs.com/ddd/qr.jpg" width="50%" height="50%" alt="pi1rmB6.jpg" border="0"/></div>
