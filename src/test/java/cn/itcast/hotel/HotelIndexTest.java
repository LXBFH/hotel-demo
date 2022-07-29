package cn.itcast.hotel;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static cn.itcast.hotel.constants.HotelContants.MAPPING_TEMPLATE;

/**
 * @Author LIXUBO
 * @Date 2022-07-29 19:16
 * @description
 * @Version 1.0
 */
public class HotelIndexTest {
    private RestHighLevelClient client;

    @Test
    void testInit(){
        System.out.println(client);
    }

    @Test
    void creatHotelIndex() throws IOException {
        // 1. 创建request对象
        CreateIndexRequest request = new CreateIndexRequest("hotel");
        // 2. 准备请求的参数 ， DSL语句
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        // 3. 发送请求
        client.indices().create(request,RequestOptions.DEFAULT);
    }
    @BeforeEach
    void setUp(){
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://127.0.0.1:9200")
        ));
    }

    @AfterEach
    void tearDown() throws Exception{
        this.client.close();
    }
}
