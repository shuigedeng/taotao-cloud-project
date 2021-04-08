/**
 * Created by jack on 2017/8/2.
 */
(function (jq) {
    var CREATE_SEARCH_CONDITION = true;
    var GLOBAL_DICT = {};
    /*
     {
     'device_type_choices': (
     (1, '服务器'),
     (2, '交换机'),
     (3, '防火墙'),
     )
     'device_status_choices': (
     (1, '上架'),
     (2, '在线'),
     (3, '离线'),
     (4, '下架'),
     )
     }
     */

    /*自定义字符串格式化*/
    String.prototype.Format = function (args) {
        /*this代表要调用Format方法的字符串*/
        /*replace的第一个参数为正则表达式，g表示处理匹配到的所有字符串，在js中使用//包起来*/
        /*replace的第二个参数为匹配字符串的处理，k1匹配结果包含{}，k2只保留{}内的内容*/
        var temp = this.replace(/\{(\w+)\}/g, function (k1, k2) {
            // console.log(k1, k2);
            /*replace将匹配到的k2用参数args替换后赋给新变量temp*/
            return args[k2];
        });
        /*自定义方法Format将格式化后的字符串返回*/
        return temp;
    };

    function getSearchConditon() {
        var condition = {};
        console.log('getSearchConditon');
        $(".search-list").find('input[type="text"], select').each(function () {
            // 获取所有搜索条件
            var name = $(this).attr('name');
            var value = $(this).val();
            console.log(name, value, 'search condition');
            if(condition[name]){
                condition[name].push(value);
            }else {
                condition[name] = [value];
            }
        });
        return condition;
    }

    function initial(url) {
        // 执行获取搜索条件的函数
        var searchCondition = getSearchConditon();
        console.log(searchCondition, '...');
        $.ajax({
            url: url,
            type: 'GET',
            data: {condition: JSON.stringify(searchCondition)},
            dataType: 'JSON',
            success: function (arg) {
                console.log(arg.search_config, 'search');
                $.each(arg.global_dict, function (k, v) {
                    GLOBAL_DICT[k] = v;
                });
                // console.log(GLOBAL_DICT);
                initTableHead(arg.table_config);
                initTableBody(arg.server_list, arg.table_config);
                initSearch(arg.search_config);
            }
        })
    }

    function initSearch(searchConfig) {
        if(searchConfig && CREATE_SEARCH_CONDITION){
            CREATE_SEARCH_CONDITION = false;
            $.each(searchConfig, function (k, v) {
                var li = document.createElement('li');
                $(li).attr('searchType', v.searchType);
                $(li).attr('name', v.name);
                if(v.searchType == 'select'){
                    $(li).attr('globalName', v.globalName);
                }
                var a = document.createElement('a');
                a.innerHTML = v.text;
                $(li).append(a);
                $(".searchArea ul").append(li);
            });
            // 初始化默认搜索条件
            // searchConfig[0]初始化
            // 初始化默认选中值
            console.log(searchConfig[0].text, 'searchConfig[0].text');
            $(".search-item .searchDefault").text(searchConfig[0].text);
            if(searchConfig[0].searchType == 'select'){
                var sel = document.createElement('select');
                $(sel).attr('class','form-control');
                $.each(GLOBAL_DICT[searchConfig[0].globalName], function (k, v) {
                    var op = document.createElement('option');
                    $(op).text(v[1]);
                    $(op).text(v[0]);
                    $(sel).append(op);
                });
                $(".input-group").append(sel);
            }else {
                var inp = document.createElement('input');
                $(inp).attr('class', 'form-control');
                $(inp).attr('name', searchConfig[0].name);
                $(inp).attr('type', 'text');
                $(".input-group").append(inp);
            }
        }
    }

    function initTableHead(table_config) {
        $("#tbHead").empty();    // 刷新操作前清空标题
        var tr = document.createElement('tr');
        $.each(table_config, function (k, v) {
            if (v.display) {
                var th = document.createElement('th');
                th.innerHTML = v.title;
                $(tr).append(th);
            }
        });
        $("#tbHead").append(tr);
    }

    function initTableBody(server_list, table_config) {
        $("#tbBody").empty();
        $.each(server_list, function (k, row) {
            var tr = document.createElement('tr');
            tr.setAttribute('nid', row.id);
            $.each(table_config, function (tk, trow) {
                if (trow.display) {
                    var td = document.createElement('td');
                    var newKwargs = {};
                    $.each(trow.text.kwargs, function (ttk, ttrow) {
                        var tvalue = ttrow;
                        if (ttrow.substring(0, 2) == '@@') {
                            var global_dict_key = ttrow.substring(2);       //返回从索引2开始到结束的字符串
                            var nid = row[trow.field];
                            // console.log(tvalue, 'tvalue', trow.field, nid);
                            $.each(GLOBAL_DICT[global_dict_key], function (gk, gv) {
                                if (gv[0] == nid) {
                                    tvalue = gv[1];
                                }
                            })
                        }
                        else if (ttrow[0] == '@') {
                            tvalue = row[ttrow.substring(1)];
                        }
                        newKwargs[ttk] = tvalue;
                    });
                    var newText = trow.text.tpl.Format(newKwargs);
                    td.innerHTML = newText;
                    /* 在td标签中添加属性 */
                    $.each(trow.attrs, function (akey, aval) {
                        if (aval[0] == '@') {
                            td.setAttribute(akey, row[aval.substring(1)]);
                        } else {
                            td.setAttribute(akey, aval);
                        }
                    });
                    $(tr).append(td);
                }
            });
            $("#tbBody").append(tr);
        })
    }

    // 进入编辑状态
    function trToEdit($tr) {
        $tr.find('td[edit-enable="true"]').each(function () {
            // $(this) 每一个可编辑的td标签
            var editType = $(this).attr('edit-type');
            if (editType == 'select') {
                // 找到生成下拉框的数据源
                var selectData = GLOBAL_DICT[$(this).attr('global-key')];
                // 生成select标签
                var selTag = document.createElement('select');
                var selectId = $(this).attr('origin');

                $.each(selectData, function (k, v) {
                    var op = document.createElement('option');
                    $(op).text(v[1]);
                    $(op).val(v[0]);
                    if (v[0] == selectId) {
                        // 显示默认值
                        $(op).prop('selected', true);
                    }
                    $(selTag).append(op);
                });
                $(this).html(selTag);
            } else {
                // 获取原来td标签的文本
                var t1 = $(this).text();
                // 创建input标签，并设置值
                var inp = document.createElement('input');
                $(inp).val(t1);
                $(this).html(inp);
            }
        })
    }

    // 退出编辑状态
    function trOutEdit($tr) {
        $tr.find('td[edit-enable="true"]').each(function () {
            // 每一个可编辑td
            var editType = $(this).attr('edit-type');
            if (editType == 'select') {
                var op = $(this).find('select')[0].selectedOptions;
                $(this).attr('new-origin', $(op).val());
                $(this).html($(op).text());
            } else {
                var inputVal = $(this).find('input').val();
                $(this).html(inputVal);
            }
        })
    }

    jq.extend({
        init_asset: function (url) {
            initial(url);

            // 所有checkbox绑定事件
            $('#tbBody').on('click', ':checkbox', function () {
                // 已选中的默认进入编辑状态
                if ($("#editMode").hasClass('btn-warning')) {
                    var $tr = $(this).parent().parent();
                    if ($(this).prop('checked')) {
                        trToEdit($tr);
                    } else {
                        trOutEdit($tr);
                    }
                }
            });

            // 所有按钮绑定事件
            // 全选
            $("#checkAll").click(function () {
                if ($("#editMode").hasClass('btn-warning')) {
                    $("#tbBody").find(':checkbox').each(function () {
                        if (!$(this).prop('checked')) {
                            var $tr = $(this).parent().parent();
                            trToEdit($tr);
                            $(this).prop('checked', true);
                        }
                    })
                } else {
                    $("#tbBody").find(':checkbox').prop('checked', true);
                }
            });

            // 反选
            $("#checkReverse").click(function () {
                if ($("#editMode").hasClass('btn-warning')) {
                    $("#tbBody").find(':checkbox').each(function () {
                        var $tr = $(this).parent().parent();
                        if ($(this).prop('checked')) {    // 已选中的退出编辑，去掉选中状态
                            trOutEdit($tr);
                            $(this).prop('checked', false);
                        } else {
                            trToEdit($tr);
                            $(this).prop('checked', true);
                        }
                    })
                } else {
                    $("#tbBody").find(':checkbox').each(function () {
                        var $tr = $(this).parent().parent();
                        if ($(this).prop('checked')) {
                            $(this).prop('checked', false);
                        } else {
                            $(this).prop('checked', true);
                        }
                    })
                }
            });

            // 全部取消
            $("#checkCancel").click(function () {
                if ($("#editMode").hasClass('btn-warning')) {
                    $("#tbBody").find(':checkbox').each(function () {
                        if ($(this).prop('checked')) {
                            var $tr = $(this).parent().parent();
                            trOutEdit($tr);
                            $(this).prop('checked', false);
                        }
                    })
                } else {
                    $("#tbBody").find(':checkbox').prop('checked', false);
                }
            });

            // 编辑按钮
            $("#editMode").click(function () {
                if ($(this).hasClass('btn-warning')) {
                    // 退出编辑
                    $(this).removeClass('btn-warning');
                    $(this).text('编辑模式');
                    $("#tbBody").find(':checkbox').each(function () {
                        if ($(this).prop('checked')) {
                            var $tr = $(this).parent().parent();
                            trOutEdit($tr);
                        }
                    })
                } else {
                    // 进入编辑
                    $(this).addClass('btn-warning');
                    $(this).text('退出编辑');
                    $("#tbBody").find(':checkbox').each(function () {
                        if ($(this).prop('checked')) {
                            var $tr = $(this).parent().parent();
                            trToEdit($tr);
                        }
                    })
                }
            });

            //批量删除
            $("#multiDel").click(function () {
                var idList = [];
                $("#tbBody").find(':checkbox').each(function () {
                    var v = $(this).val();
                    idList.push(v);
                });
                $.ajax({
                    url: url,
                    type: 'delete',
                    data: JSON.stringify(idList),
                    success: function (arg) {
                        console.log(arg);
                    }
                })
            });

            // 刷新
            $("#refresh").click(function () {
                initial(url);
            });

            // 保存
            $("#save").click(function () {
                if ($("#editMode").hasClass('btn-warning')) {
                    $("#tbBody").find(':checkbox').each(function () {
                        if ($(this).prop('checked')) {
                            var $tr = $(this).parent().parent();
                            trOutEdit($tr);
                        }
                    })
                }
                var dataList = [];  //保存修改后的数据
                $("#tbBody").children().each(function () {
                    var $tr = $(this);
                    var nid = $tr.attr('nid');
                    var rowDict = {};
                    var flag = false;   // 判断字典是否为空，若为空，表明用户未修改任何数据
                    $tr.children().each(function () {
                        if ($(this).attr('edit-enable')) {
                            if ($(this).attr('edit-type') == 'select') {      // select标签
                                var newData = $(this).attr('new-origin');
                                var oldData = $(this).attr('origin');
                                console.log(newData, oldData);
                                if (newData) {
                                    if (newData != oldData) {
                                        var name = $(this).attr('name');
                                        rowDict[name] = newData;
                                        flag = true;
                                    }
                                }
                            } else {     // input标签
                                var newData = $(this).text();
                                var oldData = $(this).attr('origin');
                                if (newData != oldData) {
                                    var name = $(this).attr('name');
                                    rowDict[name] = newData;
                                    flag = true;
                                }
                            }
                        }
                    });
                    if (flag) {
                        rowDict['id'] = nid;
                        dataList.push(rowDict);
                    }
                    // dataList.push(rowDict);
                });
                console.log(dataList);
                if (dataList.length != 0) {
                    // Ajax提交
                    $.ajax({
                        url: url,
                        type: 'put',
                        data: JSON.stringify(dataList),
                        success: function (arg) {
                            console.log('put', arg);
                        }
                    })
                }else {
                    alert('未作任何修改');
                }
            });
            
            $(".search-list").on('click', 'li', function () {
                // 点击li标签执行函数(下拉菜单选项)
                var content = $(this).text();
                var searchType = $(this).attr('searchType');
                var name = $(this).attr('name');
                var globalName = $(this).attr('globalName');
                // 替换显示
                $(this).parent().prev().find('.searchDefault').text(content);
                console.log(content, 'content');
                if(searchType == 'select'){
                    var sel = document.createElement('select');
                    $(sel).attr('class', 'form-control');
                    $(sel).attr('name', name);
                    $.each(GLOBAL_DICT[globalName], function (k, v) {
                        var op = document.createElement('option');
                        $(op).text(v[1]);
                        $(op).val(v[0]);
                        $(sel).append(op);
                    });
                    $(this).parent().parent().next().remove();
                    $(this).parent().parent().after(sel);
                }else {
                    var inp = document.createElement('input');
                    $(inp).attr('class', 'form-control');
                    $(inp).attr('name', name);
                    $(inp).attr('type', 'text');
                    $(this).parent().parent().next().remove();
                    $(this).parent().parent().after(inp);
                }
            });

            $(".search-list").on('click', '.add-search-condition', function () {
                // copy新的搜索项
                var newSearchItem = $(this).parent().parent().clone();
                $(newSearchItem).find('.add-search-condition span').removeClass('glyphicon-plus').addClass('glyphicon-minus');
                $(newSearchItem).find('.add-search-condition').addClass('del-search-condition').removeClass('add-search-condition');
                $('.search-list').append(newSearchItem);
            });

            $(".search-list").on('click', '.del-search-condition', function () {
                $(this).parent().parent().remove();
            });

            $("#doSearch").click(function () {
                initial(url);
            })

        }
    })
})(jQuery);