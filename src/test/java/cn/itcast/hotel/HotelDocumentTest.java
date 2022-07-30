package cn.itcast.hotel;

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * @Author LIXUBO
 * @Date 2022-07-29 21:36
 * @description
 * @Version 1.0
 */
@SpringBootTest
public class HotelDocumentTest {

    private RestHighLevelClient client;
    @Autowired
    private IHotelService hotelService;

    @Test
    void testInit() {
        System.out.println(client);
    }

    @Test
    void testBulkRequest() throws IOException {

        // 批量查询酒店数据
        List<Hotel> hotels = hotelService.list();
        //转化为文档类型
        //1.创建request
        BulkRequest request = new BulkRequest();
        for (Hotel hotel : hotels) {
            HotelDoc hotelDoc = new HotelDoc(hotel);
            //2.准备参数，添加多个Request
            request.add(
                    new IndexRequest("hotel")
                            .id(hotelDoc.getId().toString())
                            .source( JSON.toJSONString(hotelDoc), XContentType.JSON));
        }
        //3.发送请求
        client.bulk(request, RequestOptions.DEFAULT);

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
