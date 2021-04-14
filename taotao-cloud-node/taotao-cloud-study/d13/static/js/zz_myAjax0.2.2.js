(function(){
    var myAjax = {};  //空对象
    //向外暴露这么一个全局变量
    //就是这个函数的命名空间
    window.myAjax = myAjax;

    //=======================属性=======================
    myAjax.version = "0.2.2";
    myAjax.author = "传智播客JS高薪班";
    myAjax.description = "增加了缓存问题的处理";

    //=======================方法=======================
    myAjax.get = function(){
        //参数个数
        var argLength = arguments.length;
        var URL,json,callback;
        if(argLength == 2 && typeof arguments[0] == "string" && typeof arguments[1] == "function"){
            //两个参数
            URL = arguments[0];
            callback = arguments[1];
            //传给我们的核心函数来发出Ajax请求
            myAjax._doAjax("get",URL,null,null,callback);
        }else if(argLength == 3 && typeof arguments[0] == "string" && typeof arguments[1] == "object" && typeof arguments[2] == "function"){
            //3个参数
            URL = arguments[0];
            json = arguments[1];
            callback = arguments[2];
            //传给我们的核心函数来发出Ajax请求
            myAjax._doAjax("get",URL,json,null,callback);
        }else{
            throw new Error("get方法参数错误！");
        }
    }

    myAjax.post = function(){
        //参数个数
        var argLength = arguments.length;
        if(argLength == 3 && typeof arguments[0] == "string" && typeof arguments[1] == "object" && typeof arguments[2] == "function"){
            //3个参数
            var URL = arguments[0];
            var json = arguments[1];
            var callback = arguments[2];
            //传给我们的核心函数来发出Ajax请求
            myAjax._doAjax("post",URL,json,null,callback);
        }else{
            throw new Error("post方法参数错误！");
        }
    }

    //post方式提交所有表单
    myAjax.postAllForm = function(URL,formId,callback){
        var params = myAjax._formSerialize(formId);
        myAjax._doAjax("post",URL,null,params,callback);
    }

    //=======================内部方法=====================
    //将JSON转换为URL查询参数写法
    //传入{"id":12,"name":"考拉"}
    //返回id=12&name=%45%45%ED
    myAjax._JSONtoURLparams = function(json){
        var arrParts = [];  //每个小部分的数组
        for(k in json){
            arrParts.push(k + "=" + encodeURIComponent(json[k]));
        }
        return arrParts.join("&");
    }

    //最核心的发出Ajax请求的方法
    //也就是说，这个函数调用的时候，又可以传一个json进来。也可以直接传输一个序列化的串进来。
    myAjax._doAjax = function(method,URL,json,params,callback){
        //Ajax的几个公式
        if(window.XMLHttpRequest){
            var xhr = new XMLHttpRequest();
        }else{
            var xhr = new ActiveXObject("Microsoft.XMLHTTP");
        }

        xhr.onreadystatechange = function(){
            if(xhr.readyState == 4){
                if(xhr.status >= 200 && xhr.status < 300 || xhr.status == 304){
                    callback(null,xhr.responseText);
                }else{
                    callback("文件没有找到" + xhr.status,null);
                }
            }
        }

        //现在要根据请求类型进行判断
        if(method == "get"){
            //请求类型是get
            //如果用户传输了json,此时要连字
            if(json) {
                var combineChar = URL.indexOf("?") == -1 ? "?" : "&";
                URL += combineChar + myAjax._JSONtoURLparams(json);
            }

            //增加一个随机数参数，防止缓存
            var combineChar = URL.indexOf("?") == -1 ? "?" : "&";
            URL += combineChar + Math.random().toString().substr(2);

            xhr.open("get",URL,true);
            xhr.send(null);
        }else if(method == "post"){
            //增加一个随机数参数，防止缓存
            var combineChar = URL.indexOf("?") == -1 ? "?" : "&";
            URL += combineChar + Math.random().toString().substr(2);

            xhr.open("post",URL,true);
            xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            if(json){
                xhr.send(myAjax._JSONtoURLparams(json));
            }else if(params){
                xhr.send(params);
            }
        }
    }

    myAjax._formSerialize = function(formId){
        //得到表单
        var oForm = document.getElementById(formId);
        //得到按钮
        var oBtn = document.getElementById("btn");
        //得到所有表单控件
        var fields = oForm.elements;
        //表单控件的数量
        var fieldLength = fields.length;
        //存放每个部分的数组
        var partsArray = new Array();
        //遍历所有的控件
        for (var i = 0; i < fieldLength; i++) {
            //得到你遍历到的这个控件
            var field = fields[i];
            var k = field.name;
            var v = "";

            //根据这是一个什么控件来决定v
            switch(field.type){
                case "button":
                case "submit":
                case "reset" :
                    break;
                case "select-one":
                    //遍历这个单选列表的所有option
                    var options = field.options;
                    //这个单选列表的option的个数
                    var optionsLength = options.length;
                    //遍历所有的option，查看那个选项被selected了
                    //被selected了的那个选项的value，就是总value
                    for(var j = 0 ; j < optionsLength ; j++){
                        if(options[j].selected){
                            v = options[j].value;
                            partsArray.push(k + "=" + v);
                        }
                    }
                    break;
                case "radio" :
                case "checkbox" :
                    if(!field.checked){
                        break;
                    }
                case "text" :
                default:
                    v = field.value;
                    partsArray.push(k + "=" + v);
                    break;
            }
        }
        return partsArray.join("&");
    }
})();