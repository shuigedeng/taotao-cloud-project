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
                'k1': '@id',
                'k2': 'v2',
            }
    },
    {
        'field': 'name',
        'title': '机房',
        'display': True,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@name',
                    }
            },
        'attrs': {
            'name': 'name',
            'origin': '@name',
            'edit-enable': 'true',
        },
    },
    {
        'field': 'floor',
        'title': '楼层',
        'display': True,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@floor',
                    }
            },
        'attrs': {
            'name': 'floor',
            'origin': '@floor',
            'edit-enable': 'true',
        },
    },
    {
        'field': None,
        'title': '操作',
        'display': True,
        'text':
            {
                'tpl': "<a href='/del?nid={nid}'>删除</a>",
                'kwargs':
                    {
                        'nid': '@id',
                    }
            },
        'attrs':
            {
                'k1': '@id',
                'k2': 'v2',
            }
    },
]
