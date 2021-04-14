/**
 * Created by Administrator on 2015/9/7.
 */
    //头部组件
$.extend({
    formateString : function(str, data){
        //\w+,表示一个或多个\w,最少一个
        //\w 表示 字符 数字
        //函数表示 用json替换正则表达式找到的值
        return str.replace(/@\((\w+)\)/g, function(match, key){
            return typeof data[key] === "undefined" ? '' : data[key]});
    }
})


var header = (function(w,$,undefined){
    var config = {
            main_html : [

                '<div class="left ln_logo">@(leftlogo)</div>',
                '<div class="left g-menu-mini">',
                '<ul class="ln-nav-item">',
                '<li><a href="index.html" >课程</a></li>',
                '<li><a href="#">计划</a></li>',
                '<li><a href="#">分享</a></li>',
                '<li><a href="sq.html">社区</a></li>',
                '</ul>',
                '</div>',
                '<div class="right ln-app">',
                '<div id="ln-login-area right">',
                '<ul class="ln-header-unlogin clearfix right">',
                '<li class=@(messageClassname)>',
                '<a href="#" id="js-signup-btn">@(myMessage)</a>',
                '</li>',
                '<li class=@(peopleClassname)>',
                '<a href="#" id="js-signin-btn">@(people)</a>',
                ,
                '</li>',
                '</ul>',
                '</div>',
                '<div class="ln-app-down-area right">',
                '<a href="#">',
                '<i class="header-app-icon"></i>APP',
                '</a>',
                '</div>',
                '<div class="ln-search-area right" data-search="top-banner">',
                '<input class="search-input right"  placeholder="请输入想搜索的内容..." type="text">',
                '<div  class="btn_search right"></div>',
                '</div>',
                '</div>'

            ].join("")
        },
        init,bindEvent,createDOM;

    //三部分
    init = function($container,data){
        config.$container = $container;
        var headerInfo = {
            //"leftlogo":"<img src ='image/logo.png'/>",
            "myMessage" :"登录",
            "people":"注册",

            "messageClassname":"ln-header-signin",
            "peopleClassname":"ln-header-signup",
        }
        config.data = headerInfo;

        createDOM();
        bindEvent();
    };

    createDOM = function(){
        var $container = config.$container;
        var data = config.data;
        var template = $.formateString(config.main_html,data);
        $container.html(template);
    };

    bindEvent = function(){
        var pageNumber ;
        //头部分类选择
        //var headerChoose = function(){
        //    $(".ln-nav-item li a").on("click",function(e,i){
        //        pageNumber = e;
        //        console.log("pageNumber:"+pageNumber)
        //
        //        $(this).addClass("active").parent().siblings().find("a").removeClass("active");
        //    })
        //}
        //点击搜索框输入
        var sreachBox = function(){

            //ln-search-area right

            $(".search-input.right").on("focus",function(){
                $(this).css("backgroundColor","white")
                $(".btn_search").css({"backgroundColor":"white","background-position":"0px -40px"})
            }).blur(function(){
                $(this).css("backgroundColor","#363c41")
                $(".btn_search").css({"backgroundColor":"#363c41","background-position":"0px 0px"})
            });
        }

        sreachBox();
        //headerChoose();
        //点击登录
        $(".ln-header-signin").click(function(){
            //mask
            $("body").css("overflow","hidden")
            Ngrade_login.init($("body"),'denglu',function(userMsg,judgeInfo){
                var dictionary = userMsg.list;
                var userInformation;
                var nameWrong = true;
                var passportWorng = true;
                //判断返回值，跟输入用户名跟密码皮不匹配
                for(var i=0;i<dictionary.length;i++){
                    var thisDictionary = dictionary[i];

                    if(thisDictionary["username"] == judgeInfo["username"]){
                        nameWrong = false;
                        if(thisDictionary["password"] == judgeInfo["password"]){
                            passportWorng = false;
                            userInformation = thisDictionary
                            //如果匹配，将原来的登录还有注册按钮都变换成信息图还有头像
                            //利用动态类名变化实现！！！！！！！！！！！！！！！！！！！！！！！！
                            var $container = config.$container;
                            config.data = userInformation;
                            var data = config.data;
                            var template = $.formateString(config.main_html,data);
                            $container.html(template);


                            //因为是append，所以要清空操作
                            $("body").css("overflow","visible")
                            $("body").find("#alert").remove();
                            $("body").find("#mask").remove();

                            hide();

                            //因为这次相当于又是一个组件又初始化了一下，所以要重新绑定事件
                            sreachBox();
                            //headerChoose();
                            //添加事件，不知道对不对
                            //因为全是动态事件，事件委托总是不管用，只能在里面写
                            //经过头像弹出框
                            $('.ln-header-people').hover(function(e){
                                $("#down-header-box").show();
                                var zhanshijson={
                                    zhanshi:[{
                                        imageSrc:userInformation["peoplebig"],
                                        nicheng:userInformation["username"],
                                        jingyannum:userInformation["jingyan"],
                                        jindu_info:userInformation["jindu_info"],
                                        jindu_bar:userInformation["jindu_bar"]
                                    }]
                                }

                                zhanshi.init($("#down-header-box"),zhanshijson)
                            },function(){
                                $("#down-header-box").hide();
                            })
                        }
                    }
                }
                //用户名或者密码分别弹出
                if(nameWrong || passportWorng){
                    $(".denglu-test").html("您的用户名输入有误")
                    $(".password-test").html("您的密码输入有误")
                }else{
                    $(".denglu-test").html("")
                    $(".password-test").html("")
                }




                function hide(){
                    $('.mask').hide();
                    $('.alert').hide();
                }
            });
        });

        //点击注册
        $(".ln-header-signup").click(function(){

            //mask
            $("body").css("overflow","hidden")
            Ngrade_login.init($("body"),'zhuce',function(userMsg,judgeInfo){
                //因为是append，所以要清空操作
                $("body").css("overflow","visible")
                $("body").find("#alert").remove();
                $("body").find("#mask").remove();
                hide();
                function hide(){
                    $('.mask').hide();
                    $('.alert').hide();
                }
            })
        });






    };

    return{init:init};

})(window,jQuery)