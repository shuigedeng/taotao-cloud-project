/**
 * Created by Administrator on 2015/9/6.
 */


//这是登录注册的组件。
var Ngrade_login = (function(w,$,undefined){
    var html = {
            mask:'<div id="mask" class="mask"></div>',
            alert:'<div id="alert" class="alert">'+
        '<h1 class="alert-header">'+
        '<span class="alert-header-denglu">登录</span>'+
        '<span class="alert-header-zhuce">注册</span>'+
        '<span class="alert-header-close"></span>'+
        '</h1>'+
        '<div class="alert-in">'+

        '</div>'+
        '<div class="alert-login">'+


        '</div>'+
        '<div class="alert-O">'+
        '<span class="alert-O-com">联合登录</span>'+
        '<div class="alert-O-method">'+
        '<a>微信</a>'+
        '<a>QQ</a>'+
        '<a>微博</a>'+
        '</div>'+
        '</div>'+
        '</div>',
        autoLogDengLu:'<input type="checkbox" class="alert-login-auto"><label class="alert-login-autoFont">&nbsp;自动登录</label></input>'+
                '<a class="alert-login-forget">忘记密码</a>'+
                '<div  id = "dengluButton" class="alert-login-submit"><a >登&nbsp;&nbsp;录</a></div>',
        justZhuce:'<div id = "zhuceButton"class="alert-login-submit"><a >注&nbsp;&nbsp;册</a></div>',



            denglu:'<input class="alert-in-denglu" type="text" placeholder="请输入您的用户名"/>'+
                '<div class="denglu-test"></div>'+
                '<input class="alert-in-password" type="password" placeholder="请输入您的密码"/>'+
                '<div class="password-test"></div>',
        zhuce:'<input class="alert-in-email" type="text" placeholder="请输入您的用户名"/>'+
                '<div class="email-test"></div>'+
                '<input class="alert-in-psone" type="text" placeholder="请输入您的密码"/>'+
                '<div class="psone-test"></div>'+
                '<input class="alert-in-pstwo" type="text" placeholder="请再次输入您的密码"/>'+
                '<div class="pstwo-test"></div>'+
                '<input class="alert-in-usnm" type="text" placeholder="请输入您的昵称"/>'+
                '<div class="usnm-test"></div>'
        },
    config = {},init,createDom,bindEvents,show;
    init = function($container,startPart,callback){
        config['$container'] = $container;
        config['callback'] = callback;
        config['startPart'] = startPart;
        createDom();
        bindEvents();
    }
    createDom = function(){
        var $body = $('body');
        var $alert_and_mask_alert = $('.alert-and-mask-alert');
        var $alert_and_mask_mask = $('.alert-and-mask-mask');
        $body.append(html.mask);
        $body.append(html.alert);
        show();
        //弹出登录/注册框时的状态初始化
        var $in = $('.alert-in'),
            $zhuce  = $('.alert-header-zhuce'),
            $denglu  = $('.alert-header-denglu'),
            $login = $(".alert-login");


        $in.html(html[config.startPart]);
        if(config.startPart == "denglu"){
            $denglu.addClass("alert-header-current").siblings().removeClass("alert-header-current");
            $in.html(html.denglu);
            $login.html(html.autoLogDengLu);



        }else if(config.startPart == "zhuce"){
            $zhuce.addClass("alert-header-current").siblings().removeClass("alert-header-current");
            $in.html(html.zhuce);
            $login.html(html.justZhuce);

        };




    }
    bindEvents = function(){
        var
            $zhuce  = $('.alert-header-zhuce'),
            $in = $('.alert-in'),
            $denglu  = $('.alert-header-denglu'),
            $login = $(".alert-login");



        //添加事件做成个包//我们的类名都是动态添加的，事件委托都不管用，只能都放在一个函数里动态调用
        var checkForm = function(){

            var $unm_test = $('.email-test'),
                $psone = $('.alert-in-psone'),
                $pso_test = $('.psone-test'),
                $pstwo = $('.alert-in-pstwo'),
                $psusm = $(".alert-in-usnm"),
                $pst_test = $('.pstwo-test'),
                $userName = $('.alert-in-denglu'),
                $password = $('.alert-in-password'),
                $unm = $('.alert-in-email');
            //验证表单
            //登录
            $userName.on("focus",function(){
                $(this).val("");
                $(".denglu-test").html("")

            })
            $userName.on("blur",function(){
                if(/^[a-zA-Z]\w{3,10}$/.test($(".alert-in-denglu").val())){
                    $(".denglu-test").html("")
                }else{
                    $(".denglu-test").html("您输入的用户名格式不正确")
                }
            })


            $password.on("focus",function(){
                $(this).val("");
                $(".password-test").html("");

            });
            $password.on("blur",function(){
                if(/^\w{8,}$/.test($(".alert-in-password").val())){
                    $(".password-test").html("")
                }else{
                    $(".password-test").html("您输入的密码不得少于8位")
                }
            })


            //注册
            $unm.on("focus",function(){

                $(".email-test").html("")

            })
            $unm.on('blur',function(){
                //用户名验证，3-10位，只能是数字，字母混合

                if(/^[a-zA-Z]\w{3,10}$/.test($(this).val())){
                    $($unm_test).html("")
                }else{
                    $($unm_test).html("您输入的用户名格式不正确")
                }

            });

            $psone.on("focus",function(){

                $(".psone-test").html("")

            })
            $psone.on('blur',function(){
                var psoval = $(this).val();
                //密码验证，8-16位，只能是数字，字母混合
                var psoReg = /[\w\d]{8,16}/;
                if(!psoReg.exec(psoval)){
                    var psoErr = '8-16位，只能是数字字母混合';
                    $pso_test.html(psoErr);
                }

            });

            $pstwo.on("focus",function(){

                $(".pstwo-test").html("")

            })
            $pstwo.on('blur',function(){
                var pstval = $(this).val();
                //密码一致
                if(pstval !==$psone.val()){
                    var psErr = '前后输入密码不一致';
                    $pst_test.html(psErr);
                }

            });

            $psusm.on("focus",function(){
                $(".usnm-test").html("")
            })
            $psusm.on("blur",function(){
                var dlval = $(this).val();
                //用户名验证，3-10位，只能是数字，字母混合
                var unmReg = /^[a-zA-Z]{3,10}$/;
                if(!unmReg.exec(dlval)){
                    var unmErr = '用户名3-10位，只能字母';
                    $(".usnm-test").html(unmErr);
                }

            })



        }

            checkForm();
        //所有事件
        //点击登录，界面变化
        $denglu.on('click',function(){
            $denglu.addClass("alert-header-current").siblings().removeClass("alert-header-current")
            $in.html(html.denglu);
            $login.html(html.autoLogDengLu);
            checkForm();

        });
        //点击注册，界面变化
        $zhuce.on('click',function(){
            $zhuce.addClass("alert-header-current").siblings().removeClass("alert-header-current")
            $in.html(html.zhuce);
            $login.html(html.justZhuce);
            checkForm();
        });





        //提交登录
        $("#dengluButton").on('click',function(){
            console.log("denglu")

            var username = $('.alert-in-denglu').val(),
                password = $('.alert-in-password').val();
            var data = {
                "username":username,
                "password":password
            };
            $.post('json/userMsg.txt',data,function(responseText,status){
                var userMsg = JSON.parse(responseText);

                if(config['callback']){
                    config['callback'](userMsg,data);
                }
            },"text");
        })

        //提交表单
        $("#zhuceButton").on("click",function(){
            var username = $('.alert-in-email').val(),
                password = $('.alert-in-psone').val(),
                name = $('.alert-in-usnm').val();
            var data = {
                "username" : username,
                "password" : password,
                "name" : name,
                };
            $.post('json/userMsg.txt',data,function(responseText,status){
                var userMsg = JSON.parse(responseText);


                if(config['callback']){
                    config['callback'](userMsg,data);
                }

            },"text");



        })

        //用户体验
        $("#mask,.alert-header-close").on("click",function(){
            $("body").css("overflow","visible");
            $("body").find("#alert").remove();
            $("body").find("#mask").remove();

            hide();
        });

    }
    show = function(){
        $('.mask').show();
        $('.alert').show();
    }
    hide = function(){
        $('.mask').hide();
        $('.alert').hide();
    }
    return {init:init}
})(window,jQuery);