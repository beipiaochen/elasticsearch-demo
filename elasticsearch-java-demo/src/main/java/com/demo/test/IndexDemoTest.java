package com.demo.test;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class IndexDemoTest {

//    ObjectMapper objectMapper = new ObjectMapper();
    private String index = "project2index";
    private String type="project2type";
    private RestHighLevelClient client;

    @Before
    public void before(){
        HttpHost httpHost = new HttpHost("localhost", 9200, "http");
        RestClientBuilder builder = RestClient.builder(httpHost);
        client = new RestHighLevelClient(builder);
    }


    @Test
    public void createIndex() throws IOException, InterruptedException {

        CreateIndexRequest request = new CreateIndexRequest(index);
        /** 设置属性 */
        request.settings(Settings.builder()
                /** 默认 5分片1副本 */
                .put("index.number_of_shards", 5)
                .put("index.number_of_replicas", 1)
        );
        /** 设置mapping 方式1 */
//        request.mapping(
//                "{\n" +
//                        "  \"properties\": {\n" +
//                        "    \"message\": {\n" +
//                        "      \"type\": \"text\"\n" +
//                        "    }\n" +
//                        "  }\n" +
//                        "}",
//                XContentType.JSON);
        /** 设置mapping 方式2 */
//        Map<String, Object> message = new HashMap<>();
//        message.put("type", "text");
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("message", message);
//        Map<String, Object> mapping = new HashMap<>();
//        mapping.put("properties", properties);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String s = objectMapper.writeValueAsString(mapping);
//        request.mapping(mapping);
        /** 设置mapping 方式3 */
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("name");
                {
                    builder.field("type", "text");
                }
                builder.endObject();
                builder.startObject("desc");
                {
                    builder.field("type", "text");
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        request.mapping(String.valueOf(builder));
        /** 设置mapping 方式4 整体设置 */
//        request.source("{\n" +
//                "    \"settings\" : {\n" +
//                "        \"number_of_shards\" : 1,\n" +
//                "        \"number_of_replicas\" : 0\n" +
//                "    },\n" +
//                "    \"mappings\" : {\n" +
//                "        \"properties\" : {\n" +
//                "            \"message\" : { \"type\" : \"text\" }\n" +
//                "        }\n" +
//                "    },\n" +
//                "    \"aliases\" : {\n" +
//                "        \"twitter_alias\" : {}\n" +
//                "    }\n" +
//                "}", XContentType.JSON);
        /** 同步处理 */
        IndicesClient indices = client.indices();
//        CreateIndexResponse createIndexResponse = indices.create(request, RequestOptions.DEFAULT);
//        System.out.println(createIndexResponse);

        ActionListener<CreateIndexResponse> listener =
                new ActionListener<CreateIndexResponse>() {

                    @Override
                    public void onResponse(CreateIndexResponse createIndexResponse) {
                        System.out.println("节点确认请求 "+createIndexResponse.isAcknowledged());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("异常返回信息 "+e.getMessage());

                    }
                };
        /**异步创建index*/
        client.indices().createAsync(request, RequestOptions.DEFAULT, listener);
        Thread.sleep(2000);
        
    }

    @Test
    public void delete() throws IOException, InterruptedException {
        index = "projectindex";
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        /** 可选项 */
        /** 等待所有节点超时时间 */
//        request.timeout(TimeValue.timeValueMinutes(2));
//        request.timeout("2m");
        /** 等待主节点超时时间 */
//        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
//        request.masterNodeTimeout("1m");
        /** LENIENT_EXPAND_OPEN */
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        /** 同步删除index */
        try{
            AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
        }catch (ElasticsearchException e){
            if (e.status() == RestStatus.NOT_FOUND) {
                System.out.println("NOT_FOUND！！");
            }else if(e.status() == RestStatus.NO_CONTENT){
                System.out.println("NO_CONTENT！！");
            }
        }
//
//        System.out.println("确认删除： "+deleteIndexResponse.isAcknowledged());
        /** 异步进行删除 */
        ActionListener<AcknowledgedResponse> listener =
                new ActionListener<AcknowledgedResponse>() {
                    @Override
                    public void onResponse(AcknowledgedResponse deleteIndexResponse) {
                        System.out.println("删除正常 "+deleteIndexResponse.isAcknowledged());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("异常信息 "+e.getMessage());
                    }
                };
        client.indices().deleteAsync(request, RequestOptions.DEFAULT, listener);
        Thread.sleep(2000);
    }

    /**
     * 判断 index 是否存在
     */
    @Test
    public void getIndex() throws IOException, InterruptedException {
        index = "userindex";
        GetIndexRequest request = new GetIndexRequest(index);
        /** 可选配置 */
        /** 本地/节点 */
//        request.local(false);
        /** 可读 */
//        request.humanReadable(true);
        /** 是否包括默认配置 */
//        request.includeDefaults(false);
        /** 设置通配符表达式 */
//        request.indicesOptions(indicesOptions);
        /** 同步 */
//        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
//        System.err.println("index "+index+" 是否存在: "+(exists == true?"存在":"不存在"));
        /** 异步 */
        ActionListener<Boolean> listener = new ActionListener<Boolean>() {
            @Override
            public void onResponse(Boolean exists) {
                System.out.println("index "+index+" 是否存在: "+(exists == true?"存在":"不存在"));
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("异常信息 "+e.getMessage());
            }
        };
        client.indices().existsAsync(request, RequestOptions.DEFAULT, listener);
        Thread.sleep(2000);
    }

    /**
     * 在 kibana 的 索引管理 中 有个状态项 （open）
     * @throws IOException
     */
    @Test
    public void openIndex() throws IOException, InterruptedException {
        index = "userindex";
        OpenIndexRequest request = new OpenIndexRequest(index);
        /** 同步打开 */
        OpenIndexResponse openIndexResponse = client.indices().open(request, RequestOptions.DEFAULT);
        /** 异步打开 */
//        ActionListener<OpenIndexResponse> listener =
//                new ActionListener<OpenIndexResponse>() {
//                    @Override
//                    public void onResponse(OpenIndexResponse openIndexResponse) {
//                        System.out.println(openIndexResponse.isAcknowledged());
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                };
//        client.indices().openAsync(request, RequestOptions.DEFAULT, listener);
//        Thread.sleep(2000);
    }

    /**
     * 在 kibana 的 索引管理 中 有个状态项 (关闭)
     * @throws IOException
     */
    @Test
    public void closeIndex() throws IOException, InterruptedException {
        index = "userindex";
        CloseIndexRequest request = new CloseIndexRequest(index);
        /** 设置请求超时时间 */
//        request.timeout(TimeValue.timeValueMinutes(2));
//        request.timeout("2m");

        /** 同步 */
        AcknowledgedResponse closeIndexResponse = client.indices().close(request, RequestOptions.DEFAULT);
        System.err.println(closeIndexResponse.isAcknowledged());
        /** 异步 */
//        ActionListener<AcknowledgedResponse> listener =
//                new ActionListener<AcknowledgedResponse>() {
//                    @Override
//                    public void onResponse(AcknowledgedResponse closeIndexResponse) {
//                        System.out.println(closeIndexResponse.isAcknowledged());
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                };
//        client.indices().closeAsync(request, RequestOptions.DEFAULT, listener);
//        Thread.sleep(2000);

    }

    /**
     * 添加/修改 mapping
     *
     * @throws IOException
     */
    @Test
    public void putMapping() throws IOException, InterruptedException {
        index = "personindex";
        PutMappingRequest request = new PutMappingRequest(index);
        /** 方式1 */
        request.source(
                "{\n" +
                        "  \"properties\": {\n" +
                        "    \"message\": {\n" +
                        "      \"type\": \"text\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                XContentType.JSON);
        /** 方式2 */
//        Map<String, Object> jsonMap = new HashMap<>();
//        Map<String, Object> message = new HashMap<>();
//        message.put("type", "text");
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("message", message);
//        jsonMap.put("properties", properties);
//        request.source(jsonMap);
        /** 方式3 */
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("id");
                {
                    builder.field("type", "integer");
                }
                builder.endObject();
                builder.startObject("name");
                {
                    builder.field("type", "text");
                }
                builder.endObject();
                builder.startObject("age");
                {
                    builder.field("type", "integer");
                }
                builder.endObject();
                builder.startObject("desc");
                {
                    builder.field("type", "text");
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        request.source(builder);
        /** 同步 */
        AcknowledgedResponse putMappingResponse = client.indices().putMapping(request, RequestOptions.DEFAULT);
        System.err.println(putMappingResponse.isAcknowledged());
        /** 异步 */
//        ActionListener<AcknowledgedResponse> listener =
//                new ActionListener<AcknowledgedResponse>() {
//                    @Override
//                    public void onResponse(AcknowledgedResponse putMappingResponse) {
//                        System.out.println(putMappingResponse.isAcknowledged());
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//                        System.err.println(e.getMessage());
//                    }
//                };
//        client.indices().putMappingAsync(request, RequestOptions.DEFAULT, listener);
//        Thread.sleep(2000);
    }
    @Test
    public void getMapping() throws IOException {
        index = "personindex";
        GetMappingsRequest request = new GetMappingsRequest();
        /** 方法1 提前指定 index */
//        request.indices(index);
        /** 同步 */
        GetMappingsResponse getMappingResponse = client.indices().getMapping(request, RequestOptions.DEFAULT);
        Map<String, MappingMetaData> mappings = getMappingResponse.mappings();
        ObjectMapper mappers = new ObjectMapper();
        /** 方法1 显示结果 */
//        Set<Map.Entry<String, MappingMetaData>> entries = mappings.entrySet();
//        Iterator<Map.Entry<String, MappingMetaData>> iterator = entries.iterator();
//        Map.Entry<String, MappingMetaData> next = iterator.next();
//        System.out.println(next.getKey() +"\n"+mappers.writeValueAsString(next.getValue().getSourceAsMap()));
        /** 方式2 全部查出来 再过滤 */
        MappingMetaData indexMapping = mappings.get("personindex");
        Map<String, Object> mapping = indexMapping.sourceAsMap();
        System.out.println(mappers.writeValueAsString(mapping));
        System.out.println();
        System.out.println();
        /** 异步 */
//        ActionListener<GetMappingsResponse> listener =
//                new ActionListener<GetMappingsResponse>() {
//                    @Override
//                    public void onResponse(GetMappingsResponse putMappingResponse) {
//                        Map<String, MappingMetaData> mappings = getMappingResponse.mappings();
//                        Set<Map.Entry<String, MappingMetaData>> entries = mappings.entrySet();
//                        Iterator<Map.Entry<String, MappingMetaData>> iterator = entries.iterator();
//                        while(iterator.hasNext()){
//                            Map.Entry<String, MappingMetaData> next = iterator.next();
//                            ObjectMapper mappers = new ObjectMapper();
//
//                            try {
//                                System.out.println(next.getKey() +"\n"+mappers.writeValueAsString(next.getValue().getSourceAsMap()));
//                            } catch (JsonProcessingException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        System.out.println();
//                        System.out.println();
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                };
//        client.indices().getMappingAsync(request, RequestOptions.DEFAULT, listener);

    }

    /**
     * 添加数据
     */
    @Test
    public void putData() throws IOException {
        index = "personindex";
        /** 方式1 */
//        IndexRequest request = new IndexRequest(index);
//        request.id("1");
//        String jsonString = "{" +
//                "\"name\":\"小尘\"," +
//                "\"age\":\"6\"," +
//                "\"desc\":\"learn Elasticsearch\"" +
//                "}";
//        request.source(jsonString, XContentType.JSON);

        /** 方式2 */
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "刘备");
        jsonMap.put("age", 23);
        jsonMap.put("desc", "刘备 此人身长七尺五寸，两耳垂肩，双手过膝，目能自顾其耳，面如冠玉，唇若涂脂，性宽和，寡言语，喜怒不形于色，素有大志，专好结交天下豪杰。刘备的外貌描写如下： 此人身长七尺五寸，两耳垂肩，双手过膝，目能自顾其耳，面如冠玉，唇若涂脂，性宽和，寡言语，喜怒不形于色，素有大志，专好结交天下豪杰");
        IndexRequest request = new IndexRequest(index)
                .id("2").source(jsonMap);
        /** 方式3 */
//        XContentBuilder builder = XContentFactory.jsonBuilder();
//        builder.startObject();
//        {
//            builder.field("name", "小尘");
//            builder.field("age", 6);
//            builder.field("desc", "learn Elasticsearch");
//        }
//        builder.endObject();
//        IndexRequest indexRequest = new IndexRequest(index)
//                .id("1").source(builder);
//        IndexRequest indexRequest = new IndexRequest("posts")
//                .id("1")
//                .source("name", "小尘",
//                        "age", 6,
//                        "desc", "learn Elasticsearch");

        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        String index = indexResponse.getIndex();
        String id = indexResponse.getId();
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            System.out.println("创建文档");
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            System.out.println("修改文档");
        }
        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            System.out.println("成功分片数小于总的分片数");
        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure :
                    shardInfo.getFailures()) {
                String reason = failure.reason();
                System.err.println(reason);
            }
        }
    }


    /**
     *
     * @throws IOException
     */
    @Test
    public void search() throws IOException {
        index = "personindex";
//        SearchRequest searchRequest = new SearchRequest();
        /** 指定 index */
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        /** term 查询的字段不能再进行分词 */
//        searchSourceBuilder.query(QueryBuilders.termQuery("name", "刘"));
        /** 单个字段匹配 */
        searchSourceBuilder.query(QueryBuilders.matchQuery("name", "刘备"));
        /** 多个字段匹配 */
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("刘备", "name","desc"));
        /** 分页查询 */
