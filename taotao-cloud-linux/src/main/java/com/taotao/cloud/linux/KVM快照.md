# KVM 快照

查看磁盘格式：
```
    [root@Testserver ~]# cd /var/lib/libvirt/images/
    [root@Testserver images]# qemu-img info node2_centos6.img 
    image: node2_centos6.img
    file format: raw
    virtual size: 20G (21474836480 bytes)
    disk size: 1.7G
```

关闭 node2 虚拟机：
```
    [root@Testserver images]# virsh list
     Id    Name                           State
    ----------------------------------------------------
     3     node2_centos6                  running
     5     node1_centos6                  running
     12    node3_centos7                  running
     15    node4_centos7                  running

    [root@Testserver images]# virsh shutdown node2_centos6
    Domain node2_centos6 is being shutdown

    [root@Testserver images]# virsh domstate node2_centos6
    shut off

    [root@Testserver images]# virsh list --all 
     Id    Name                           State
    ----------------------------------------------------
     5     node1_centos6                  running
     12    node3_centos7                  running
     15    node4_centos7                  running
     -     node2_centos6                  shut off
```

转换磁盘格式：
```
    [root@Testserver images]# qemu-img convert -f raw -O qcow2 node2_centos6.img node2_centos6.qcow2

    -f：源镜像格式
    -O：目标镜像格式

    旧格式(raw)的磁盘映像文件还在，新建了一份相同大小的新格式(qcow2)的磁盘映像文件：
    [root@Testserver images]# ll
    -rw------- 1 root root  21474836480 May  5 16:58 node2_centos6.img
    -rw-r--r-- 1 root root   1457192960 May  5 18:17 node2_centos6.qcow2

    [root@Testserver images]# qemu-img info node2_centos6.img
    image: node2_centos6.img
    file format: raw
    virtual size: 20G (21474836480 bytes)
    disk size: 1.7G
    [root@Testserver images]# qemu-img info node2_centos6.qcow2
    image: node2_centos6.qcow2
    file format: qcow2
    virtual size: 20G (21474836480 bytes)
    disk size: 1.4G
    cluster_size: 65536
```

修改虚拟机配置文件，用以使用新的磁盘映像文件：
```
    [root@Testserver images]# virsh edit node2_centos6
    <driver name='qemu' type='raw' cache='none'/>
    <source file='/var/lib/libvirt/images/node2_centos6.img'/>
    改为：
    <driver name='qemu' type='qcow2' cache='none'/>
    <source file='/var/lib/libvirt/images/node2_centos6.qcow2'/>    

```

## 对虚拟机进行快照管理

创建磁盘快照：
```
    [root@Testserver images]# virsh start node2_centos6
    Domain node2_centos6 started

    [root@Testserver images]# virsh domstate node2_centos6
    running

    [root@Testserver images]# virsh snapshot-create node2_centos6
    Domain snapshot 1525516124 created
```

查看虚拟机镜像快照的版本：
```
    [root@Testserver ~]# virsh snapshot-list node2_centos6
     Name                 Creation Time             State
    ------------------------------------------------------------
     1525516124           2018-05-05 18:28:44 +0800 running

    查看当前使用的快照版本：
    [root@Testserver ~]# virsh snapshot-current node2_centos6 
    <domainsnapshot>
      <name>1525516124</name>
      <state>running</state>
      <creationTime>1525516124</creationTime>
      <memory snapshot='internal'/>
      <disks>
        <disk name='vda' snapshot='internal'/>
        <disk name='hdc' snapshot='no'/>
      </disks>
      <domain type='kvm'>
        <name>node2_centos6</name>
```

再创建一个磁盘快照：
```
    [root@Testserver images]# virsh snapshot-create node2_centos6
    Domain snapshot 1525516779 created

    查看磁盘信息，会显示有两个快照信息：
    [root@Testserver images]# qemu-img info node2_centos6.qcow2 
    image: node2_centos6.qcow2
    file format: qcow2
    virtual size: 20G (21474836480 bytes)
    disk size: 2.0G
    cluster_size: 65536
    Snapshot list:
    ID        TAG                 VM SIZE                DATE       VM CLOCK
    1         1525516124             303M 2018-05-05 18:28:44   00:00:53.886
    2         1525516779             303M 2018-05-05 18:39:39   00:10:44.840
```

快照配置文件：
```
    快照配置文件默认保存在 /var/lib/libvirt/qemu/snapshot/虚拟机名称下：
    [root@Testserver images]# ll /var/lib/libvirt/qemu/snapshot/node2_centos6/
    total 8
    -rw------- 1 root root 3561 May  5 18:39 1525516124.xml
    -rw------- 1 root root 3612 May  5 18:40 1525516779.xml
```

恢复磁盘快照：
```
    创建一个文件：
    [root@node2 ~]# echo 'Hello World!' > snapshot_test.text 
    [root@node2 ~]# cat snapshot_test.text
    Hello World!

    [root@node2 ~]# ll snapshot_test.text
    -rw-r--r--. 1 root root   13 May  5 18:52 snapshot_test.text

    恢复磁盘快照：恢复到 1525516779
    [root@Testserver images]# virsh snapshot-list node2_centos6
     Name                 Creation Time             State
    ------------------------------------------------------------
     1525516124           2018-05-05 18:28:44 +0800 running
     1525516779           2018-05-05 18:39:39 +0800 running

    [root@Testserver images]# virsh snapshot-revert node2_centos6 1525516779

    再登录上去查看创建的文件，已经不存在了：
    [root@node2 ~]# ll snapshot_test.text
    ls: cannot access snapshot_test.text: No such file or directory
```

删除快照：
```
    当前使用的快照：
    [root@Testserver ~]# virsh snapshot-current node2_centos6
    <domainsnapshot>
      <name>1525516779</name>
      <state>running</state>
      <parent>
        <name>1525516124</name>
      </parent>
      <creationTime>1525516779</creationTime>
      <memory snapshot='internal'/>
      <disks>
        <disk name='vda' snapshot='internal'/>
        <disk name='hdc' snapshot='no'/>
      </disks>
      <domain type='kvm'>
        <name>node2_centos6</name>

    创建一个文件：
    [root@node2 ~]# echo 'Hello World!' > snapshot_test.text 
    [root@node2 ~]# cat snapshot_test.text
    Hello World!

    [root@node2 ~]# ll snapshot_test.text 
    -rw-r--r--. 1 root root 13 May  5 18:41 snapshot_test.text

    删除快照：
    [root@Testserver images]# virsh snapshot-delete node2_centos6 1525516779 
    Domain snapshot 1525516779 deleted

    查看文件，文件还在：
    [root@node2 ~]# ll snapshot_test.text 
    -rw-r--r--. 1 root root 13 May  5 18:41 snapshot_test.text

    查看当前使用的镜像，默认切换为 1525516124：
    [root@Testserver ~]# virsh snapshot-current node2_centos6
    <domainsnapshot>
      <name>1525516124</name>
      <state>running</state>
      <creationTime>1525516124</creationTime>
      <memory snapshot='internal'/>
      <disks>
        <disk name='vda' snapshot='internal'/>
        <disk name='hdc' snapshot='no'/>
      </disks>
      <domain type='kvm'>
        <name>node2_centos6</name>
```

Reference：<http://blog.51cto.com/lxshopping/1652472>