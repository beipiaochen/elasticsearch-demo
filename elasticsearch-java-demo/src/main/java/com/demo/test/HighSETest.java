package com.demo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HighSETest {
    private String indexName = "personindex";
    private String id = "1";
    private RestHighLevelClient client;


    @Before
    public void before(){
        client = new RestHighLevelClient(
                RestClient.builder(
//                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9200, "http")));

    }

    @Test
    public void test() throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        IndexRequest request = new IndexRequest(indexName);
        request.id("1");
        map.put("username","xiao chen");
        map.put("age","6");
        map.put("desc","learn elasticsearch now");
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(map);
        request.source(str,XContentType.JSON);

//        XContentBuilder builder = XContentFactory.jsonBuilder();
//        builder.startObject();
//        {
//            builder.field("user", "kimchy");
//            builder.timeField("postDate", new Date());
//            builder.field("message", "trying out Elasticsearch");
//        }
//        builder.endObject();
//        IndexRequest indexRequest = new IndexRequest("posts")
//                .id("1").source(builder);

//        IndexRequest indexRequest = new IndexRequest("posts")
//                .id("1")
//                .source("user", "kimchy",
//                        "postDate", new Date(),
//                        "message", "trying out Elasticsearch");


//        request.versionType(VersionType.EXTERNAL);
        request.opType(DocWriteRequest.OpType.CREATE);
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse);

    }


    @After
    public void after() throws IOException {
        client.close();
    }
}
