from django.conf import settings
from repository import models
import importlib


class PluginsManage(object):
    def __init__(self, data):
        self.data = data
        self.server_obj = None
        if self.data.get('Basic').get('status'):  # get不会报错，没有该key，取值为None
            self.hostname = self.data.get('Basic').get('data').get('hostname')
            # print(self.hostname, 'hostname')
            if self.hostname:
                self.server_obj = models.Server.objects.filter(hostname=self.hostname).first()
                # print(self.server_obj, 'server')


    def exec_plugin(self):
        if not self.server_obj:
            return False
        for k, v in settings.PLUGINS.items():
            plugin_model, plugin_class = v.rsplit('.', 1)
            # print(plugin_model, plugin_class)
            plugin_data = self.data.get(plugin_class)
            model = importlib.import_module(plugin_model)
            # print(plugin_data, 'data.....')
            cls = getattr(model, plugin_class)
            if hasattr(model, 'initial'):
                obj = cls().initial()
                # print('....initial')
            else:
                obj = cls()
            obj.process(plugin_data, self.server_obj)
        return True