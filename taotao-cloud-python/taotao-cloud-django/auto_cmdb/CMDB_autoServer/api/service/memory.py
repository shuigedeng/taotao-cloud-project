from repository import models


class Memory(object):
    def __init__(self):
        pass

    @classmethod
    def initial(cls):
        return cls()

    def process(self, new, server_obj):
        new_data = new.get('data')
        if new.get('status'):
            new_slot = list(new_data.keys())  # 新采集内存插槽列表
            objs = server_obj.memory.all()
            old_slot = [obj.slot for obj in objs]  # 旧内存插槽列表
            add_slot = list(set(new_slot).difference(set(old_slot)))     # 新增插槽
            del_slot = list(set(old_slot).difference(set(new_slot)))     # 删除插槽
            update_slot = list(set(new_slot).intersection(set(old_slot)))     # 更新插槽
            # 新增槽位
            log = []
            add_objs = []
            for slot in add_slot:
                new_data[slot]['server_obj_id'] = server_obj.id
                log.append('内存信息：新增槽位{slot}；制造商{manufacturer}；型号{model}；容量GB{capacity}；内存SN号{sn}；速度{speed}'.format(**new_data[slot]))
                add_objs.append(models.Memory(**new_data.get(slot)))
            models.Memory.objects.bulk_create(add_objs, 5)
            if log:
                models.AssetRecord.objects.create(asset_obj=server_obj.asset, content='新增内存：%s' % ('；'.join(log)))
            # 删除槽位
            if del_slot:
                models.Memory.objects.filter(server_obj=server_obj, slot__in=del_slot).delete()
                models.AssetRecord.objects.create(asset_obj=server_obj.asset, content='移除内存：%s' % ('；'.join(del_slot)))
            # 更新槽位
            log = []
            field_map = {'manufacturer': '制造商', 'model': '型号', 'capacity': '容量', 'sn': '内存SN号', 'speed': '速度'}
            for slot in update_slot:
                slot_data = new_data.get(slot)
                slot_obj = models.Memory.objects.filter(slot=slot, server_obj=server_obj).first()
                for k, v in slot_data.items():
                    if k == 'capacity':
                        v = float(v)
                    value = getattr(slot_obj, k)
                    if v != value:
                        log.append('内存槽位：%s，%s由%s变为%s' % (slot, field_map.get(k), value, v))
                        setattr(slot_obj, k, v)
                slot_obj.save()
            if log:
                models.AssetRecord.objects.create(asset_obj=server_obj.asset, content='；'.join(log))
        else:
            models.ErrorLog.objects.create(title='内存信息采集出错', content=new_data)
