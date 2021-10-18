let api = [];
const apiDocListSize = 1
api.push({
    name: 'default',
    order: '1',
    list: []
})
api[0].list.push({
    alias: 'SysDictController',
    order: '1',
    link: '字典管理api',
    desc: '字典管理API',
    list: []
})
api[0].list.push({
    alias: 'SysJobController',
    order: '2',
    link: '岗位管理api',
    desc: '岗位管理API',
    list: []
})
api[0].list.push({
    alias: 'SysCompanyController',
    order: '3',
    link: '公司管理api',
    desc: '公司管理API',
    list: []
})
api[0].list.push({
    alias: 'SysUserController',
    order: '4',
    link: '后台用户管理api',
    desc: '后台用户管理API',
    list: []
})
api[0].list[3].list.push({
    order: '1',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/user/exists/phone/{phone}',
    desc: '根据手机号码查询用户是否存在',
});
api[0].list[3].list.push({
    order: '2',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/user/exists/id/{userId}',
    desc: '根据用户id查询用户是否存在',
});
api[0].list[3].list.push({
    order: '3',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/user/rest/password/{userId}',
    desc: '重置密码',
});
api[0].list[3].list.push({
    order: '4',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/user/current',
    desc: '获取当前登录人信息',
});
api[0].list[3].list.push({
    order: '5',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/user/roles/{userId}',
    desc: '根据用户id更新角色信息(用户分配角色)',
});
api[0].list.push({
    alias: 'SysResourceController',
    order: '5',
    link: '资源管理api',
    desc: '资源管理API',
    list: []
})
api[0].list[4].list.push({
    order: '1',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/resource/roleId/{roleId}',
    desc: '根据角色id获取资源列表',
});
api[0].list[4].list.push({
    order: '2',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/resource/roleIds',
    desc: '根据角色id列表获取角色列表',
});
api[0].list[4].list.push({
    order: '3',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/resource/code/{code}',
    desc: '根据角色code获取资源列表',
});
api[0].list[4].list.push({
    order: '4',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/resource/codes',
    desc: '根据角色code列表获取角色列表',
});
api[0].list[4].list.push({
    order: '5',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/resource/current/user',
    desc: '获取当前用户菜单列表',
});
api[0].list[4].list.push({
    order: '6',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/resource/current/user/tree',
    desc: '获取当前用户树形菜单列表',
});
api[0].list[4].list.push({
    order: '7',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/resource/tree',
    desc: '获取树形菜单集合1.false-非懒加载，查询全部2.true-懒加载，根据parentId查询2.1父节点为空，则查询parentId=0',
});
api[0].list[4].list.push({
    order: '8',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/resource/test/se',
    desc: '',
});
api[0].list[4].list.push({
    order: '9',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/resource/test/async',
    desc: '',
});
api[0].list[4].list.push({
    order: '10',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/resource/test/async/future',
    desc: '',
});
api[0].list[4].list.push({
    order: '11',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/resource/test/seata',
    desc: '',
});
api[0].list.push({
    alias: 'SysDeptController',
    order: '6',
    link: '部门管理api',
    desc: '部门管理API',
    list: []
})
api[0].list.push({
    alias: 'SysRegionController',
    order: '7',
    link: '地区管理api',
    desc: '地区管理API',
    list: []
})
api[0].list[6].list.push({
    order: '1',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/region/parentId/{parentId}',
    desc: '根据父id查询地区数据',
});
api[0].list[6].list.push({
    order: '2',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/region/tree',
    desc: '树形结构查询',
});
api[0].list.push({
    alias: 'SysDictItemController',
    order: '8',
    link: '字典项管理api',
    desc: '字典项管理API',
    list: []
})
api[0].list.push({
    alias: 'SysRoleController',
    order: '9',
    link: '角色管理api',
    desc: '角色管理API',
    list: []
})
api[0].list[8].list.push({
    order: '1',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/role/userId/{userId}',
    desc: '根据用户id获取角色列表',
});
api[0].list[8].list.push({
    order: '2',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/role/userId',
    desc: '根据用户id列表获取角色列表',
});
api[0].list[8].list.push({
    order: '3',
    deprecated: 'false',
    url: 'http://dev.taotaocloud.com/api/v2021.10.1/uc/role/resources/{roleId}',
    desc: '根据角色id更新资源信息(角色分配资源)',
});
api[0].list.push({
    alias: 'error',
    order: '10',
    link: 'error_code_list',
    desc: '错误码列表',
    list: []
})
api[0].list.push({
    alias: 'dict',
    order: '11',
    link: 'dict_list',
    desc: '数据字典',
    list: []
})
api[0].list[10].list.push({
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
            html = buildAccordion(api,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        } else {
            const liClass = "open";
            const display = "display: block";
            html = buildAccordion(searchGroup,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        }
        const Accordion = function (el, multiple) {
            this.el = el || {};
            this.multiple = multiple || false;
            const links = this.el.find('.dd');
            links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown);
        };
        Accordion.prototype.dropdown = function (e) {
            const $el = e.data.el;
            $this = $(this), $next = $this.next();
            $next.slideToggle();
            $this.parent().toggleClass('open');
            if (!e.data.multiple) {
                $el.find('.submenu').not($next).slideUp("20").parent().removeClass('open');
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
                html += '<li class="'+liClass+'">';
                html += '<a class="dd" href="#_' + apiData[j].link + '">' + apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
                html += '<ul class="sectlevel2" style="'+display+'">';
                doc = apiData[j].list;
                for (let m = 0; m < doc.length; m++) {
                    let spanString;
                    if (doc[m].deprecated == 'true') {
                        spanString='<span class="line-through">';
                    } else {
                        spanString='<span>';
                    }
                    html += '<li><a href="#_' + apiData[j].order + '_' + doc[m].order + '_' + doc[m].href + '">' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + spanString + doc[m].desc + '<span></a> </li>';
                }
                html += '</ul>';
                html += '</li>';
            }
        } else {
            for (let i = 0; i < apiGroups.length; i++) {
                let apiGroup = apiGroups[i];
                html += '<li class="'+liClass+'">';
                html += '<a class="dd" href="#_' + apiGroup.name + '">' + apiGroup.order + '.&nbsp;' + apiGroup.name + '</a>';
                html += '<ul class="sectlevel1">';

                let apiData = apiGroup.list;
                for (let j = 0; j < apiData.length; j++) {
                    html += '<li class="'+liClass+'">';
                    html += '<a class="dd" href="#_'+apiGroup.order+'_'+ apiData[j].order + '_'+ apiData[j].link + '">' +apiGroup.order+'.'+ apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
                    html += '<ul class="sectlevel2" style="'+display+'">';
                    doc = apiData[j].list;
                    for (let m = 0; m < doc.length; m++) {
                       let spanString;
                       if (doc[m].deprecated == 'true') {
                           spanString='<span class="line-through">';
                       } else {
                           spanString='<span>';
                       }
                       html += '<li><a href="#_'+apiGroup.order+'_' + apiData[j].order + '_' + doc[m].order + '_' + doc[m].desc + '">'+apiGroup.order+'.' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + spanString + doc[m].desc + '<span></a> </li>';
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