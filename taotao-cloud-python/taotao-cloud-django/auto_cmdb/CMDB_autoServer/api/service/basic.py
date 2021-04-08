from repository import models


class Basic(object):
    def __init__(self):
        pass

    @classmethod
    def initial(cls):
        return cls()

    def process(self, new, server_obj):
        print(new)
        new_data = new.get('data')
        if new.get('status'):
            print('start..........')
            log = []
            if server_obj.os_platform != new_data.get('os_platform'):
                log.append('基本信息：系统由 %s 变为 %s' % (server_obj.os_platform, new_data.get('os_platform')))
                server_obj.os_platform = new_data.get('os_platform')
            if server_obj.os_version != new_data.get('os_version'):
                log.append('基本信息：系统版本由 %s 变为 %s' % (server_obj.os_version, new_data.get('os_version')))
                server_obj.os_version = new_data.get('os_version')
            server_obj.save()
            if log:
                models.AssetRecord.objects.create(asset_obj=server_obj.asset, content=';'.join(log))
        else:
            print('errors...............')
            models.ErrorLog.objects.create(title='基本信息采集出错', content=new_data)


