package com.cy.pj.es.service;

import com.alibaba.fastjson.JSON;
import com.cy.pj.entity.Page;
import com.cy.pj.es.model.SearchVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mzlion.core.io.IOUtils;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Description
 * @Author psq
 * @Date 2020/8/17/15:35
 */
@Component
public class EsSearchServiceImpl{

    @Autowired
    private RestHighLevelClient client;

    private String index =  "blog_index";
    private String type =  "custom";

    public void init() throws Exception {
        this.createIndex(index);
    }

    /**
     * 测试不通
     * 创建索引
     * @param index
     * @throws IOException
     */
    public void createIndexOld(String index) throws IOException {
        //如果存在就不创建了
        if(this.existsIndex(index)) {
            System.out.println(index+"索引库已经存在!");
            return;
        }
        // 开始创建库
        CreateIndexRequest request = new CreateIndexRequest(index);
        //配置文件
        ClassPathResource seResource = new ClassPathResource("mapper/es/setting.json");
        InputStream seInputStream = seResource.getInputStream();
        String seJson = String.join("\n",IOUtils.readLines(seInputStream, Charset.forName("UTF-8")));
        seInputStream.close();
        //映射文件
        ClassPathResource mpResource = new ClassPathResource("mapper/es/"+index+".json");
        InputStream mpInputStream = mpResource.getInputStream();
        String mpJson = String.join("\n", IOUtils.readLines(mpInputStream,Charset.forName("UTF-8")));
        mpInputStream.close();

        request.settings(seJson, XContentType.JSON);
        request.mapping(mpJson, XContentType.JSON);

        //设置别名
        request.alias(new Alias(index+"_alias"));
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        boolean falg = createIndexResponse.isAcknowledged();
        if(falg){
            System.out.println("创建索引库:"+index+"成功！" );
        }
    }

    /**
     * 测试通过
     * 创建索引
     * @param index
     * @throws IOException
     */
    public void createIndex(String index) throws IOException {
        //如果存在就不创建了
        if(this.existsIndex(index)) {
            System.out.println(index+"索引库已经存在!");
            return;
        }
        // 开始创建库
        org.elasticsearch.action.admin.indices.create.CreateIndexRequest request = new org.elasticsearch.action.admin.indices.create.CreateIndexRequest(index);
        //封装属性 类似于json格式,映射属性
        Map<String, Object> jsonMap = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        Map<String, Object> content1 = new HashMap<>();
        content.put("type", "text");
        content .put("fielddata", "true");
        if(index.contains("blog_index")){
            properties.put("pinyin", content);
            content1.put("type", "text");
            content1 .put("fielddata", "true");
            properties.put("updateTime", content1);
        }else{
            properties.put("search_time", content);
        }
        jsonMap.put("properties", properties);
        request.mapping(type,jsonMap);
        org.elasticsearch.action.admin.indices.create.CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        boolean falg = createIndexResponse.isAcknowledged();
        if(falg){
            System.out.println("创建索引库:"+index+"成功！" );
        }
    }

    /**
     * 测试通过
     * 判断索引是否存在
     * @param index
     * @return
     * @throws IOException
     */
    public boolean existsIndex(String index) throws IOException {
        org.elasticsearch.client.indices.GetIndexRequest getIndexRequest = new org.elasticsearch.client.indices.GetIndexRequest(index);
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        return exists;
    }

