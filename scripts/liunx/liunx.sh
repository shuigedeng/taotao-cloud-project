#查看僵尸进程
ps -al | gawk '{print $2,$4}' | grep Z


# 匹配电子邮件的地址
cat index.html | egrep -o "[A-Za-z0-9._]+@[A-Za-z0-9.]+\.[a-zA-Z]{2,4}" > ans.txt

#匹配http URL
cat index.html | egrep -o "http://[A-Za-z0-9.]+\.[a-zA-Z]{2,3}" > ans.txt

#纯文本形式下载网页
lynx -dump www.baidu.com > plain.txt

#只打印HTTP头部信息，无须远程下载文件
curl --head www.baidu.com

#使用POST提交数据
curl -d "param2=nickwolfe¶m2=12345" http://www.linuxidc.com/login.cgi

#显示分组途经的网关
traceroute www.baidu.com

#列出系统中的开放端口以及运行在端口上的服务
lsof -i

#nc命令建立socket连接

#设置监听　nc -l 5555
#连接到套接字 nc 192.0.0.1 5555

#快速文件传输
#接收端　nc -l 5555 > destination_filename
#发送端　nc 192.0.0.1 5555 < source_filename

#找出指定目录最大的n个文件
du -ak target_dir | sort -nrk 1 | head -n 4
# du中a为递归,k为kb；sort中n为数字,r为降序,k指定列

#向终端中的所有登陆用户发送广播信息
cat message.txt | wall

#创建新的screen窗口
screen

#打印所有的.txt和.pdf文件
find . \( -name "*.txt" -o -name "*.pdf" \) -print

# -exec command {} \;是连用的，所有符合的都会放置在{}中，去执行command

#将文件分割成多个大小为10kb的文件
split -b 10k data.file

#打印两个文件的交集
comm A.txt B.txt -3 | sed 's/^\t//'

#sed移除空白行
sed '/^$/d' file
