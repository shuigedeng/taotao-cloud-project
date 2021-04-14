/**
 * Created by Administrator on 2015/9/6.
 */


    //主要模块
;(function(){
    //课程选择
    (function(){

        var types = {
            "fe":["HTML/CSS", "JavaScript", "Css3","Html5","Jquery","AngularJS","Bootstrap","Node.js","WebApp","前端工具"],
            "be":["PHP", "JavaEE", "Linux", "C#", "Python", "C++",  "Go"],
            "mobile": ["Android", "IOS","Unity 3D","Cocos2d-x"],
            "photo": ["PhotoShop", "Maya", "Promiere"],
            "data":["MySQL", "MongoDB", "云计算", "大数据", "Oracle", "SQLServer"]
        };





        //初始化；
        getAndRenderPics("all");
        getAndRenderChoice("all");

        //全局
        var $directions = $(".direction .choice span");
        var $departs = $(".depart .choice span");
        var $levels = $(".level .choice span");



        //上
        $directions.on("click",function(){
            $(this).addClass("current").siblings().removeClass("current");
            $levels.each(function(i,e){
                $(this).removeClass("current")
            });
            $(".level .choice span:first").addClass("current");
            var project = $(this).attr("name");
//            console.log(project)
            getAndRenderChoice(project)
            getAndRenderPics(project)

        });

        //中
        $("body").on("click",".depart .choice span",function(){
            getAndRenderPics($(this).attr("id"));//第一步加载图片
            var thisContent = $(this).html()
            var that = this;
            var change = false;

            //如果当前有current的方向跟当前点的按钮是父子关系，则只需要变current
            //否则不行，就得执行getAndRenderChoice。
            //只要“方向”没有current就好办了，只要单纯的去改变背景色，而不要去加载ajax
            if(!$(".direction .choice .all").hasClass("current")){
                change = true;
            }

            if(change){
                //
                $(this).addClass("current").siblings().removeClass("current");
            }else{
                //说明“方向”跟“分类”都是“全部”这个选项被打红，则需要加载ajax去变换“分类”中呈现的内容
                for(var i in types){//判断目前店的在哪个“方向”中，在哪个“方向”让哪个亮
                    if(isInArray($(this).html(),types[i])){
                        $("."+i).addClass("current").siblings().removeClass("current");//让“方向”亮

                        getAndRenderChoice(i,thisContent);
                    }
                }
            }
        });

        //下
        $levels.on("click",function(){
            getAndRenderPics($(this).attr("name"));//第一步加载图片
            $(this).addClass("current").siblings().removeClass("current");

        })



        //判断在不在组里
        function isInArray(str,Array){
            //console.log(str,Array)
            var judge
            for(var i =0;i<Array.length;i++){
                if(Array[i] == str){
                    judge = true;
                    break;
                }
                else{
                    judge = false;
                }
            };

            return judge
        }
    })();




//        getAndRender("all");
    //中间主要内容
    function getAndRenderPics(project) {
//        console.log(111)
        var templateString = $("#mainlist-template").html();
        var $courseUl = $(".js-course-lists ul");

        $courseUl.html("");
        myAjax.get("json/" + project + ".json", function (err, data) {
            var dataJSON = JSON.parse(data);
//                console.log(dataJSON)
            var dictionaryArray = dataJSON.list;
//                console.log(dictionaryArray)
            for (var i = 0; i < dictionaryArray.length; i++) {
                var thisDictionary = dictionaryArray[i];
                var compiled = _.template(templateString);
                compiedString = compiled(thisDictionary);
                var $box = $(compiedString)

                $courseUl.append($box);
            }
        })
    }
    //"分类"中主要内容
    function getAndRenderChoice(project,clickContent){
        var $choices = $(".depart .choice");
        $choices.html("");
        myAjax.get("json/" + project + ".json", function (err, data) {
            var dataJSON = JSON.parse(data);
//                console.log(dataJSON)
            var dictionaryArray = dataJSON.names;
//                console.log(dictionaryArray)

            //“分类”这一层内容的设置
            for (var i = 0; i < dictionaryArray.length; i++) {
                var thisDictionary = dictionaryArray[i];
                var $span = $("<span>"+thisDictionary.name+"</span>")
                $span.attr("id",thisDictionary.id)
                $choices.append($span);
//                console.log($(".depart .choice span").length)
            }

            //接受到外部传来的clickContent，去改变“方向”这一层的状态
            if(clickContent){
//                console.log(clickContent)
                $(".depart .choice span").each(function(index,element){

                    if($(this).html() == clickContent){
//                        console.log($(this).html)
                        $(this).addClass("current").siblings().removeClass("current");
                    }
                })
            }else{
                $(".depart .choice span:first").addClass("current");
            }

        })

    }





})();

