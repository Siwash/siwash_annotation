package rpf.study.annotation.cacheUtils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
/**
 * 模拟redis功能
 * */
@Component
public class SimulateCacheUtils
{

    private Map<String,Object> redis;

    public SimulateCacheUtils() {
        redis=new HashMap<>();
    }
    /***
     * 读取缓存
     * */
    public  Object getCache(String key){
        if (isCached(key)){
            Object bean=redis.get(key);
            System.out.println("【redis】读取缓存成功 key="+key+"\t value="+bean);
            return bean;
        }else {
            System.out.println("【redis】--key 不存在");
            return  null;
        }
    }
    /**
     * 写入缓存
     * */
    public boolean writeCache(String key,Object bean){
        if (!isCached(key)){
            redis.put(key,bean);
            System.out.println("【redis】写入缓存成功:key="+key+"\t value="+bean);
            return true;
        }else {
            System.out.println("【redis】--key 已存在");
            return  false;
        }
    }
    /**
     * 判断是否存在缓存
     * */
    public  boolean isCached(String key){
        return redis.containsKey(key);
    }
    /***
     * 修改缓存
     * */
    public boolean setCache(String key,Object bean){
        if (isCached(key)){
            redis.put(key,bean);
            return true;
        }else {
            System.out.println("key 不存在");
            return  false;
        }
    }
}
