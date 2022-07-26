-- 可能回滚的列表，一个记录要回滚的skuid一个记录库存
local skuid_list= {}
local stock_list= {}

local arg_list = ARGV;
local function cut ( key , num )
    KEYS[1] = key;
    local value = redis.call("get",KEYS[1])

    if not value then
        value = 0;
    end

    value=value+num
    if(value<0)
    then
        -- 发生超卖
        return false;
    end
    redis.call("set",KEYS[1],value)
    return true
end

local function rollback ( )
    for i,k in ipairs (skuid_list) do
        -- 还原库存
        KEYS[1] = k;
        redis.call("incrby",KEYS[1],0-stock_list[i])
    end
end

local function doExec()
    for i, k in ipairs (arg_list)
    do
        local num = tonumber(k)
        local key=  KEYS[i]
        local result = cut(key,num)

        -- 发生超卖，需要回滚
        if (result == false)
        then
            rollback()
            return false
        else
            -- 记录可能要回滚的数据
            table.insert(skuid_list,key)
            table.insert(stock_list,num)
        end

    end
    return true;
end

return doExec()