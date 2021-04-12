from repository import models


class Cpu(object):
    def __init__(self):
        pass

    @classmethod
    def initial(cls):
        return cls()

    def process(self, new, server_obj):
        new_data = new.get('data')
        if new.get('status'):
            log = []
            if server_obj.cpu_count != new_data.get('cpu_count'):
                log.append('CPU信息：个数由 %s 变为 %s' % (server_obj.cpu_count, new_data.get('cpu_count')))
                server_obj.cpu_count = new_data.get('cpu_count')
            if server_obj.cpu_physical_count != new_data.get('cpu_physical_count'):
                log.append('CPU信息：物理个数由 %s 变为 %s' % (server_obj.cpu_physical_count, new_data.get('cpu_physical_count')))
                server_obj.cpu_physical_count = new_data.get('cpu_physical_count')
            if server_obj.cpu_model != new_data.get('cpu_model'):
                log.append('CPU信息：型号由 %s 变为 %s' % (server_obj.cpu_model, new_data.get('cpu_model')))
                server_obj.cpu_model = new_data.get('cpu_model')
            server_obj.save()
            if log:
                models.AssetRecord.objects.create(asset_obj=server_obj.asset, content=';'.join(log))
        else:
            models.ErrorLog.objects.create(title='Cpu信息采集出错', content=new_data)
