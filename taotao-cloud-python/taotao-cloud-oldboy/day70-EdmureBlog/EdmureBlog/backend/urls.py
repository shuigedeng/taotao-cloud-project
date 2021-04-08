from django.conf.urls import url
from django.conf.urls import include
from .views import user
from .views import trouble
urlpatterns = [
    url(r'^index.html$', user.index),
    url(r'^base-info.html$', user.base_info),
    url(r'^tag.html$', user.tag),
    url(r'^category.html$', user.category),
    url(r'^article-(?P<article_type_id>\d+)-(?P<category_id>\d+).html$', user.article,name='article'),
    url(r'^add-article.html$', user.add_article),
    url(r'^edit-article-(?P<nid>\d+).html$', user.edit_article),
    url(r'^upload-avatar.html$', user.upload_avatar),

    # 一般用户： 提交报障单,查看，修改（未处理），评分（处理完成，未评分）
    url(r'^trouble-list.html$', trouble.trouble_list),
    url(r'^trouble-create.html$', trouble.trouble_create),
    url(r'^trouble-edit-(\d+).html$', trouble.trouble_edit),

    url(r'^trouble-kill-list.html$', trouble.trouble_kill_list),
    url(r'^trouble-kill-(\d+).html$', trouble.trouble_kill),
    url(r'^trouble-report.html$', trouble.trouble_report),
    url(r'^trouble-json-report.html$', trouble.trouble_json_report),
]
