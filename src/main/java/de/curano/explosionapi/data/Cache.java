package de.curano.explosionapi.data;

import java.util.HashMap;

public class Cache {

    private int ttl = 30000;
    private HashMap<String, Object> cache = new HashMap<>();
    private HashMap<String, Long> cacheTiming = new HashMap<>();

    public Cache() {

    }

    // In milliseconds
    public Cache(int ttl) {
        this.ttl = ttl;
    }

    public Cache setTTL(int ttl) {
        this.ttl = ttl;
        return this;
    }

    public int getTTL() {
        return ttl;
    }

    private void removeOld() {
        for (String key : cacheTiming.keySet()) {
            long time = cacheTiming.get(key);
            if (time + ttl < System.currentTimeMillis()) {
                cache.remove(key);
                cacheTiming.remove(key);
            }
        }
    }

    public void set(String key, Object value) {
        if (cache.containsKey(key)) {
            cache.replace(key, value);
            if (cacheTiming.containsKey(key)) {
                cacheTiming.replace(key, System.currentTimeMillis());
            } else {
                cacheTiming.put(key, System.currentTimeMillis());
            }
        } else {
            cache.put(key, value);
            if (cacheTiming.containsKey(key)) {
                cacheTiming.replace(key, System.currentTimeMillis());
            } else {
                cacheTiming.put(key, System.currentTimeMillis());
            }
        }
    }

    public Object get(String key) {
        if ((System.currentTimeMillis() - cacheTiming.getOrDefault(key, 0L)) < ttl) {
            return cache.get(key);
        } else {
            removeOld();
        }
        return null;
    }

    public Cache remove(String key) {
        cache.remove(key);
        cacheTiming.remove(key);
        return this;
    }

    public Cache clear() {
        cache.clear();
        cacheTiming.clear();
        return this;
    }

}
