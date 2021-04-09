from repository import models


class Board(object):
    def __init__(self):
        pass

    @classmethod
    def initial(cls):
        return cls()

    def process(self, new, server_obj):
        new_data = new.get('data')
        if new.get('status'):
            log = []
            if server_obj.sn != new_data.get('sn'):
                log.append('主板信息：SN号由 %s 变为 %s' % (server_obj.sn, new_data.get('sn')))
                server_obj.sn = new_data.get('sn')
            if server_obj.manufacturer != new_data.get('manufacturer'):
                log.append('主板信息：制造商由 %s 变为 %s' % (server_obj.manufacturer, new_data.get('manufacturer')))
                server_obj.manufacturer = new_data.get('manufacturer')
            if server_obj.model != new_data.get('model'):
                log.append('主板信息：型号由 %s 变为 %s' % (server_obj.model, new_data.get('model')))
                server_obj.model = new_data.get('model')
            server_obj.save()
            if log:
                models.AssetRecord.objects.create(asset_obj=server_obj.asset, content=';'.join(log))
        else:
            models.ErrorLog.objects.create(title='主板信息采集出错', content=new_data)
