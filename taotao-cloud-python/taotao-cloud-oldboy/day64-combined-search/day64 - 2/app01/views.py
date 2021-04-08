from django.shortcuts import render
from app01 import models

def video(request,*args,**kwargs):
    condition = {
        # 'level_id': 0,
        # 'classification_id': 0,
    }
    for k,v in kwargs.items():
        temp = int(v)
        kwargs[k] = temp
        if temp:
            condition[k] = temp

    class_list = models.Classification.objects.all()
    level_list = models.Level.objects.all()
    """
    status_choice = (
        (1, '下线'),
        (2, '上线'),
    )
    """
    status_list = list(map(lambda x:{'id':x[0],'name':x[1]},models.Video.status_choice))

    video_list = models.Video.objects.filter(**condition)

    return render(
        request,
        'video.html',
        {
            'class_list':class_list,
            'level_list':level_list,
            'status_list':status_list,
            'kwargs':kwargs,
            'video_list':video_list,
        }
    )

def video2(request,*args,**kwargs):
    condition = {}

    for k, v in kwargs.items():
        temp = int(v)
        kwargs[k] = temp
    print(kwargs) #　(?P<direction_id>(\d+))-(?P<classification_id>(\d+))-(?P<level_id>(\d+))
    # 构造查询字典
    direction_id = kwargs.get('direction_id')
    classification_id = kwargs.get('classification_id')
    level_id = kwargs.get('level_id')

    direction_list = models.Direction.objects.all()

    if direction_id == 0:
        class_list = models.Classification.objects.all()
        if classification_id == 0:
            pass
        else:
            condition['classification_id'] = classification_id
    else:
        direction_obj = models.Direction.objects.filter(id=direction_id).first()
        class_list = direction_obj.classification.all()

        vlist = direction_obj.classification.all().values_list('id')
        if not vlist:
            classification_id_list = []
        else:
            classification_id_list = list(zip(*vlist))[0]

        if classification_id == 0:
            condition['classification_id__in'] = classification_id_list
        else:
            if classification_id in classification_id_list:
                condition['classification_id'] = classification_id
            else:
                #################指定方向：[1,2,3]   分类：5
                condition['classification_id__in'] = classification_id_list

    if level_id == 0:
        pass
    else:
        condition['level_id'] = level_id

    level_list = models.Level.objects.all()

    video_list = models.Video.objects.filter(**condition)

    return render(
        request,
        'video2.html',
        {
            'direction_list':direction_list,
            'class_list':class_list,
            'level_list':level_list,
            'video_list':video_list

        }
    )
    """
    如果：direction_id 0
        *列出所有的分类
        如果 classification_id = 0：
            pass
        else:
            condition['classification_id'] = classification_id

    否则：direction_id != 0
        *列表当前方向下的所有分类
         如果 classification_id = 0：
            获取当前方向下的所有分类 [1,2,3,4]
            condition['classification_id__in'] = [1,2,3,4]
         else:
            classification_id != 0
            获取当前方向下的所有分类 [1,2,3,4]
            classification_id 是否在 [1,2,3,4]  :
                condition['classification_id'] = classification_id
            else:
                condition['classification_id__in'] = [1,2,3,4]


    """
    # models.Video.objects.filter()
    # return render(request,'video2.html')