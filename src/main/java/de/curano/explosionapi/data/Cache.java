package de.curano.explosionapi.data;

import java.util.*;

public class Cache {

    private int ttl = 30000;
    private final HashMap<String, Object> cache = new HashMap<>();
    private final HashMap<String, Long> cacheTiming = new HashMap<>();

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

    private synchronized void removeOld() {
        Iterator<Map.Entry<String, Long>> it = cacheTiming.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry<String, Long> entry = (HashMap.Entry<String, Long>) it.next();
            long time = entry.getValue();
            if (time + ttl < System.currentTimeMillis()) {
                cache.remove(entry.getKey());
                it.remove();
            }
        }
    }

    public synchronized void set(String key, Object value) {
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

    public synchronized Object get(String key) {
        if ((System.currentTimeMillis() - cacheTiming.getOrDefault(key, 0L)) < ttl) {
            return cache.get(key);
        } else {
            removeOld();
        }
        return null;
    }

    public synchronized Cache remove(String key) {
        cache.remove(key);
        cacheTiming.remove(key);
        return this;
    }

    public synchronized Cache clear() {
        cache.clear();
        cacheTiming.clear();
        return this;
    }

}
