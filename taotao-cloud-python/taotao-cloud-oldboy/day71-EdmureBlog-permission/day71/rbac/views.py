from django.shortcuts import render,HttpResponse
from . import models
def login(request):
    username = request.GET.get('u')
    # ########## 获取角色列表 ##########
    # 方式一 : role_list
    # 前提：m = models.ManyToManyField("Role")
    # user_obj = models.User.objects.get(username=username)
    # role_list = user_obj.m.all() # [Role(),Role]

    # 方式二 : user2role_list
    # user_obj = models.User.objects.get(username=username)
    # user2role_list = models.User2Role.objects.filter(u=user_obj) #[User2Role,User2Role,]
    # print(user2role_list)
    # 循环，把所有role_id, [1,2,3,4]

    # 方式三: role_list
    # user_obj = models.User.objects.get(username=username)
    # role_list = models.Role.objects.filter(user2role__u=user_obj)
    # print(role_list)

    # 方式四: role_list = [<Role: 搬砖者>, <Role: 按摩>, <Role: 技师>]
    role_list = models.Role.objects.filter(user2role__u__username=username)
    print(role_list)

    # 注意：
    # v = models.Permission2Action2Role.objects.filter(r__in=role_list) #　［Permission2Action2Role，Permission2Action2Role］
    # models.Permission2Action2Role.objects.filter(r_id__in=[1,3,4,5])
    # print(v)
    # ########## 获取跟人权限列表 ##########
    # 获取个人所有权限列表，放置在session中，缺点：无法获取实时权限信息，需重新登录
    # 11个带着帽子的鬼
    permission2action_list = models.Permission2Action.objects.\
        filter(permission2action2role__r__in=role_list).\
        values('p__url','a__code').distinct()
    # for item in permission2action_list:
    #     print(item)

    # 4个人
    # url_list = models.Permission2Action.objects. \
    #     filter(permission2action2role__r__in=role_list). \
    #     values('p__url','p__caption').distinct()
    # for item in url_list:
    #     print(item)

    # ??????????????? 2个人, 应该在菜单中显示的 权限 --- 在最后一层 ????????????????????
    menu_leaf_list = models.Permission2Action.objects. \
        filter(permission2action2role__r__in=role_list).exclude(p__menu__isnull=True). \
        values('p_id','p__url', 'p__caption','p__menu').distinct()
    menu_leaf_dict = {}
    for item in menu_leaf_list:
        # {'p__menu': 4, 'p__url': '/report.html', 'p__caption': '报表管理'}
        item = {
            'id': item['p_id'],
            'url': item['p__url'],
            'caption': item['p__caption'],
            'parent_id': item['p__menu'],
            'child': []
        }
        if item['parent_id'] in menu_leaf_dict:
            menu_leaf_dict[item['parent_id']].append(item)
        else:
            menu_leaf_dict[item['parent_id']] = [item,]
    print('挂钩上的位置....')
    menu_list = models.Menu.objects.values('id','caption','parent_id')
    menu_dict = {}
    for item in menu_list:
        item['child'] = []
        menu_dict[item['id']] = item

    # 把袜子挂载衣钩上
    for k,v in menu_leaf_dict.items():
        menu_dict[k]['child'] = v


    # ##################### 处理等级关系
    #　menu_dict: 应用：评论（models.xx.objects.values('...')）
    result = []
    for row in menu_dict.values():
        if not row['parent_id']:
            result.append(row)
        else:
            menu_dict[row['parent_id']]['child'].append(row)

    for item in result:
        print(item['caption'])
        for r in item['child']:
            print('----',r['caption'])
            for n in r['child']:
                print('-------->',n['caption'])
    return HttpResponse('...')

