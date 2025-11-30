let api = [];
const apiDocListSize = 1
api.push({
    name: 'default',
    order: '1',
    list: []
})
api[0].list.push({
    alias: 'OrderInfoController',
    order: '1',
    link: '订单管理api',
    desc: '订单管理API',
    list: []
})
api[0].list[0].list.push({
    order: '1',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2026.01/order/exists/id',
    desc: '根据用户id查询用户是否存在',
});
api[0].list.push({
    alias: 'OrderItemController',
    order: '2',
    link: '订单项管理api',
    desc: '订单项管理API',
    list: []
})
api[0].list.push({
    alias: 'error',
    order: '3',
    link: 'error_code_list',
    desc: '错误码列表',
    list: []
})
api[0].list.push({
    alias: 'dict',
    order: '4',
    link: 'dict_list',
    desc: '数据字典',
    list: []
})
api[0].list[3].list.push({
    order: '1',
    deprecated: 'false',
    url: '',
    desc: 'http状态码字典',
});
document.onkeydown = keyDownSearch;

function keyDownSearch(e) {
    const theEvent = e;
    const code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code == 13) {
        const search = document.getElementById('search');
        const searchValue = search.value;
        let searchGroup = [];
        for (let i = 0; i < api.length; i++) {

            let apiGroup = api[i];

            let searchArr = [];
            for (let i = 0; i < apiGroup.list.length; i++) {
                let apiData = apiGroup.list[i];
                const desc = apiData.desc;
                if (desc.indexOf(searchValue) > -1) {
                    searchArr.push({
                        order: apiData.order,
                        desc: apiData.desc,
                        link: apiData.link,
                        list: apiData.list
                    });
                } else {
                    let methodList = apiData.list || [];
                    let methodListTemp = [];
                    for (let j = 0; j < methodList.length; j++) {
                        const methodData = methodList[j];
                        const methodDesc = methodData.desc;
                        if (methodDesc.indexOf(searchValue) > -1) {
                            methodListTemp.push(methodData);
                            break;
                        }
                    }
                    if (methodListTemp.length > 0) {
                        const data = {
                            order: apiData.order,
                            desc: apiData.desc,
                            link: apiData.link,
                            list: methodListTemp
                        };
                        searchArr.push(data);
                    }
                }
            }
            if (apiGroup.name.indexOf(searchValue) > -1) {
                searchGroup.push({
                    name: apiGroup.name,
                    order: apiGroup.order,
                    list: searchArr
                });
                continue;
            }
            if (searchArr.length === 0) {
                continue;
            }
            searchGroup.push({
                name: apiGroup.name,
                order: apiGroup.order,
                list: searchArr
            });
        }
        let html;
        if (searchValue == '') {
            const liClass = "";
            const display = "display: none";
            html = buildAccordion(api, liClass, display);
            document.getElementById('accordion').innerHTML = html;
        } else {
            const liClass = "open";
            const display = "display: block";
            html = buildAccordion(searchGroup, liClass, display);
            document.getElementById('accordion').innerHTML = html;
        }
        const Accordion = function (el, multiple) {
            this.el = el || {};
            this.multiple = multiple || false;
            const links = this.el.find('.dd');
            links.on('click', {el: this.el, multiple: this.multiple},
                this.dropdown);
        };
        Accordion.prototype.dropdown = function (e) {
            const $el = e.data.el;
            $this = $(this), $next = $this.next();
            $next.slideToggle();
            $this.parent().toggleClass('open');
            if (!e.data.multiple) {
                $el.find('.submenu').not($next).slideUp(
                    "20").parent().removeClass('open');
            }
        };
        new Accordion($('#accordion'), false);
    }
}

function buildAccordion(apiGroups, liClass, display) {
    let html = "";
    let doc;
    if (apiGroups.length > 0) {
        if (apiDocListSize == 1) {
            let apiData = apiGroups[0].list;
            for (let j = 0; j < apiData.length; j++) {
                html += '<li class="' + liClass + '">';
                html += '<a class="dd" href="#_' + apiData[j].link + '">'
                    + apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
                html += '<ul class="sectlevel2" style="' + display + '">';
                doc = apiData[j].list;
                for (let m = 0; m < doc.length; m++) {
                    let spanString;
                    if (doc[m].deprecated == 'true') {
                        spanString = '<span class="line-through">';
                    } else {
                        spanString = '<span>';
                    }
                    html += '<li><a href="#_' + apiData[j].order + '_'
                        + doc[m].order + '_' + doc[m].href + '">'
                        + apiData[j].order + '.' + doc[m].order + '.&nbsp;'
                        + spanString + doc[m].desc + '<span></a> </li>';
                }
                html += '</ul>';
                html += '</li>';
            }
        } else {
            for (let i = 0; i < apiGroups.length; i++) {
                let apiGroup = apiGroups[i];
                html += '<li class="' + liClass + '">';
                html += '<a class="dd" href="#_' + apiGroup.name + '">'
                    + apiGroup.order + '.&nbsp;' + apiGroup.name + '</a>';
                html += '<ul class="sectlevel1">';

                let apiData = apiGroup.list;
                for (let j = 0; j < apiData.length; j++) {
                    html += '<li class="' + liClass + '">';
                    html += '<a class="dd" href="#_' + apiGroup.order + '_'
                        + apiData[j].order + '_' + apiData[j].link + '">'
                        + apiGroup.order + '.' + apiData[j].order + '.&nbsp;'
                        + apiData[j].desc + '</a>';
                    html += '<ul class="sectlevel2" style="' + display + '">';
                    doc = apiData[j].list;
                    for (let m = 0; m < doc.length; m++) {
                        let spanString;
                        if (doc[m].deprecated == 'true') {
                            spanString = '<span class="line-through">';
                        } else {
                            spanString = '<span>';
                        }
                        html += '<li><a href="#_' + apiGroup.order + '_'
                            + apiData[j].order + '_' + doc[m].order + '_'
                            + doc[m].desc + '">' + apiGroup.order + '.'
                            + apiData[j].order + '.' + doc[m].order + '.&nbsp;'
                            + spanString + doc[m].desc + '<span></a> </li>';
                    }
                    html += '</ul>';
                    html += '</li>';
                }

                html += '</ul>';
                html += '</li>';
            }
        }
    }
    return html;
}
