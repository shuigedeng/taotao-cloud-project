wget https://github.com/shadowsocks/shadowsocks/archive/refs/tags/2.9.1.tar.gz

tar -zxvf 2.9.1.tar.gz -C /opt

cd shadowsocks

python setup.py install

vim /etc/shadowsock.json
{
    "server":"0.0.0.0",
    "server_port":8091,
    "local_address": "127.0.0.1",
    "local_port":1080,
    "password":"123456",
    "timeout":300,
    "method":"aes-256-cfb",
    "fast_open": false
}
