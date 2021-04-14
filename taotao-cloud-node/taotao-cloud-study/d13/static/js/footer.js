/**
 * Created by Administrator on 2015/9/6.
 */
    var footer = function(){
            init = function(){}
}
 var footer = (
    function(){
        var html =
        '<div class="bottom">'+
            '<div class="container clearfix">'+
            '<div class="left bottom_link">'+
            '<ul class="ln_our clear clearfix">'+
                '<li><a href="#" target="_blank">网站首页</a></li>'+
                '<li><a href="#" target="_blank">人才招聘</a></li>'+
                '<li> <a href="#" target="_blank">联系我们</a></li>'+
                '<li><a href="#" target="_blank">高校联盟</a></li>'+
                '<li><a href="#" target="_blank">关于我们</a></li>'+
                '<li> <a href="#" target="_blank">讲师招募</a></li>'+
                '<li> <a href="#" target="_blank">意见反馈</a></li>'+
                '<li> <a href="#" target="_blank">友情链接</a></li>'+
            '</ul>'+
        '<p class="clear ln_copy">Copyright ? 2015 imooc.com All Rights Reserved | 京ICP备 123456789号-2</p>'+
        '</div>'+
        '<div class="right bottom_netlink">'+
        '<div class="ln_copy_ewm"></div>'+
        '<ul class="ln_bottom_ewm">'+
            '<li><a href="#" class="ln_bottom_weixin"></a></li>'+
            '<li><a href="#" class="ln_bottom_weibo_content"></a></li>'+
            '<li><a href="#" class="ln_bottom_qq"></a></li>'+
        '</ul>'+
        '</div>'+
        '</div>'+
        '</div>', config={},init,createDom,bindEvents;
        init = function($container,json){
            config['$container'] = $container;
            config['json'] = json;
            createDom();
            bindEvents();
        };
        createDom = function(){
            config.$container.html(html);
        };
        bindEvents = function(){

              $('.ln_bottom_weixin').on('mouseover',function(){
                $('.ln_copy_ewm').fadeIn();
            })
            $('.ln_bottom_weixin').on('mouseout',function(){
                $('.ln_copy_ewm').fadeOut();
            })
        };
        return{init:init};

    }
)()