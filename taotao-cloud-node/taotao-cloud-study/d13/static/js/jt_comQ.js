var comQ = (function(){
            var html ='{{each newQ}}'+
                '<div id="comQl" class="comQl">'+

                '<div class="comQl-user">'+
                '<a href="#"><div style="background-image:url({{$value.pic}})" class="comQl-user-pic"></div></a>'+
                '<span class="comQl-user-nm"><a href="">{{$value.q_name}}</a></span>'+
                '</div>'+
                '<div class="comQl-queAns">'+
                '<h2 class="comQl-queAns-Q">'+
                '<span class="comQl-queAns-Q-tag wjt_ty_margin">{{$value.q_rank}}</span>'+
                '<a class="comQl-queAns-Q-con" href="">{{$value.title}}</a>'+
                '</h2>'+
                '<div class="comQl-queAns-A">'+
                '<span class="comQl-queAns-A-tag">[{{$value.tag_answer}}]</span>'+
                '<a style="background-image:url({{$value.pic_answer}})" href="javascript:;" class="comQl-queAns-A-pic wjt_ty_margin_small"></a>'+
                '<a class="comQl-queAns-A-nm">{{$value.nm_answer}}：</a>'+
                '<span class="comQl-queAns-A-con">{{$value.answer}}</span>'+
                '</div>'+
                '<div class="comQl-queAns-O">'+
                '<span class="comQl-queAns-O-time">提问时间：{{$value.sub_time}}</span>'+
                '{{each $value.tag as tag}}'+
                '<a class="comQl-queAns-O-tag" href="">{{tag}}</a>'+
                '{{/each}}'+
                '<a class="comQl-queAns-O-src" href="">源自：{{$value.src_question}}</a>'+
                '<a class="comQl-queAns-O-course" href="">此课问答</a>'+
                '</div>'+
                '</div>'+
                '<div class="comQl-record">'+
                '<div class="comQl-record-ans wjt_left">'+
                '<span class="comQl-record-ans-num">{{$value.num_answer}}</span>'+
                '<span class="comQl-record-ans-con">回答</span>'+
                '</div>'+
                '<div class="comQl-record-scan  wjt_left">'+
                '<span class="comQl-record-scan-num">{{$value.num_scan}}</span>'+
                '<span class="comQl-record-scan-con">浏览</span>'+
                '</div>'+
                '</div>' +

                '</div>'+
            '{{/each}}',

                 config = {}, init,createDom,bindEvents;


            init = function($container) {
                config['$container'] = $container;
                $.post('json/weda.txt',{},function(responseText,status){
                    var wenda = JSON.parse(responseText);
                    config['data'] = wenda;
                    createDom();
                });

                bindEvents();

            }

            createDom = function() {
                var render = template.compile(html);
                var HTML   = render(config['data']);

                config['$container'].append(HTML);
                //有无rank值
                //有无回答数
                var $num_answer = $('.comQl-record-ans-num');
                var $record_ans = $('.comQl-record-ans');
                var $answer = $('.comQl-queAns-A');
                var $con_answer = $('.comQl-queAns-A-con');

                for(var i= 0,len =$num_answer.length; i<len;i++ ){
                    if ($num_answer.eq(i).html() !== '0'){
                        $record_ans.eq(i).css('color','#00b33b');
                    }
                    //问题还没有得到回答
                    if($con_answer.eq(i).html()==''){
                        //console.log(i);
                        $answer.eq(i).html('嗯～～，这个提问大家都在考虑......');
                    }
                }


            }

            bindEvents = function(){

            }

            return {init:init}
        })();