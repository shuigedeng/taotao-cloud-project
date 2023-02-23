-- well, we could put the require() and new() calls in our own Lua
-- modules to save overhead. here we put them below just for
-- convenience.

local limit_conn = require "resty.limit.conn"
-- 这里我们使用AB测试,-n访问1000次, -c并发100个 
-- ab -n 1000 -c 100 http://121.42.155.213/ ,这里1000个请求将会有700个失败
-- 相同IP段的人将不能被访问，不影响其它IP 

-- 限制 IP 总请求数
-- 限制单个 ip 客户端最大 200 req/sec 并且允许100 req/sec的突发请求
-- 就是说我们会把200以上300一下的请求请求给延迟， 超过300的请求将会被拒绝
-- 最后一个参数其实是你要预估这些并发（或者说单个请求）要处理多久,可以通过的log_by_lua中的leaving（）调用进行动态调整
local lim, err = limit_conn.new("my_limit_conn_store", 200, 100, 0.5)
if not lim then
    ngx.log(ngx.ERR,
            "failed to instantiate a resty.limit.conn object: ", err)
    return ngx.exit(500)
end

-- the following call must be per-request.
-- here we use the remote (IP) address as the limiting key
-- commit 为true 代表要更新shared dict中key的值，
-- false 代表只是查看当前请求要处理的延时情况和前面还未被处理的请求数
local key = ngx.var.binary_remote_addr
local delay, err = lim:incoming(key, true)
if not delay then
    if err == "rejected" then
        return ngx.exit(503)
    end
    ngx.log(ngx.ERR, "failed to limit req: ", err)
    return ngx.exit(500)
end

if lim:is_committed() then
    local ctx = ngx.ctx
    ctx.limit_conn = lim
    ctx.limit_conn_key = key
    ctx.limit_conn_delay = delay
end

-- the 2nd return value holds the current concurrency level
-- for the specified key.
local conn = err

if delay >= 0.001 then
    -- 其实这里的 delay 肯定是上面说的并发处理时间的整数倍，
    -- 举个例子，每秒处理100并发，桶容量200个，当时同时来500个并发，则200个拒掉
    -- 100个在被处理，然后200个进入桶中暂存，被暂存的这200个连接中，0-100个连接其实应该延后0.5秒处理，
    -- 101-200个则应该延后0.5*2=1秒处理（0.5是上面预估的并发处理时间）
    -- the request exceeding the 200 connections ratio but below
    -- 300 connections, so
    -- we intentionally delay it here a bit to conform to the
    -- 200 connection limit.
    -- ngx.log(ngx.WARN, "delaying")
    ngx.sleep(delay)
end
