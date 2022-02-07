export interface IMenu {
  title: string
  path: string
  icon: string
  roles: string[]
  children?: IMenu[]
}

const menuConfig: IMenu[] = [
  {
    title: '首页',
    path: '/dashboard',
    icon: 'iconhome',
    roles: ['admin', 'product', 'order']
  },
  {
    title: '用户管理',
    path: '/uc',
    icon: 'iconbulb',
    roles: ['admin', 'product', 'order'],
    children: [
      {
        title: '菜单管理',
        path: '/uc/user',
        icon: '',
        roles: ['admin', 'order']
      },
      {
        title: '角色管理',
        path: '/uc/role',
        icon: '',
        roles: ['admin', 'uc', 'order']
      },
      {
        title: '资源管理',
        path: '/uc/resource',
        icon: '',
        roles: ['admin', 'uc', 'order']
      }
    ]
  },
  {
    title: '系统管理',
    path: '/sys',
    icon: 'iconbulb',
    roles: ['admin', 'product', 'order'],
    children: [
      {
        title: '岗位管理',
        path: '/sys/job',
        icon: '',
        roles: ['admin', 'order']
      },
      {
        title: '字典管理',
        path: '/sys/dict',
        icon: '',
        roles: ['admin', 'uc', 'order']
      },
      {
        title: '部门管理',
        path: '/sys/dept',
        icon: '',
        roles: ['admin', 'uc', 'order']
      },
      {
        title: '公司管理',
        path: '/sys/company',
        icon: '',
        roles: ['admin', 'uc', 'order']
      }
    ]
  },
  {
    title: '商品管理',
    path: '/product',
    icon: 'iconbulb',
    roles: ['admin', 'product', 'order'],
    children: [
      {
        title: '商品管理',
        path: '/product/manage',
        icon: '',
        roles: ['admin', 'order']
      },
      {
        title: '分类管理',
        path: '/product/classify',
        icon: '',
        roles: ['admin', 'order']
      },
      {
        title: '评论管理',
        path: '/product/comment',
        icon: '',
        roles: ['admin', 'uc', 'order']
      }
    ]
  },
  {
    title: '订单管理',
    path: '/order',
    icon: 'iconbulb',
    roles: ['admin', 'product', 'order'],
    children: [
      {
        title: '订单管理',
        path: '/order/manage',
        icon: '',
        roles: ['admin', 'order']
      },
      {
        title: '超时订单管理',
        path: '/order/overtime',
        icon: '',
        roles: ['admin', 'order']
      },
      {
        title: '订单支付管理',
        path: '/order/pay',
        icon: '',
        roles: ['admin', 'uc', 'order']
      },
      {
        title: '订单退款管理',
        path: '/order/refund/pay',
        icon: '',
        roles: ['admin', 'uc', 'order']
      },
      {
        title: '订单售后管理',
        path: '/order/refund/req',
        icon: '',
        roles: ['admin', 'uc', 'order']
      }
    ]
  },
  {
    title: '导航',
    path: '/nav',
    icon: 'iconbulb',
    roles: ['admin', 'order'],
    children: [
      {
        title: '下拉菜单',
        path: '/nav/dropdown',
        icon: '',
        roles: ['admin', 'order']
      },
      {
        title: '导航菜单',
        path: '/nav/menu',
        icon: '',
        roles: ['admin', 'order']
      },
      {
        title: '步骤条',
        path: '/nav/steps',
        icon: '',
        roles: ['admin']
      }
    ]
  },
  {
    title: '表单',
    path: '/form',
    icon: 'iconform',
    roles: ['admin'],
    children: [
      {
        title: '基础表单',
        path: '/form/base-form',
        icon: '',
        roles: ['admin']
      },
      {
        title: '步骤表单',
        path: '/form/step-form',
        icon: '',
        roles: ['admin']
      }
    ]
  },
  {
    title: '展示',
    path: '/show',
    icon: 'iconPiechart',
    roles: ['admin'],
    children: [
      {
        title: '表格',
        path: '/show/table',
        icon: '',
        roles: ['admin']
      },
      {
        title: '折叠面板',
        path: '/show/collapse',
        icon: '',
        roles: ['admin']
      },
      {
        title: '树形控件',
        path: '/show/tree',
        icon: '',
        roles: ['admin']
      },
      {
        title: '标签页',
        path: '/show/tabs',
        icon: '',
        roles: ['admin']
      }
    ]
  },
  {
    title: '其它',
    path: '/others',
    icon: 'iconpaper-clip',
    roles: ['admin'],
    children: [
      {
        title: '进度条',
        path: '/others/progress',
        icon: '',
        roles: ['admin']
      },
      {
        title: '动画',
        path: '/others/animation',
        icon: '',
        roles: ['admin']
      },
      {
        title: '上传',
        path: '/others/upload',
        icon: '',
        roles: ['admin']
      },
      {
        title: '富文本',
        path: '/others/editor',
        icon: '',
        roles: ['admin']
      },
      {
        title: '404',
        path: '/404',
        icon: '',
        roles: ['admin']
      },
      {
        title: '500',
        path: '/500',
        icon: '',
        roles: ['admin']
      }
    ]
  },
  {
    title: '多级导航',
    path: '/one',
    icon: 'iconbars',
    roles: ['admin'],
    children: [
      {
        title: '二级',
        path: '/one/two',
        icon: '',
        roles: ['admin'],
        children: [
          {
            title: '三级',
            path: '/one/two/three',
            icon: '',
            roles: ['admin']
          }
        ]
      }
    ]
  },
  {
    title: '关于',
    path: '/about',
    icon: 'iconabout',
    roles: ['admin']
  }
]

export default menuConfig
