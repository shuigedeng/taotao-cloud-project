/**
 * Created by Administrator on 2015/9/5.
 */
var  leifeng= (function(w,$,undefined){
        var  config = {
                leifenghtml:[
                '<div class="lei-list-box mr-top">',
                '<h2 href="#"class="lei-list-box-title">一周回答雷锋榜</h2>',
                '</div>',
                '<ul class="leifeng-list-body">',
                '{{each leifengqusetion as value i }}',
                '<li>',
                '<a href="#" class="roll-head left" target="_blank" title="偌颜宁"><img src="image/{{i+1}}.jpg" width="40" height="40"></a>',
                '<div class="left">',
                '<a href="#" target="_blank" title="偌颜宁" class="rankingnickname">{{value.rankingnickname}}</a>',
                '<em class="archieve">{{value.archieve}}</em>',
                '</div>',
                '<i class="rankingnum">{{value.rankingnum}}<br>回答</i>',
                '</li>',
                '{{/each}}',
                '</ul>',
                '</div>'
                ].join("")
            } ,stateMap  = { $container : null }, init,createDOM,bindEvent;
        init=function($container){

            stateMap.$container = $container;
            $.post('json/leifeng.txt',{},function(data){
                stateMap.data = JSON.parse(data);
                createDOM();
            });
            //stateMap.data = data;
            //createDOM();
            bindEvent();
        };
        createDOM = function(){
            var $container = stateMap.$container;
            var data = stateMap.data;
            var render = template.compile(config.leifenghtml);
            var html=  render(data);
            $container.html(html);
        };
        bindEvent = function(){
            //返回顶部
            $(window).scroll(function(){
                $(window).scrollTop()>400?$("#backTop").css("display","block"):$("#backTop").css("display","none");
            });
            $("#backTop").click(function(){
                $('body,html').animate({scrollTop:0},500);
                return false;
            });
            $("#GotoTop a").eq(0).mouseover(function(){
                $(this).addClass("weixin");
            })
            $("#GotoTop a").eq(0).mouseout(function(){
                $(this).removeClass("weixin");
            })
            $("#GotoTop a").eq(2).mouseover(function(){
                $(this).addClass("app");
            })
            $("#GotoTop a").eq(2).mouseout(function(){
                $(this).removeClass("app");
            })
       };

        return {init:init};
    }
)(window,jQuery)