/**
 * Created by Administrator on 2015/9/6.
 */
var goto = (function(){
  var  config={
       gotohtml:[
       '<a class="GotoTop-info-weixin" href="javascript:;"></a>',
       '<a class="GotoTop-info-msg" href="#" target="_blank" id="feedBack"></a>',
       '<a class="GotoTop-info-app" href="3"></a>',
       '<a class="GotoTop-info-top" href="javascript:;" style="display: none;" id="backTop"></a>'
       ].join("")
        },stateMap = { $container : null }, init,createDOM,bindEvent;
    init=function($container){

        stateMap.$container = $container;
        stateMap.data = config.gotohtml;
        createDOM();
        bindEvent();
        };
    createDOM = function(){
        var $container = stateMap.$container;
        var data = stateMap.data;
        $container.html(data);
        };
    bindEvent = function(){
        //·µ»Ø¶¥²¿
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

})()