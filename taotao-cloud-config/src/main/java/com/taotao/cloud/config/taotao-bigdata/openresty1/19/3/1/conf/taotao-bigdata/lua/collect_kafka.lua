local producer = require("resty.kafka.producer")
local client = require("resty.kafka.client")
local cjson = require("cjson.safe")

local kafka_servers = ngx.var.KAFKA_SERVERS

local broker_list = {
    { host = '172.16.32.4', port = 9092 },
}

local error_handle = function(topic, partition_id, queue, index, error, retryable)
    ngx.log(ngx.ERR, 'failed sentd to kafka' .. error)
end

local producer_config = {
    request_timeout = 60000,
    socket_timeout = 60000,
    producer_type = 'async',
    flush_time = 1000,
    batch_num = 5,
    max_buffering = 10000,
    error_handle = error_handle
}

local meta_data = {}
if ngx.var.http_x_forwarded_for == nil then
    meta_data["ip"] = ngx.var.remote_addr
else
    meta_data["ip"] = ngx.var.http_x_forwarded_for
end

local args = ngx.req.get_uri_args()
local data = args["data"]
if data == nil then
    ngx.say('{"code:": 500, "msg": "data error"}')
end

--local escape_data = ngx.var.arg_data
--local data = ngx.unescape_uri(escape_data)

local data_json_str = ngx.decode_base64(data)
local data_json = cjson.decode(data_json_str)

local properties_json = data_json["properties"]
local project = properties_json["project"]
meta_data["project"] = project

local current_time = ngx.now() * 1000
meta_data["ctime"] = current_time

local meta = cjson.encode(meta_data)
local msg = ngx.encode_base64(meta) .. "-" .. data

local bp = producer:new(broker_list, producer_config)

local offset, err = bp:send(project, nil, msg)
if not offset then
    ngx.log(ngx.ERR, 'kafka send error:', err)
    ngx.say('{"code:": 500, "msg": failed"}')
end
ngx.say('{"code:": 200, "msg": "success"}')
