package com.demo.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.internal.JsonContext;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.ml.PostDataRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Test {
    public static void main(String[] args) throws Exception {
        /** 生成json格式 */
        /**转化Json方法1 */
        Map<String, String> map = new HashMap<String, String>();
        map.put("username","xiaochen");
        map.put("age","6");
        map.put("desc","learn elasticsearch now");
        ObjectMapper mapper = new ObjectMapper();
        byte[] str = mapper.writeValueAsBytes(map);
        System.out.println(new String(str));

        /**转化Json方法2 */
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        xContentBuilder.startObject().field("username","xiaochen")
                .field("age","6")
                .field("desc","learn elasticsearch now")
                .endObject();
        String s = Strings.toString(xContentBuilder);
        System.out.println(s);
        /** 索引文件 */


    }
}
