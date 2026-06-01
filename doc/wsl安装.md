#wsl安装

### 默认安装位置
C:\Users\你的用户名\AppData\Local\Packages\CanonicalGroupLimited.Ubuntu...\LocalState\ext4.vhdx

### 手动安装
wsl --version
wsl --update
下载 Ubuntu .wsl 文件

版本                          下载地址
Ubuntu 26.04 LTS（推荐）        https://releases.ubuntu.com/26.04/ubuntu-26.04-wsl-amd64.wsl
Ubuntu 24.04 LTS                https://releases.ubuntu.com/24.04/ubuntu-24.04-wsl-amd64.wsl
Daily                           构建版https://cdimage.ubuntu.com/ubuntu-wsl/daily-live/current/

国内用户可用阿里云镜像加速：
https://mirrors.aliyun.com/ubuntu-cdimage/ubuntu-wsl/

# 安装到 D 盘指定目录
wsl --install --from-file D:\WSL\images\ubuntu-26.04-wsl-amd64.wsl --location D:\WSL\Ubuntu-26.04

# 首次配置
# 启动
wsl -d Ubuntu-26.04

# 首次进入会提示创建用户名和密码
# 如果没有提示，手动创建：
adduser myuser
usermod -aG sudo myuser

# 设置为默认用户
cat > /etc/wsl.conf <<'EOF'
[user]
default=myuser

[boot]
systemd=true
EOF

# 退出重启
exit
wsl -t Ubuntu-26.04
wsl -d Ubuntu-26.04

# 设置为默认发行版
wsl --set-default Ubuntu-26.04
