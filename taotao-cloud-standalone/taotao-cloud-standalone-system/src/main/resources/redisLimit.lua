--Lua脚本

--- 限流KEY资源唯一标识
local key = "rate.limit:" .. KEYS[1]
--- 时间窗最大并发数
local limit = tonumber(ARGV[1])
--- 时间窗内当前并发数
local current = tonumber(redis.call('get', key) or "0")
--如果超出限流大小
if current + 1 > limit then
  return 0
else  --请求数+1，并设置2秒过期
  redis.call("INCRBY", key,"1")
   redis.call("expire", key,"2")
   return current + 1
end



--IP限流Lua脚本

--local key = "rate.limit:" .. KEYS[1]
--local limit = tonumber(ARGV[1])
--local expire_time = ARGV[2]
--
--local is_exists = redis.call("EXISTS", key)
--if is_exists == 1 then
--    if redis.call("INCR", key) > limit then
--        return 0
--    else
--        return 1
--    end
--else
--    redis.call("SET", key, 1)
--    redis.call("EXPIRE", key, expire_time)
--    return 1
--end
