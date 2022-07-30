package cn.itcast.hotel;

import cn.itcast.hotel.pojo.HotelDoc;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author LIXUBO
 * @Date 2022-07-30 14:36
 * @description
 * @Version 1.0
 */
public class HotelISearchTest {
    private RestHighLevelClient client;

    @Test
    void testMatchAll1() throws IOException {
        //1. 准备request
        SearchRequest request = new SearchRequest("hotel");
        //2. 准备DSL
        request.source().query(QueryBuilders.matchAllQuery());
        //3. 发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        System.out.println("结果：" + response);

    }

    @Test
    void testMatchAll() throws IOException {
        //1. 准备request
        SearchRequest request = new SearchRequest("hotel");
        //2. 准备DSL
        request.source().query(QueryBuilders.matchAllQuery());
        //3. 发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //4. 解析响应结果
        SearchHits searchHits = response.getHits();
        //4.1 查询总条数.
        long total = searchHits.getTotalHits().value;
        System.out.println("共查询到 " + total + " 条结果");
        //4.2 查询遍历结果数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits
        ) {
            //4.3 得到source
            String json = hit.getSourceAsString();
            //反序列化
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            //4.4 打印
            System.out.println("HotelDoc = " + hotelDoc);
        }
        System.out.println("结果：" + response);
    }

    @BeforeEach
    void setUp() {
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://127.0.0.1:9200")
        ));
    }

    @AfterEach
    void tearDown() throws Exception {
        this.client.close();
    }
}
