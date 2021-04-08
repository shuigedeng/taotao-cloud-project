#_*_coding:utf-8_*_
import os
def file_handle(filename,backend_data,record_list=None,type='fetch'): #type:fetch append change
    new_file=filename+'_new'
    bak_file=filename+'_bak'
    if type == 'fetch':
        r_list = []
        with open(filename, 'r') as f:
            tag = False
            for line in f:
                if line.strip() == backend_data:
                    tag = True
                    continue
                if tag and line.startswith('backend'):
                    break
                if tag and line:
                    r_list.append(line.strip())
            for line in r_list:
                print(line)
            return r_list
    elif type == 'append':
        with open(filename, 'r') as read_file, \
                open(new_file, 'w') as write_file:
            for r_line in read_file:
                write_file.write(r_line)

            for new_line in record_list:
                if new_line.startswith('backend'):
                    write_file.write(new_line + '\n')
                else:
                    write_file.write("%s%s\n" % (' ' * 8, new_line))
        os.rename(filename, bak_file)
        os.rename(new_file, filename)
        os.remove(bak_file)
    elif type == 'change':
        with open(filename, 'r') as read_file, \
                open(new_file, 'w') as write_file:
            tag=False
            has_write=False
            for r_line in read_file:
                if r_line.strip() == backend_data:
                    tag=True
                    continue
                if tag and r_line.startswith('backend'):
                    tag=False
                if not tag:
                    write_file.write(r_line)
                else:
                    if not has_write:
                        for new_line in record_list:
                            if new_line.startswith('backend'):
                                write_file.write(new_line+'\n')
                            else:
                                write_file.write('%s%s\n' %(' '*8,new_line))
                        has_write=True
        os.rename(filename, bak_file)
        os.rename(new_file, filename)
        os.remove(bak_file)

def fetch(data):
    backend_data="backend %s" %data
    return file_handle('haproxy.conf',backend_data,type='fetch')
def add(data):
    backend=data['backend']
    record_list=fetch(backend)
    current_record="server %s %s weight %s maxconn %s" %(data['record']['server'],\
                                                         data['record']['server'],\
                                                         data['record']['weight'],\
                                                         data['record']['maxconn'])
    backend_data="backend %s" %backend

    if not record_list:
        record_list.append(backend_data)
        record_list.append(current_record)
        file_handle('haproxy.conf',backend_data,record_list,type='append')
    else:
        record_list.insert(0,backend_data)
        if current_record not in record_list:
            record_list.append(current_record)
        file_handle('haproxy.conf',backend_data,record_list,type='change')
def remove(data):
    backend=data['backend']
    record_list=fetch(backend)
    current_record="server %s %s weight %s maxconn %s" %(data['record']['server'],\
                                                         data['record']['server'],\
                                                         data['record']['weight'],\
                                                         data['record']['maxconn'])
    backend_data = "backend %s" % backend
    if not record_list or current_record not in record_list:
        print('\033[33;1m无此条记录\033[0m')
        return
    else:
        #处理record_list
        record_list.insert(0,backend_data)
        record_list.remove(current_record)
        file_handle('haproxy.conf',backend_data,record_list,type='change')
def change(data):
    backend=data[0]['backend']
    record_list=fetch(backend)

    old_record="server %s %s weight %s maxconn %s" %(data[0]['record']['server'],\
                                                         data[0]['record']['server'],\
                                                         data[0]['record']['weight'],\
                                                         data[0]['record']['maxconn'])

    new_record = "server %s %s weight %s maxconn %s" % (data[1]['record']['server'], \
                                                        data[1]['record']['server'], \
                                                        data[1]['record']['weight'], \
                                                        data[1]['record']['maxconn'])
    backend_data="backend %s" %backend

    if not record_list or old_record not in record_list:
        print('\033[33;1m无此内容\033[0m')
        return
    else:
        record_list.insert(0,backend_data)
        index=record_list.index(old_record)
        record_list[index]=new_record
        file_handle('haproxy.conf',backend_data,record_list,type='change')
def qita(data):
    pass


if __name__ == '__main__':
    msg='''
    1：查询
    2：添加
    3：删除
    4：修改
    5：退出
    6:其他操作
    '''
    menu_dic={
        '1':fetch,
        '2':add,
        '3':remove,
        '4':change,
        '5':exit,
        '6':qita,
    }
    while True:
        print(msg)
        choice=input("操作>>: ").strip()
        if len(choice) == 0 or choice not in menu_dic:continue
        if choice == '5':break

        data=input("数据>>: ").strip()

        #menu_dic[choice](data)==fetch(data)
        if choice != '1':
            data=eval(data)
        menu_dic[choice](data) #add(data)




# [{'backend':'www.oldboy20.org','record':{'server':'2.2.2.3','weight':20,'maxconn':3000}},{'backend':'www.oldboy10.org','record':{'server':'10.10.0.10','weight':9999,'maxconn':33333333333}}]