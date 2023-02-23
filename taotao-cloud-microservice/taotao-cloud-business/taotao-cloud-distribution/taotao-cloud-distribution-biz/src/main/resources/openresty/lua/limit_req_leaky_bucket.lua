local limit_req = require "resty.limit.req"

-- 漏桶算法
-- 这里我们使用AB测试,-n访问1000次, -c并发100个 
-- ab -n 1000 -c 100 http://121.42.155.213/
-- 限制 ip 每秒只能调用 200 次 接口  ，超过部分进入桶中等待，（桶容量为100），如果桶也满了，则进行限流
local lim, err = limit_req.new("my_limit_req_store", 200, 100)
if not lim then
    ngx.log(ngx.ERR,
            "failed to instantiate a resty.limit.req object: ", err)
    return ngx.exit(500)
end

-- the following call must be per-request.
-- here we use the remote (IP) address as the limiting key
local key = ngx.var.binary_remote_addr
local delay, err = lim:incoming(key, true)
if not delay then
    if err == "rejected" then
        return ngx.exit(503)
    end
    ngx.log(ngx.ERR, "failed to limit req: ", err)
    return ngx.exit(500)
end

-- 基于桶的容量，让其排队等待，然后依次处理
if delay >= 0.001 then
    -- the 2nd return value holds  the number of excess requests
    -- per second for the specified key. for example, number 31
    -- means the current request rate is at 231 req/sec for the
    -- specified key.
    local excess = err

    -- the request exceeding the 200 req/sec but below 300 req/sec,
    -- so we intentionally delay it here a bit to conform to the
    -- 200 req/sec rate.
    ngx.sleep(delay)
end