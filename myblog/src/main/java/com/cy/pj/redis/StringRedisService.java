package com.cy.pj.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cy.pj.entity.Contents;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class StringRedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> vOps;

    private String redisKey = "blog";

    /**
     * 设置值
     * @param key
     * @param value
     */
    public void set(String key, Object value){
        try{
            vOps.set(StringUtils.join(redisKey,key), value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设置值
     * @param key
     * @param value
     * @param secondTime 有效时间，单位秒
     */
    public void set(String key, Object value, long secondTime){
        try{
            vOps.set(StringUtils.join(redisKey,key), value, secondTime, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设置值
     * @param key
     * @param value
     * @param secondTime 有效时间，单位秒
     */
    public void setValue(String key, Object value, long secondTime) throws Exception{
        vOps.set(StringUtils.join(redisKey,key), value, secondTime, TimeUnit.SECONDS);
    }

    //获取值
    public Object get(String key){
        Object res = null;
        try{
            res = vOps.get(StringUtils.join(redisKey,key));
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //获取 String
    public String getString(String key){
        String res = null;
        try{
            Object value = this.get(key);
            if(null != value){
                res = value.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //获取 Double
    public Double getDouble(String key){
        Double res = null;
        try{
            Object value =this.get(key);
            if(null != value){
                res = (Double)value;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //获取 Map
    public Map<String,Object> getMap(String key){
        Map<String,Object> res = null;
        try{
            Object value = this.get(key);
            if(null != value){
                res = (Map<String,Object>) value;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //获取 List
    public List<Map<String,Object>> getList(String key){
        List<Map<String,Object>> res = null;
        try{
            Object value = this.get(key);
            if(null != value){
                res = (List<Map<String,Object>>) value;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //获取 收藏List
    public List<Contents> getCollectList(String key){
        List<Contents> res = null;
        try{
            Object value = this.get(key);
            if(null != value){
                res = (List<Contents>) value;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //获取 JSONObject
    public JSONObject getJSONObject(String key){
        JSONObject res = null;
        try{
            Object value = this.get(key);
            if(null != value){
                res = JSON.parseObject(JSON.toJSONString(value));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //获取 JSONObject
    public JSONArray getJSONArray(String key){
        JSONArray res = null;
        try{
            Object value = this.get(key);
            if(null != value){
                res = JSON.parseArray(JSON.toJSONString(value));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 删除key
     * @param key
     */
    public void delete(String key){
        try{
            redisTemplate.delete(StringUtils.join(redisKey,key));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 删除key
     * @param customerNo
     * @param key
     */
    public void delete(String customerNo,String key){
        try{
            redisTemplate.delete(StringUtils.join(customerNo,key));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 删除key以某个字符串开头的所有至
     * @param pattern
     */
    public void deleteKeysByPrefix(String pattern){
        try{
            Set keys = redisTemplate.keys(StringUtils.join(redisKey,pattern) +"*");
            redisTemplate.delete(keys);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void lock(String key){
        key = StringUtils.join(redisKey,"lock_",key);
        Boolean setnx = vOps.setIfAbsent(key, 1, 3, TimeUnit.SECONDS);
        while (!setnx){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            setnx = vOps.setIfAbsent(key, 1, 3, TimeUnit.SECONDS);
        }
    }

    public void delLock(String key){
        key = StringUtils.join(redisKey,"lock_",key);
        try{
            redisTemplate.delete(key);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 获取锁之后需删除
     * @param key
     */
    public void lockNew(String key){
        key = StringUtils.join(redisKey,key);
        //默认失效时间3秒
        Boolean setnx = vOps.setIfAbsent( key, 1,3, TimeUnit.SECONDS);
        while (!setnx){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            setnx =vOps.setIfAbsent( key, 1,3, TimeUnit.SECONDS);
        }
    }
    public boolean setnx(String key,Object value,long secondTime){
        Boolean flag = vOps.setIfAbsent(StringUtils.join(redisKey,key), value,secondTime, TimeUnit.SECONDS);
        return flag;
    }

    public Set getKeys(String key){
        return redisTemplate.keys(StringUtils.join(redisKey,key));
    }

    /**
     * 判断缓存中是否有对应得value
     * @param key
     * @return
     */
    public boolean exists(final String key){
        return redisTemplate.hasKey(StringUtils.join(redisKey,key));
    }

    /**
     * 获取无序队列
     * @param key
     * @return
     */
    public Set getOperationsSet(String key){
        key = redisKey + key;
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.difference(key, new HashSet());
    }

    /**
     * 获取长度内的并移除
     * @param key
     * @param size
     * @return
     */
    public List getAndRemoveSet(String key,long size){
        key = redisKey + key;
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.pop(key,size);
    }

    /**
     * 获取Set 长度
     * @param key
     * @return
     */
    public Long getOperationsSetSize(String key){
        key = redisKey + key;
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.size(key);
    }

    /**
     * 删除无序队列中的值
     * @param key
     * @param obj
     * @return
     */
    public Long deleteOperationsSet(String key,Object... obj){
        key = redisKey + key;
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.remove(key, obj);
    }

    /**
     * 添加值至无序队列
     * @param key
     * @param obj
     */
    public void addOperationsSet(String key,Object... obj){
        key = redisKey + key;
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add(key,obj);
    }

    /**
     * hash表设置 键值对
     * @param mapKey hash 表的名称
     * @param key
     * @param value
     */
    public void hashPut(String mapKey,String key,Object value){
        mapKey = redisKey + mapKey;
        HashOperations hash = redisTemplate.opsForHash();
        hash.put(mapKey,key,value);
    }

    /**
     * 如果不存在，则向redis hash几何 中存放一个元素
     * @param mapKey
     * @param key
     * @param value
     */
    public void hashPutIfAbsent(String mapKey,String key,Object value){
        mapKey = redisKey + mapKey;
        HashOperations hash = redisTemplate.opsForHash();
        hash.putIfAbsent(mapKey,key,value);
    }

    /**
     * hash表 删除键
     * @param mapKey
     * @param key
     * @return
     */
    public Long hashDelete(String mapKey,Object... key){
        mapKey = redisKey + mapKey;
        HashOperations hash = redisTemplate.opsForHash();
        return hash.delete(mapKey,key);
    }

    /**
     * hash表 获取键值对
     * @param mapKey
     * @param key
     * @return
     */
    public Object  hashGet(String mapKey,String key){
        mapKey = redisKey + mapKey;
        HashOperations hash = redisTemplate.opsForHash();
        return  hash.get(mapKey,key);
    }

    /**
     * hash表 获取多个键值对
     * @param mapKey
     * @param keys
     * @return keys长度的list 顺序和keys 中 顺序 一一对应
     */
    public List  hashMultiGet(String mapKey, List<String> keys){
        mapKey = redisKey + mapKey;
        HashOperations hash = redisTemplate.opsForHash();
        return  hash.multiGet(mapKey,keys);
    }

    /**
     * hash表 保存多个键值对
     * @param mapKey
     * @param map
     */
    public void   hashMultiPut(String mapKey, Map<String,Object> map){
        mapKey = redisKey + mapKey;
        HashOperations hash = redisTemplate.opsForHash();
        hash.putAll(mapKey,map);
    }

    /**
     * hash表  获取表长度
     * @param mapKey
     * @return
     */
    public Long  hashSize(String mapKey){
        mapKey = redisKey + mapKey;
        HashOperations hash = redisTemplate.opsForHash();
        return  hash.size(mapKey);
    }


    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment 。
     * @param mapKey
     * @param key
     * @param increment
     */
    public Long hashIncrement(String mapKey,String key,Long increment){
        mapKey = redisKey + mapKey;
        HashOperations hash = redisTemplate.opsForHash();
        return hash.increment(mapKey, key, increment);
    }

    /**
     * 为哈希表 key 中的指定字段的浮点值加上增量 increment 。
     * @param mapKey
     * @param key
     * @param increment
     * @return
     */
    public Double hashIncrement(String mapKey,String key,Double increment){
        mapKey = redisKey + mapKey;
        HashOperations hash = redisTemplate.opsForHash();
        return hash.increment(mapKey, key, increment);
    }

    /**
     * hash表 获取该表所有元素
     * 谨慎调用，调用之前 可使用 {@link #hashSize(String mapKey)} 方法查询该hash 表长度
     * @param mapKey
     * @return
     */
    public Map<String,Object> hashEntries(String mapKey){
        mapKey = redisKey + mapKey;
        HashOperations hash = redisTemplate.opsForHash();
        return  hash.entries(mapKey);
    }

    /**
     *  hash表 获取该表所有key
     * @param mapKey
     * @return
     */
    public Set<String> hashKeys(String mapKey){
        mapKey = redisKey + mapKey;
        HashOperations hash = redisTemplate.opsForHash();
        return hash.keys(mapKey);
    }

    /**
     * 给key设置过期时间
     * @param key
     * @param timeOut  秒数
     * @return
     */
    public Boolean setExpire(String key,long timeOut){
        key = redisKey + key;
        return redisTemplate.expire(key,timeOut,TimeUnit.SECONDS);
    }

}