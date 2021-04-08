from django.shortcuts import render



class BaseKingAdmin(object):
    def __init__(self):
        self.actions.extend(self.default_actions)

    list_display = []
    list_filter = []
    search_fields = []
    readonly_fields = []
    filter_horizontal = []
    list_per_page = 20
    default_actions = ['delete_selected_objs']
    actions = []

    def delete_selected_objs(self,request,querysets):

        return render(request,'kingadmin/table_obj_delete.html')