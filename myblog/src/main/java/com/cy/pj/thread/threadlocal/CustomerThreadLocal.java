package com.cy.pj.thread.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.cy.pj.dao.enums.Constants;
import com.cy.pj.dao.enums.LocalName;

import java.util.HashMap;
import java.util.Map;

/**
* @Description:    线程副本
* @Author:         psq
* @CreateDate:     2021/2/22 16:15
* @Version:        1.0
*/
public class CustomerThreadLocal {
    private static final ThreadLocal<Map<LocalName,Object>> customThreadLocal = new TransmittableThreadLocal<Map<LocalName,Object>>(){
        @Override
        public Map<LocalName,Object> initialValue(){
            return new HashMap<>(4);
        }

        @Override
        protected Map<LocalName, Object> childValue(Map<LocalName, Object> parentValue) {
            if (parentValue != null){
                return new HashMap<>(parentValue);
            }
            return new HashMap<>(4);
        }

        @Override
        protected Map<LocalName, Object> copy(Map<LocalName, Object> parentValue) {
            if (parentValue != null){
                return new HashMap<>(parentValue);
            }
            return new HashMap<>(4);
        }
    };

    public static Object getInfo(LocalName name){
        Map<LocalName, Object> map = customThreadLocal.get();
        if (map == null){
            return null;
        }
        return map.get(name);
    }

    /**
     * 获取副本线程中的对象
     * @return
     */
    public static Map<LocalName, Object> getThreadLocal(){
        return customThreadLocal.get();
    }

    /**
     * 移除该线程副本
     */
    public static void removeThreadLocal(){
        customThreadLocal.remove();
    }

    /**
     * 设置 值
     * @param name
     * @param object
     */
    public static void setInfo(LocalName name,Object object){
        if (LocalName.CUSTOMER_NO.equals(name) && !Constants.DEFAULT_CUSTOMER_NO.equals(object)){
            // 若切换客户标识，则先清除副本信息
            customThreadLocal.remove();
            Map<LocalName, Object> map = new HashMap<>();
            map.put(LocalName.CUSTOMER_NO,object);
            customThreadLocal.set(map);
        }else {
            Map<LocalName, Object> map = customThreadLocal.get();
            if (map == null){
                return;
            }
            map.put(name,object);
        }
    }

    public  static void remove(){
        customThreadLocal.remove();
    }

    public  static Object removeInfo(String name){
        Map<LocalName, Object> map = customThreadLocal.get();
        if (map == null){
            return null;
        }
        return map.remove(name);
    }

    public  static boolean removeInfo(String name,Object value){
        Map<LocalName, Object> map = customThreadLocal.get();
        if (map == null){
            return true;
        }
        return map.remove(name,value);
    }

    /**
     * 获取副本中 客户标识
     * @return
     */
    public static String getCustomerNo(){
        return (String) getInfo(LocalName.CUSTOMER_NO);
    }

    /**
     * 获取副本中 主从数据库标识
     * @return
     */
    public static Integer getDataSourceType(){
        return (Integer) getInfo(LocalName.DATA_SOURCE_TYPE);
    }

    /**
     * 设置副本中 主从数据库标识 master,slave
     * @return
     */
    public static void setDataSourceType(int type){
        setInfo(LocalName.DATA_SOURCE_TYPE,type);
    }

    /**
     * 移除 主从库设置
     * @return
     */
    public static void removeDataSourceType(){
        Map<LocalName, Object> map = customThreadLocal.get();
        if (map == null){
            return;
        }
        map.remove(LocalName.DATA_SOURCE_TYPE);
    }

    /**
     * 设置 客户标识
     * @param customerNo
     */
    public static void setCustomerNo(String customerNo){
        setInfo(LocalName.CUSTOMER_NO,customerNo);
    }

    /**
     * 设置默认标识
     * 需配合{@link CustomerThreadLocal#revertCustomerNo()} 方法使用
     */
    public static void setDefaultCustomerNo(){
        Map<LocalName, Object> map = customThreadLocal.get();
        if (map == null){
            return;
        }
        Object o = map.get(LocalName.CUSTOMER_NO);
        if (o != null && !Constants.DEFAULT_CUSTOMER_NO.equals(o)){
            map.put(LocalName.TEMP_CUSTOMER_NO,o);
        }
        setInfo(LocalName.CUSTOMER_NO,Constants.DEFAULT_CUSTOMER_NO);
    }

    /**
     * 移除默认标识
     * 并返回原有客户标识
     */
    public static void revertCustomerNo(){
        Map<LocalName, Object> map = customThreadLocal.get();
        if (map == null){
            return;
        }
        Object o = map.remove(LocalName.TEMP_CUSTOMER_NO);
        if (o != null){
            map.put(LocalName.CUSTOMER_NO,o);
        }
    }


}
