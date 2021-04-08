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
        'field': 'device_type_id',
        'title': '资产类型',
        'display': True,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@@device_type_choices',
                    }
            },
        'attrs':
            {
                'name': 'device_type_id',
                'origin': '@device_type_id',
                'edit-enable': 'true',
                'edit-type': 'select',
                'global-key': 'device_type_choices',
            }
    },
    {
        'field': 'device_status_id',
        'title': '资产状态',
        'display': True,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@@device_status_choices',
                    }
            },
        'attrs':
            {
                'name': 'device_status_id',
                'origin': '@device_status_id',
                'edit-enable': 'true',
                'edit-type': 'select',
                'global-key': 'device_status_choices',
            }
    },
    {
        'field': 'cabinet_num',
        'title': '机柜号',
        'display': True,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@cabinet_num',
                    }
            },
        'attrs': {
            'name': 'cabinet_num',
            'origin': '@cabinet_num',
            'edit-enable': 'true',
        },
    },
    {
        'field': 'idc__name',
        'title': '机房',
        'display': True,
        'text':
            {
                'tpl': '{n1}',
                'kwargs':
                    {
                        'n1': '@idc__name',
                    }
            },
        'attrs': {
            'name': 'idc__name',
            'origin': '@idc__name',
            'edit-enable': 'true',
        },
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
            },
        'attrs':
            {
                'k1': '@id',
                'k2': 'v2',
            }
    },
]
search_config = [
    {'name': 'cabinet_num', 'text': '机柜号', 'searchType': 'input'},
    {'name': 'device_type_id', 'text': '资产类型', 'searchType': 'select', 'globalName': 'device_type_choices'},
    {'name': 'device_status_id', 'text': '资产状态', 'searchType': 'select', 'globalName': 'device_status_choices'},
]
