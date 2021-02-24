package com.cy.pj.sys.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:         psq
 * @CreateDate:     2021/2/22 14:21
 */
public class Tools {

    /**
     * 将一个List集合拆分成指定长度的List
     *
     * @param collection 母集合
     * @param len 子list的长度
     * @return resultList 结果List<List>
     */
    public static List<List> getSubList(Collection collection,int len) {
        if (collection == null || collection.size() == 0 || len < 1) {
            return null;
        }
        List<List> resultList = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++) {
            if ( i % len == 0 ) {
                int count = i/len;
                List subList = (List) collection.stream().limit((count + 1) * len).skip(count * len).collect(Collectors.toList());
                resultList.add(subList);
            }
        }
        return resultList;
    }


    public static Map<String, String> toMap(JSONObject jsonObject)
    {
        Map<String, String> result = new HashMap<String, String>();
        Iterator<String> iterator = jsonObject.keySet().iterator();
        String key = null;
        String value = null;
        while (iterator.hasNext())
        {
            key = iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);
        }
        return result;
    }

    /**
     *
     * @param name
     * @param value
     * @param errMap
     */
    public static void saveErrMes(String name,String value,Map<String,List<String>> errMap){
        List list = errMap.get(name);
        if (list == null){
            list = new ArrayList<>();
            errMap.put(name,list);
        }
        list.add(value);
    }

    /**
     * 截取目标字符串 中 开始字符串和结束字符串之间的 字符串
     * @param target   目标字符串
     * @param startStr 开始字符串
     * @param endStr    结束字符串
     * @return
     */
    public static String cutStr(String target,String startStr,String endStr){
        if (target != null) {
            int start = target.indexOf(startStr);
            if (start != -1) {
                int end = target.indexOf(endStr, start + startStr.length());
                if (end != -1) {
                    return target.substring(start + startStr.length(), end);
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public static String cutStr(String target,String endStr){
        if (target != null) {
                int end = target.indexOf(endStr);
                if (end != -1) {
                    return target.substring(0, end);
                }
            return null;
        } else {
            return null;
        }
    }
}
