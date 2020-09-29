package com.cy.pj.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author psq
 * @Date 2020/8/17/17:10
 */
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    // 页码，默认是第一页
    private int pageNo = 1;

    private int recordNo = 0;

    // 每页显示的记录数，默认是10
    private int pageSize = 10;

    // 总记录数
    private long totalRecord;

    // 总页数
    private long totalPage;

    // 传递参数，只用于前台请求的参数，不能是实体对象
    private Map<String, Object> paramsMap;

    // 对应的当前页记录
    private List<T> results;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(int recordNo) {
        this.recordNo = recordNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
        // 在设置总页数的时候计算出对应的总页数，在下面的三目运算中加法拥有更高的优先级，所以最后可以不加括号。
        long totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize
                : totalRecord / pageSize + 1;
        this.setTotalPage(totalPage);
    }


    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    /**
     * 设置请求参数
     *
     * @param key
     * @param value
     */
    public void putParam(String key, Object value) {
        this.paramsMap.put(key, value);
    }

    /**
     * 获得请求参数
     *
     * @param key
     * @return
     */
    public Object getParam(Object key) {
        return this.paramsMap.get(key);
    }

    /**
     * 只用于页面参数
     *
     * @return
     */
    public Map<String, Object> getParamsMap() {
        return paramsMap;
    }

    /**
     * 只用于页面参数，不能是对象因为要加到url中
     *
     * @param paramsMap
     */
    public void setParamsMap(Map<String, Object> paramsMap) {
        this.paramsMap = paramsMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Page [pageNo=").append(pageNo).append(", pageSize=")
                .append(pageSize).append(", results=").append(results)
                .append(", totalPage=").append(totalPage)
                .append(", totalRecord=").append(totalRecord).append("]");
        return builder.toString();
    }
}