package com.cy;

import com.alibaba.fastjson.JSON;
import com.cy.pj.entity.Page;
import com.cy.pj.es.model.SearchVo;
import com.cy.pj.es.service.EsSearchServiceImpl;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Map;

/**
 * @Description
 * @Author psq
 * @Date 2020/9/9/11:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MsApplication.class})
@WebAppConfiguration
public class blogTest {

    @Autowired
    private EsSearchServiceImpl esSearchService;

    String[] resultFields = {"id", "contents"};

    @Test
    public void testSearchESContentsData(){
        Map<String, Object> filedContentMap = Maps.newHashMap();
        filedContentMap.put("contents", "hashCode");
        try{
            /**
             * 默认按拼音排序
             */
            Page<SearchVo> searchVoPage = esSearchService.searchContentsESDoc(filedContentMap, 1, 100, resultFields);
            String s = JSON.toJSONString(searchVoPage);
            System.out.println(s);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testESDelById(){
        String esId = "11";
        try{
            boolean b = esSearchService.delEsDataById(esId);
            System.out.println(b);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}