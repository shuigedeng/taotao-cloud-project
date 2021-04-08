from repository import models


class Nic(object):
    def __init__(self):
        pass

    @classmethod
    def initial(cls):
        return cls()

    def process(self, new, server_obj):
        new_data = new.get('data')
        if new.get('status'):
            new_slot = list(new_data.keys())  # 新采集网卡插槽列表
            objs = server_obj.nic.all()
            old_slot = [obj.name for obj in objs]  # 旧网卡插槽列表(槽位字段为name)
            add_slot = list(set(new_slot).difference(set(old_slot)))  # 新增插槽
            del_slot = list(set(old_slot).difference(set(new_slot)))  # 删除插槽
            update_slot = list(set(new_slot).intersection(set(old_slot)))  # 更新插槽
            # 新增槽位
            log = []
            add_objs = []
            for slot in add_slot:
                new_data[slot]['server_obj_id'] = server_obj.id
                new_data[slot]['name'] = slot
                log.append('网卡信息：新增网卡{name}；网卡mac地址{hwaddr}；子网掩码{netmask}；IP地址{ipaddrs}；状态{up}'.format(
                    **new_data[slot]))
                add_objs.append(models.NIC(**new_data.get(slot)))
            models.NIC.objects.bulk_create(add_objs, 5)
            if log:
                models.AssetRecord.objects.create(asset_obj=server_obj.asset, content='新增网卡：%s' % ('；'.join(log)))
            # 删除槽位
            if del_slot:
                models.NIC.objects.filter(server_obj=server_obj, slot__in=del_slot).delete()
                models.AssetRecord.objects.create(asset_obj=server_obj.asset, content='移除网卡：%s' % ('；'.join(del_slot)))
            # 更新槽位
            log = []
            field_map = {'name': '网卡名称', 'hwaddr': '网卡mac地址', 'netmask': '子网掩码', 'ipaddrs': 'IP地址', 'up': '状态'}
            for slot in update_slot:
                slot_data = new_data.get(slot)
                slot_obj = models.NIC.objects.filter(name=slot, server_obj=server_obj).first()
                for k, v in slot_data.items():
                    value = getattr(slot_obj, k)
                    if v != value:
                        log.append('网卡槽位：%s，%s由%s变为%s' % (slot, field_map.get(k), value, v))
                        setattr(slot_obj, k, v)
                slot_obj.save()
            if log:
                models.AssetRecord.objects.create(asset_obj=server_obj.asset, content='；'.join(log))
        else:
            models.ErrorLog.objects.create(title='网卡网卡信息采集出错', content=new_data)
