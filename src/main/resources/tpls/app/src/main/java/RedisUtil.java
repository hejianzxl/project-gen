package ${groupId}.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author luiz
 * @Title: RedisUtil
 * @ProjectName ${artifactId}-springboot
 * @Description: TODO
 * @date 2018/11/13 17:19
 */
@Component
public class RedisUtil {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private String namespace = "tpl:";

    public void set(String k, Object v, long time) {
        String key = namespace + k;
        if (v instanceof String && stringRedisTemplate != null) {
            stringRedisTemplate.opsForValue().set(key, (String) v);
        } else {
            redisTemplate.opsForValue().set(key, v);
        }
        if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }
    public void set(String k, Object v) {
        set(k, v, -1);
    }

    public void setNx(String k, Object v, long time) {
        String key = namespace + k;
        if (v instanceof String && stringRedisTemplate != null) {
            stringRedisTemplate.opsForValue().setIfAbsent(key, (String) v);
        } else {
            redisTemplate.opsForValue().setIfAbsent(key, v);
        }
        if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    public void setNx(String k, Object v) {
        setNx(k, v, -1);
    }

    public boolean contains(String key) {
        return redisTemplate.hasKey(namespace + key);
    }

    public String get(String k) {
        if (stringRedisTemplate != null) {
            return stringRedisTemplate.opsForValue().get(namespace + k);
        } else {
            return (String) redisTemplate.opsForValue().get(namespace + k);
        }
    }

    public <T> T getObject(String k) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        return (T) valueOps.get(namespace + k);
    }

    public void remove(String key) {
        redisTemplate.delete(namespace + key);
    }

    public long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(namespace + pattern);
    }

    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(namespace + key, delta);
    }

    public Double increment(String key, double delta) {
        return redisTemplate.opsForValue().increment(namespace + key, delta);
    }


}
