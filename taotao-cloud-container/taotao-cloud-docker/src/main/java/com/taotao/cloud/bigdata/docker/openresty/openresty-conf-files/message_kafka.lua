-- require需要resty.kafka.producer的lua脚本，没有会报错
local producer = require("resty.kafka.producer")
local cjson = require("cjson.safe")

-- kafka的集群信息，单机也是可以的

local kafka_servers = ngx.var.KAFKA_SERVERS

local broker_list = {}

local servers = {}
while true do
    local pos = string.find(kafka_servers, ",")
    if not pos then
        table.insert(servers, kafka_servers)
        break
    end
    local sub_str = string.sub(kafka_servers, 1, pos - 1)
    table.insert(servers, sub_str)
    kafka_servers = string.sub(kafka_servers, pos + 1, string.len(kafka_servers))
end

for i, v in pairs(servers) do
    local server = {}
    while true do
        local p = string.find(v, ":")
        if not p then
            table.insert(server, v)
            break
        end
        local sb= string.sub(v, 1, p - 1)
        table.insert(server,sb)
        v = string.sub(v, p + 1, string.len(v))
    end
    local obj = {}
    obj["host"] = server[1]
    obj["port"] = server[2]
    table.insert(broker_list, obj)
end

ngx.log(ngx.ERR, 'ngx.var.KAFKA_SERVERS-----', broker_list[1]["host"])

local uri_args = ngx.req.get_uri_args()

local data

for key, val in pairs(uri_args) do
    if (key == "data") then
        data = val
        break
    end
end

if data == nil then
    ngx.say([[{"code:": 500, "msg": "params error"}]])
end

local current_time = ngx.now() * 1000
local project = ngx.var.arg_project
local meta_data = {}

meta_data["ctime"] = current_time
meta_data["project"] = project

if ngx.var.http_x_forwarded_for == nil then
    meta_data["ip"] = ngx.var.remote_addr
else
    meta_data["ip"] = ngx.var.http_x_forwarded_for
end
local meta = cjson.encode(meta_data)

local msg = ngx.encode_base64(meta) .. "-" .. data
-- 定义kafka同步生产者，也可设置为异步 async
-- 注意！！！当设置为异步时，在测试环境需要修改batch_num,默认是200条，若大不到200条kafka端接受不到消息
-- encode()将log_json日志转换为字符串
-- 发送日志消息,send配套之第一个参数topic:
-- 发送日志消息,send配套之第二个参数key,用于kafka路由控制:
-- key为nill(空)时，一段时间向同一partition写入数据
-- 指定key，按照key的hash写入到对应的partition

-- -- batch_num修改为1方便测试
local bp = producer:new(broker_list, { producer_type = "async", batch_num = 2 })
--local bp = producer:new(broker_list)

local ok, err = bp:send("access", nil, msg)
if not ok then
    ngx.log(ngx.ERR, 'kafka send err:', err)
elseif ok then
    ngx.log(ngx.ERR, 'kafka send success:', ok)
    ngx.say([[{"code:": 200, "msg": "success"}]])
else
    ngx.say("未知错误")
end
