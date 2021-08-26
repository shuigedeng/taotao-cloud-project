-- 限制接口总并发数/请求数
local limit_count = require "resty.limit.count"

-- 这里我们使用AB测试,-n访问10000次, -c并发1200个 
-- ab -n 10000 -c 1200 http://121.42.155.213/ ,第一次测试数据：1000个请求会有差不多8801请求失败，符合以下配置说明
-- 限制 一分钟内只能调用 1200 次 接口（允许在时间段开始的时候一次性放过1200个请求）
local lim, err = limit_count.new("my_limit_count_store", 1200, 60)
if not lim then
    ngx.log(ngx.ERR, "failed to instantiate a resty.limit.count object: ", err)
    return ngx.exit(500)
end

-- use the Authorization header as the limiting key
local key = ngx.req.get_headers()["Authorization"] or "public"
local delay, err = lim:incoming(key, true)

if not delay then
    if err == "rejected" then
        ngx.header["X-RateLimit-Limit"] = "5000"
        ngx.header["X-RateLimit-Remaining"] = 0
        return ngx.exit(503)
    end
    ngx.log(ngx.ERR, "failed to limit count: ", err)
    return ngx.exit(500)
end

-- the 2nd return value holds the current remaining number
-- of requests for the specified key.
local remaining = err

ngx.header["X-RateLimit-Limit"] = "5000"
ngx.header["X-RateLimit-Remaining"] = remaining