table_config = [
    {
        'field': None,
        'title': '选择',
        'display': True,
        'text':
            {
                'tpl': '<input type="checkbox" value="{n1}" />',
                'kwargs':
                    {
                        'n1': '@id',
                    }
            },
        'attrs':
            {
                'nid': '@id',
            }
    },
    {
        'field': 'id',
        'title': 'ID',
        'display': False,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@id',
                    }
            },
        'attrs':
            {
                'k1': '@hostname',
                'k2': 'v2',
            }
    },
    {
        'field': 'hostname',
        'title': '主机名',
        'display': True,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@hostname',
                    }
            },
        'attrs':
            {
                'edit-enable': 'true',
                'origin': '@hostname',
                'name': 'hostname',
            }
    },
    {
        'field': 'os_platform',
        'title': '系统',
        'display': True,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@os_platform',
                    }
            },
        'attrs':
            {
                'hostname': '@hostname',
                'k2': 'v2',
            }
    },
    {
        'field': 'os_version',
        'title': '系统版本',
        'display': False,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@os_version',
                    }
            }
    },
    {
        'field': 'manufacturer',
        'title': '制造商',
        'display': False,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@manufacturer',
                    }
            }
    },
    {
        'field': 'model',
        'title': '型号',
        'display': False,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@model',
                    }
            }
    },
    {
        'field': 'sn',
        'title': 'SN号',
        'display': True,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@sn',
                    }
            }
    },

    {
        'field': 'create_at',
        'title': '创建时间',
        'display': True,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@create_at',
                    }
            }
    },
    {
        'field': None,
        'title': '操作',
        'display': True,
        'text':
            {
                'tpl': "<a href='/del?nid={nid}'>删除</a>|<a href='/edit?nid={nid}'>编辑</a>",
                'kwargs':
                    {
                        'nid': '@id',
                    }
            }
    },
]

search_config = [
    {'name': 'hostname__contains', 'text': '主机名', 'searchType': 'input'},
    {'name': 'sn__contains', 'text': 'SN号', 'searchType': 'input'},
]
