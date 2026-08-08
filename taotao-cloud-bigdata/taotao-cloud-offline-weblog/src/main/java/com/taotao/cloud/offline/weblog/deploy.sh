dnf config-manager --add-repo  https://openresty.org/package/centos/openresty.repo
dnf install openresty
cd /usr/local/openresty/

# nginx-lua-kafka
wget https://github.com/doujiang24/lua-resty-kafka/archive/master.zip
unzip master.zip && cd lua-resty-kafka-master/lib/resty/ \
&& cp -r kafka /usr/local/openresty/lualib/resty

# nginx-lua-prometheus
https://github.com/knyar/nginx-lua-prometheus/releases
wget https://github.com/knyar/nginx-lua-prometheus/archive/0.20201118.tar.gz
tar xf 0.20201118.tar.gz && cd nginx-lua-prometheus-0.20201118 \
cp prometheus.lua /usr/local/openresty/lualib/resty/prometheus

├── bin
│   └── openresty -> /usr/local/openresty/nginx/sbin/nginx
├── COPYRIGHT
├── luajit
│   ├── bin
│   │   ├── luajit -> luajit-2.1.0-beta3
│   │   └── luajit-2.1.0-beta3
│   ├── include
│   │   └── luajit-2.1
│   │       ├── lauxlib.h
│   │       ├── luaconf.h
│   │       ├── lua.h
│   │       ├── lua.hpp
│   │       ├── luajit.h
│   │       └── lualib.h
│   ├── lib
│   │   ├── libluajit-5.1.so -> libluajit-5.1.so.2.1.0
│   │   ├── libluajit-5.1.so.2 -> libluajit-5.1.so.2.1.0
│   │   ├── libluajit-5.1.so.2.1.0
│   │   ├── lua
│   │   │   └── 5.1
│   │   └── pkgconfig
│   │       └── luajit.pc
│   └── share
│       ├── lua
│       │   └── 5.1
│       └── luajit-2.1.0-beta3
│           └── jit
│               ├── bc.lua
│               ├── bcsave.lua
│               ├── dis_arm64be.lua
│               ├── dis_arm64.lua
│               ├── dis_arm.lua
│               ├── dis_mips64el.lua
│               ├── dis_mips64.lua
│               ├── dis_mipsel.lua
│               ├── dis_mips.lua
│               ├── dis_ppc.lua
│               ├── dis_x64.lua
│               ├── dis_x86.lua
│               ├── dump.lua
│               ├── p.lua
│               ├── v.lua
│               ├── vmdef.lua
│               └── zone.lua
├── lualib
│   ├── cjson.so
│   ├── librestysignal.so
│   ├── ngx
│   │   ├── balancer.lua
│   │   ├── base64.lua
│   │   ├── errlog.lua
│   │   ├── ocsp.lua
│   │   ├── pipe.lua
│   │   ├── process.lua
│   │   ├── re.lua
│   │   ├── req.lua
│   │   ├── resp.lua
│   │   ├── semaphore.lua
│   │   ├── ssl
│   │   │   └── session.lua
│   │   └── ssl.lua
│   ├── redis
│   │   └── parser.so
│   ├── resty
│   │   ├── aes.lua
│   │   ├── core
│   │   │   ├── base64.lua
│   │   │   ├── base.lua
│   │   │   ├── ctx.lua
│   │   │   ├── exit.lua
│   │   │   ├── hash.lua
│   │   │   ├── misc.lua
│   │   │   ├── ndk.lua
│   │   │   ├── phase.lua
│   │   │   ├── regex.lua
│   │   │   ├── request.lua
│   │   │   ├── response.lua
│   │   │   ├── shdict.lua
│   │   │   ├── socket.lua
│   │   │   ├── time.lua
│   │   │   ├── uri.lua
│   │   │   ├── utils.lua
│   │   │   ├── var.lua
│   │   │   └── worker.lua
│   │   ├── core.lua
│   │   ├── dns
│   │   │   └── resolver.lua
│   │   ├── prometheus  #********************** prometheus文件位置 ***************************
│   │   │   ├── prometheus.lua
│   │   ├── kafka  #********************** kafka文件位置 ***************************
│   │   │   ├── broker.lua
│   │   │   ├── client.lua
│   │   │   ├── errors.lua
│   │   │   ├── producer.lua
│   │   │   ├── request.lua
│   │   │   ├── response.lua
│   │   │   ├── ringbuffer.lua
│   │   │   └── sendbuffer.lua
│   │   ├── limit
│   │   │   ├── conn.lua
│   │   │   ├── count.lua
│   │   │   ├── req.lua
│   │   │   └── traffic.lua
│   │   ├── lock.lua
│   │   ├── lrucache
│   │   │   └── pureffi.lua
│   │   ├── lrucache.lua
│   │   ├── md5.lua
│   │   ├── memcached.lua
│   │   ├── mysql.lua
│   │   ├── random.lua
│   │   ├── redis.lua
│   │   ├── sha1.lua
│   │   ├── sha224.lua
│   │   ├── sha256.lua
│   │   ├── sha384.lua
│   │   ├── sha512.lua
│   │   ├── sha.lua
│   │   ├── shell.lua
│   │   ├── signal.lua
│   │   ├── string.lua
│   │   ├── upload.lua
│   │   ├── upstream
│   │   │   └── healthcheck.lua
│   │   └── websocket
│   │       ├── client.lua
│   │       ├── protocol.lua
│   │       └── server.lua
│   └── tablepool.lua
├── nginx
│   ├── conf
│   │   ├── fastcgi.conf
│   │   ├── fastcgi.conf.default
│   │   ├── fastcgi_params
│   │   ├── fastcgi_params.default
│   │   ├── koi-utf
│   │   ├── koi-win
│   │   ├── mime.types
│   │   ├── mime.types.default
│   │   ├── nginx.conf
│   │   ├── nginx.conf.default
│   │   ├── scgi_params
│   │   ├── scgi_params.default
│   │   ├── uwsgi_params
│   │   ├── uwsgi_params.default
│   │   └── win-utf
│   ├── html
│   │   ├── 50x.html
│   │   └── index.html
│   ├── logs
│   └── sbin
│       └── nginx
├── openssl111
│   ├── bin
│   │   └── openssl
│   └── lib
│       ├── engines-1.1
│       │   ├── afalg.so
│       │   ├── capi.so
│       │   └── padlock.so
│       ├── libcrypto.so -> libcrypto.so.1.1
│       ├── libcrypto.so.1.1
│       ├── libssl.so -> libssl.so.1.1
│       └── libssl.so.1.1
├── pcre
│   └── lib
│       ├── libpcre.so -> libpcre.so.1.2.12
│       ├── libpcre.so.1 -> libpcre.so.1.2.12
│       └── libpcre.so.1.2.12
├── site
│   └── lualib
└── zlib
    └── lib
        ├── libz.so -> libz.so.1.2.11
        ├── libz.so.1 -> libz.so.1.2.11
        └── libz.so.1.2.11
