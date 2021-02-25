## OS Linux 发行版介绍

	第 02 天 【OS Linux 发行版介绍】
	
### 回顾：
- 计算机的基础知识：  
	
	+ CPU, Memory, I/O
	+ 程序运行模式：
		+ 用户空间：user space, us
		+ 内核空间：system space
	+ POS：Portable Operating System 
		+ POSIX
		+ API：编程接口
	+ 运行程序格式：
		+ Windwos: EXE，DLL (dynamic link library) 
		+ Linux: ELF, so (shared object)

	+ 程序：指令 + 数据
		+ 指令：只读
		+ 数据：读写
	+ 程序：算法 + 数据结构
	+ 库调用，系统调用：允许被调用的程序。malloc(),free()
	+ 编程语言：
		+ 汇编语言：微码编程，系统中某些与硬件相关的特有代码、驱动程序开发；
		+ 高级语言：c,c++：系统级应用、驱动程序。
		+ 高级语言：java, python, php：应用程序。

### Linux的发行版

#### slackware:
- suse
	+ opensuse：桌面版
	+ sles：服务器版
	 
#### debian:
- ubuntu
 	+ mint

#### redhat:
- rhel: redhat enterprise linux，每 18 个月发行一个新版本。
- CentOS: 兼容 rhel 的格式。
- fedora: 由社区维护，每 6 个月发行一个新版本。

#### ArchLinux:

#### Gentoo:

#### LFS: Linux From scratch

Android: kernel + busybox + java虚拟机

GNU：GPLv2, GPLv3, LGPL (lesser)  

Apache: apache

BSD: BSD

问题1: Centos 和 Linux 是什么关系？ CentOS 和 RHEL 是什么关系？

问题2: 各种开源协议的具体细节？  
GPL, LGPL, Apache, BSD


#### 程序包管理器：
- rpm：
	+ RHEL, Fedora, S.u.S.E, CentOS
- dpt：
	+ Debian, Ubuntu


- 自由软件
	+ 自由使用
	+ 自由学习和修改
	+ 自由分发
	+ 自由创建衍生版

- Linux 的哲学思想
	+ 一切皆文件：把几乎所有的资源，包括硬件设备都组织为文件格式。
	+ 由众多单一目的的小程序组成；一个程序只实现一个功能，而且要做好。组合小程序完成复杂任务。
	+ 尽量避免和用户交互：目标实现脚本编程，以自动完成某些功能。
	+ 使用纯文本文件保存配置信息：目标在于使用一款文本编辑器既能完成系统配置工作。

- 如何获取 CentOS 的发行版
	+ <http://mirrors.aliyun.com>
	+ <http://mirrors.sohu.com>
	+ <http://mirrors.163.com>
	
登录：  
	root/mageedu  
	
	启动 GNOM 桌面：
	# startx &


