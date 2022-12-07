#!/bin/bash
#用途：根据网络配置对网络地址192.168.0进行修改，检查是否是活动状态

#{start..end}shell扩展生成一组地址
for ip in 192.168.0.{1..255}
do
    (
    ping $ip -c 2 &> /dev/null
    # > 标准输出重定向，和1>一致
    # 2>&1 将标准错误输出　重定向　到标准输出
    # &>file 将标准输出和标准错误输出都重定向到文件filename中

    if [ $? -eq 0 ];then
        echo $ip is alive
    fi
    )&
done
wait
#并行ping,加速
