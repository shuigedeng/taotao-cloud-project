/**
 * Created by Administrator on 2015/9/8.
 */
var  zhanshi= (function(w,$,undefined){
        var  config = {
            zhanshihtml:[
                '<div class="card-arr clearfix"></div>',

            '<div class="zhanshi" id="zhanshi">',
            '<div class="zhanshi_up clear clearfix">',
            '<div class="touxiang left">',
                '{{each zhanshi}}',
            '<a href="#"><img class="touxiang_img" src={{$value.imageSrc}} alt="头像"/></a>',
            '</div>',
            '<ul  class="left zhanglingdj">',
            '<li class="touxiang_list nicheng">{{$value.nicheng}}</li>',
            '<li class="touxiang_list jingyan">经验值<em class="jinngyan_num">{{$value.jingyannum}}</em></li>',
            '</ul>',
            '</div>',
            '<div class="zhanshi_down">',
            '<div class="zhanshi_info">',
            '<ul>',
            '<li>我的慕课</li>',
            '<li>我的社区</li>',
            '</ul>',
            '</div>',
            '<div class="jindu">',
            '<ul>',
            '<li><em class="jindu_info">{{$value.jindu_info}}</em><a href="#">继续</a></li>',
            '<li class="jindu_bar">{{$value.jindu_bar}}</li>',
            '</ul>',
            '</div>',
            '{{/each}}',
            '<div class="gerenshezhi">',
            '<p><span>个人设置</span><a href="#" class="quit">退出</a></p>',
            '</div>',
            '</div>',
            '</div>'
            ].join("")
        } ,stateMap  = { $container : null }, jqueryMap = {

        },init,createDOM,bindEvent,setJqueryMap;
        init=function($container,data){
            stateMap.$container = $container;
            stateMap.data = data;
            createDOM();
            bindEvent();
            setJqueryMap();
        };
        createDOM = function(){
            var $container = stateMap.$container;
            var data = stateMap.data;
            var render = template.compile(config.zhanshihtml);
            var html=  render(data);
            $container.html(html);
        };
        bindEvent = function(){
            $(".quit").on('click',function(){
                header.init($(".header"))
            })
        };
        setJqueryMap = function () {
            var $container = stateMap.$container;
            jqueryMap = { $container : $container };
        };
        return {init:init};
    }
)(window,jQuery)