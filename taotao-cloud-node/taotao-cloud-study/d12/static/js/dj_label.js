/**
 * Created by Administrator on 2015/9/5.
 */
var  label= (function(w,$,undefined){
        var  config = {
                labelhtml:[
                    '<div id="newques-container">',
                    '<a href="#" class="newques-btn ">新问题</a>',
                    '</div>',
                    '<div class="newques-label mr-big">',
                    '<div class="newques-container-label-body ">',
                    '{{each label as value }}',
                    '<a href="#" class="hot-label" >{{value}}</a>',
                    '{{/each}}',
                    '</div>',
                    '</div>'
                    ].join("")
            } ,stateMap  = { $container : null }, init,createDOM,bindEvent,$backTop = $("#backTop");
        init=function($container){
            stateMap.$container = $container;
            $.post('json/label.txt',{},function(data){
                stateMap.data = JSON.parse(data);
                createDOM();
            });

            bindEvent();
        };
        createDOM = function(){
            var $container = stateMap.$container;
            var data = stateMap.data;
            var render = template.compile(config.labelhtml);
            var html=  render(data);
            $container.html(html);
        };
        bindEvent = function(){

        };
        return {init:init};
    }
)(window,jQuery)