//        searchSourceBuilder.from(0);
//        searchSourceBuilder.size(5);
        searchRequest.source(searchSourceBuilder);
        /** 模糊匹配 */
//        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name", "尘");
//        /** 开启模糊查询 */
//        matchQueryBuilder.fuzziness(Fuzziness.FIELD);
//        /** 前缀匹配长度 */
//        matchQueryBuilder.prefixLength(2);
//        /** 最大模糊数 */
//        matchQueryBuilder.maxExpansions(10);
//        searchRequest.source(searchSourceBuilder);
        /** 对文档进行排序 */
//        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
//        searchSourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.ASC));
        /** 关闭解锁_source */
//        searchSourceBuilder.fetchSource(false);
        /** 过滤 field */
        String[] includeFields = new String[] {"name","desc"};
        String[] excludeFields = new String[] {"age"};
        searchSourceBuilder.fetchSource(includeFields, excludeFields);
        /** 高亮 */
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle =new HighlightBuilder.Field("name");
        highlightTitle.highlighterType("unified");
        highlightBuilder.field(highlightTitle);
        HighlightBuilder.Field highlightUser = new HighlightBuilder.Field("desc");
        highlightBuilder.field(highlightUser);
        searchSourceBuilder.highlighter(highlightBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        TotalHits totalHits = hits.getTotalHits();
        /** 总的命中数 */
        long numHits = totalHits.value;
        System.err.println("总的命中数:  "+numHits);
        TotalHits.Relation relation = totalHits.relation;
        /** 匹配度 */
        float maxScore = hits.getMaxScore();
        System.err.println("匹配度  "+maxScore);
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String index = hit.getIndex();
            String id = hit.getId();
            float score = hit.getScore();
            System.out.println("index "+index +" id "+id+ " score "+ score);
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String desc = (String) sourceAsMap.get("desc");
            System.out.println(name+"  "+desc);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlight = highlightFields.get("name");
            ObjectMapper objectMapper = new ObjectMapper();
            if(highlight != null){
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                System.err.println(fragmentString);
                sourceAsMap.put("name",fragmentString);
            }
            HighlightField highlight2 = highlightFields.get("desc");
            if(highlight2 != null){
                Text[] fragments2 = highlight2.fragments();
                String fragmentString2 = fragments2[0].string();
                System.err.println(fragmentString2);
                sourceAsMap.put("desc",fragmentString2);
            }
            System.err.println(objectMapper.writeValueAsString(sourceAsMap));
        }
    }

    @After
    public void after() throws IOException {
        client.close();
    }



}
