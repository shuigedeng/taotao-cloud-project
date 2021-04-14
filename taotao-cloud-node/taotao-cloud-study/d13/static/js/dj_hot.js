/**
 * Created by Administrator on 2015/9/5.
 */
var  hotquestion= (function(w,$,undefined){
        var  config = {
                    hothtml:[
                    '<div id="label">',
                    '<div class="hot-list-box">',
                    '<h2 href="#"class="hot-list-box-title" >一周热门问题</h2>',
                    '</div>',
                    '<div class="hot-list-body">',
                    '<ul class="weekly-hot">',
                    '{{each hotqusetion as value i }}',
                    '<li><em class="hoticon hot{{i+1}}">热</em><a href="#" target="_blank" class="hottalk">{{value.hottalk}}</a><i class="rankingnum">{{value.rankingnum}}<br>回答</i></li>',
                    '{{/each}}',
                    '</ul>',
                    '</div>',
                    '</div>'
                    ].join("")
            } ,stateMap  = { $container : null }, init,createDOM,bindEvent,$backTop = $("#backTop");
        init=function($container){
           /* $label =config.hothtml.find("#label")
            $container.append($label);*/
            stateMap.$container = $container;
            $.post('json/hot.txt',{},function(data){
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
            var render = template.compile(config.hothtml);
            var html=  render(data);
            $container.html(html);
        };
        bindEvent = function(){
            $(window).load(function(){
            })
        };
        return {init:init};
    }
)(window,jQuery)