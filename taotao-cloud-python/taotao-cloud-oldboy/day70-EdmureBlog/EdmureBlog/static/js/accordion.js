/**
 * Created by wupeiqi on 16/11/25.
 */
(function($){

    /*
     上下固定菜单：初始化左侧菜单位置
     */
    function initFixedAccordion(){
        var currentIndex = $('.fixed-accordion .accordion-item.active').index();
        currentIndex = currentIndex == -1 ? 0:currentIndex;
        changeFixedAccordion(currentIndex);
    }

    /*
     上下固定菜单：改变的左侧菜单
     */
    function changeFixedAccordion(currentIndex){

        var $accordionItem = $('.fixed-accordion .accordion-item');
        var itemLength = $accordionItem.length;
        var accordionHeight = $('.fixed-accordion').outerHeight();
        var headerHeight = $accordionItem.find('.accordion-header').outerHeight();
        var contentHeight = accordionHeight - 3 - headerHeight * itemLength;

        $accordionItem.each(function(k,v){
            $(this).removeAttr('style');
            if(currentIndex > k){
                $(this).css('top', k* headerHeight);
                $(this).removeClass('active');
            }else if(currentIndex == k){
                $(this).addClass('active');
                $(this).css('top', k* headerHeight);
                $(this).find('.accordion-content').css('height', contentHeight);
            }else{
                var index = itemLength - k - 1;
                $(this).removeClass('active');
                $(this).css('bottom', index* headerHeight);
            }
        });
    }

    /*
     上下固定菜单：为菜单绑定点击事件
     */
    function bindFixedAccordionEvent(){
        $('.fixed-accordion .accordion-header').on('click', function(){
            var itemIndex = $(this).parent().index();

            changeFixedAccordion(itemIndex);
        })
    }


    /*
     普通菜单：为菜单绑定点击事件
     */
    function bindEasyAccordionEvent(){
        $('.easy-accordion .accordion-header').on('click', function(){
            $(this).parent().addClass('active');
            $(this).parent().siblings().removeClass('active');
        });
    }
    $.extend({
        easyAccordion: function () {
            bindEasyAccordionEvent();
        },
        fixedAccordion: function () {
            initFixedAccordion();
            bindFixedAccordionEvent();
        }
    })
})(jQuery);