    /**
     * 删除索引
     * @param indexName
     * @return
     */
    public boolean deleteIndex(String indexName) {
        DeleteIndexRequest index = new DeleteIndexRequest(indexName);
        try {
            client.indices().delete(index,RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  测试通过
     * @param filedContentMap   查询属性与其对应的值
     * @param pageNo            页码
     * @param size              每页的条数
     * @param resultFields      返回的结果需包含哪些字段
     * @return
     * @throws Exception
     */
    public Page<SearchVo> searchContentsESDoc(Map<String, Object> filedContentMap, int pageNo, int size, String[] resultFields) throws Exception{
        if(!existsIndex(index)){
            createIndex(index);
        }
        Page<SearchVo> basePage = new Page<>();
        int form = (pageNo-1) * size;
        basePage.setRecordNo(form);
        basePage.setPageNo(pageNo);
        basePage.setPageSize(size);

        Map map = searchESContentsData(filedContentMap, form, size, resultFields, 1);
        SearchHit[] hits = (SearchHit[])map.get("hits");
        Long total = (Long)map.get("total");

        List<SearchVo> esDocsList = Lists.newArrayList();
        for (SearchHit hit : hits) {
            SearchVo searchVo = JSON.parseObject(hit.getSourceAsString(), SearchVo.class).setFromNo(form++);
            esDocsList.add(searchVo);
        }

        long totalPage = total/size;
        if (total%size != 0){
            totalPage++;
        }
        basePage.setTotalPage(totalPage);
        basePage.setResults(esDocsList);
        basePage.setTotalRecord(total);
        return basePage;
    }


    public Map<String,Object> searchESContentsData(Map<String, Object> filedContentMap,int form, int size, String[] resultFields,int sort) throws  Exception{
        Map<String,Object> map = Maps.newHashMap();
        SearchRequest request = new SearchRequest().types(index);
        //构建搜寻器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        sourceBuilder.fetchSource(resultFields,new String[] {});

        for (Map.Entry<String, Object> key : filedContentMap.entrySet()) {
            Object o = key.getValue();
            if (o != null){
                if (o instanceof String){
                    if (StringUtils.isBlank(o.toString())){
                        continue;
                    }
                    qb.must(QueryBuilders.matchQuery(key.getKey(), o));
                }else if (o instanceof  Collection<?>){
                    Set sets = (Set) o;
                    qb.must(QueryBuilders.termsQuery(key.getKey(), sets));
                }else {
                    qb.must(QueryBuilders.termQuery(key.getKey(), o));
                }
            }
        }
        if (sort == 1){
            //排序规则 按照拼音排序
            sourceBuilder.sort(SortBuilders.fieldSort("pinyin").order(SortOrder.ASC));
        }else if (sort == 2){
            // 先按照id排序，再按照拼音排序
            sourceBuilder.sort(SortBuilders.fieldSort("id").order(SortOrder.ASC));
            sourceBuilder.sort(SortBuilders.fieldSort("pinyin").order(SortOrder.ASC));
        }

        //参数
        sourceBuilder.query(qb);
        //分页
        sourceBuilder.from(form);
        sourceBuilder.size(size);
        //分页
        request.source(sourceBuilder);
        SearchResponse search = client.search(request,RequestOptions.DEFAULT);
        if(null != search){
            SearchHits searchHits = search.getHits();
            SearchHit[] hits = searchHits.getHits();
            TotalHits totalHits = searchHits.getTotalHits();
            long value = totalHits.value;

            if (null != hits) {
                map.put("hits",hits);
                map.put("total",value);
            }
        }
        return map;
    }


    /**
     * 测试通过
     * @param id
     * @return
     */
    public SearchVo selectContentById(String id) throws IOException{
        if(!existsIndex(index)){
            createIndex(index);
        }
        SearchVo searchVo = new SearchVo();
        Map<String, Object> filed = Maps.newHashMap();
        filed.put("id",id);
        SearchHit[] goods_indices = dealData(index, filed);
        if(goods_indices.length > 0){
            searchVo = JSON.parseObject(goods_indices[0].getSourceAsString(), SearchVo.class);
        }
        return searchVo;
    }

    /**
     * 测试通过
     * 保存或更新数据
     * @return
     * @throws IOException
     */
    public boolean saveOrUpdate(SearchVo searchVo) throws IOException {
        boolean flag = true;
        try {
            //添加之前判断是否存在index
            if(!existsIndex(index)) {
                createIndex(index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //当前数据对应的索引id
        Map<String, Object> fileds = Maps.newHashMap();
        fileds.put("id",searchVo.getId());
        String id = selectContentsInfoByIds(fileds);
        //先通过查询去判断es中数据是否存在，如果存在就更新，反之就新增
        Gson gson = new Gson();
        Map map = gson.fromJson(JSON.toJSONString(searchVo), Map.class);
        if(StringUtil.isNotEmpty(id)){
            //修改数据
            UpdateRequest updateRequest = new UpdateRequest(index, type, id);
            updateRequest.doc(map);
            try {
                client.update(updateRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            }
        }else{
            IndexRequest indexRequest = new IndexRequest(index, type);
            indexRequest.source(map);
            try {
                client.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return  flag;
    }

    public  Set<String> seg(String text, SegmentationAlgorithm segmentationAlgorithm) {
        Set<String> result = new HashSet<>();
        for(Word word : WordSegmenter.segWithStopWords(text, segmentationAlgorithm)){
            result.add(word.getText());
        }
        return result;
    }

    /**
     * 测试通过
     * 根据id删除文档
     * @param id
     * @return
     * @throws IOException
     */
    public Boolean delEsDataById(String id){
        Map<String, Object> fileds = Maps.newHashMap();
        fileds.put("id",id);
        Boolean flag = false;
        if(null == fileds || fileds.isEmpty()){
            return flag;
        }
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);

        // 更新时版本冲突
        request.setConflicts("proceed");
        // 设置查询条件，第一个参数是字段名，第二个参数是字段的值
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, Object> entry : fileds.entrySet()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
        }
        request.setQuery(boolQueryBuilder);
        // 更新最大文档数
        request.setSize(10);
        // 批次大小
        request.setBatchSize(1000);
        // 并行
        request.setSlices(2);
        // 使用滚动参数来控制“搜索上下文”存活的时间
        request.setScroll(TimeValue.timeValueMinutes(10));
        // 超时
        request.setTimeout(TimeValue.timeValueMinutes(2));
        // 刷新索引
        request.setRefresh(true);
        try {
            BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);
            response.getStatus().getUpdated();
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  flag;
    }

    /**
     * 查询ES ID并删除重复
     * @param fileds
     * @return
     */
    public String selectContentsInfoByIds(Map<String,Object> fileds){
        String result = "";
        SearchHit[] searchHits = dealData(index, fileds);
        if(null != searchHits && searchHits.length >0) {
            if (searchHits.length >1){
                // 删除重复数据 保留一个
                for (int i = 1; i < searchHits.length; i++) {
                    DeleteRequest deleteRequest = new DeleteRequest(index,type, searchHits[i].getId());
                    try {
                        client.delete(deleteRequest,RequestOptions.DEFAULT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            result = searchHits[0].getId();
        }
        return  result;
    }

    public SearchHit[] dealData(String index,Map<String, Object> fileds){
        SearchHit[] result = null;
        if(null != fileds && !fileds.isEmpty()){
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            for (Map.Entry<String, Object> entry : fileds.entrySet()) {
                //循环进行判断赋值
                boolQuery.must(QueryBuilders.termQuery(entry.getKey(),entry.getValue()));
            }
            sourceBuilder.query(boolQuery);
            SearchRequest rq = new SearchRequest();
            //索引
            rq.indices(index);
            //各种组合条件
            rq.source(sourceBuilder);
            SearchResponse search = null;
            try {
                search = client.search(rq,RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(null != search){
                SearchHits hits = search.getHits();
                result = hits.getHits();
            }
        }
        return result;
    }

}