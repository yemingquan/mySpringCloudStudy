package com.example.springBootDemo.config.system.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @所属模块<p>
 * @描述<p>
 * @创建人 xiaoYe
 * @创建时间 2021/2/20 16:06
 * @Copyright (c) 2020 inc. all rights reserved<p>
 * @公司名称
 */
@Component
public class CacheManager<T> {
    private Map<String, T> cache =new ConcurrentHashMap<>();

    public T get(String key){
        return  cache.get(key);
    }

    public void addOrUpdateCache(String key,T value) {
        cache.put(key, value);
    }

    // 依据 key 来删除缓存中的一条记录
    public void evictCache(String key) {
        if(cache.containsKey(key)) {
            cache.remove(key);
        }
    }

    // 清空缓存中的全部记录
    public void evictCache() {
        cache.clear();
    }
}
