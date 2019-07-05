package com.springboot.elasticsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.elasticsearch.document.EmployeeDocument;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.springboot.elasticsearch.util.Constant.INDEX;
import static com.springboot.elasticsearch.util.Constant.TYPE;

@Service
@Slf4j
public class EmployeeService {

    private RestHighLevelClient client;

    private ObjectMapper objectMapper;

    @Autowired
    public EmployeeService(@Qualifier("restClient") RestHighLevelClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public String createProfileDocument(List<EmployeeDocument> employeeDocumentList) throws Exception {

        // creating the index

        GetIndexRequest getIndexRequest = new GetIndexRequest(INDEX);
        if (! client.indices().exists(getIndexRequest, RequestOptions.DEFAULT)) {

            CreateIndexRequest createIndexRequest = new CreateIndexRequest(INDEX);

            createIndexRequest.settings(Settings.builder()
                    .put("index.number_of_shards", 3)
                    .put("index.number_of_replicas", 2)
            );

            createIndexRequest.mapping(
                    "{\n" +
                            "            \"properties\": {\n" +
                            "                \"id\": { \"type\": \"text\" }, \n" +
                            "                \"name\": { \"type\": \"text\" },\n" +
                            "                \"designation\": { \"type\": \"text\" },\n" +
                            "                \"departmentList\" : {\n" +
                            "                    \"type\" : \"nested\",\n" +
                            "                    \"include_in_parent\" : true,\n" +
                            "                    \"properties\" : {\n" +
                            "                    \"name\" : {\"type\" : \"text\"},\n" +
                            "                    \"type\" : {\"type\" : \"text\"}\n" +
                            "                    }\n" +
                            "                }\n" +
                            "            }\n" +
                            "}",
                    XContentType.JSON);
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        }

        // create document

        BulkRequest request = new BulkRequest();

        for (EmployeeDocument employeeDocument : employeeDocumentList) {

            UUID uuid = UUID.randomUUID();
            employeeDocument.setId(uuid.toString());

            request.add(new IndexRequest(INDEX, TYPE, employeeDocument.getId())
                    .source(convertProfileDocumentToMap(employeeDocument)));
        }

        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);

        if (bulkResponse.hasFailures()){
            return bulkResponse.buildFailureMessage();
        }
        return "CREATED";
    }

    public EmployeeDocument findById(String id) throws Exception {

            GetRequest getRequest = new GetRequest(INDEX, TYPE, id);

            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            Map<String, Object> resultMap = getResponse.getSource();

            return convertMapToProfileDocument(resultMap);

    }



    public String updateProfile(EmployeeDocument document) throws Exception {

            EmployeeDocument resultDocument = findById(document.getId());

            UpdateRequest updateRequest = new UpdateRequest(
                    INDEX,
                    TYPE,
                    resultDocument.getId());

            updateRequest.doc(convertProfileDocumentToMap(document));
            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);

            return updateResponse
                    .getResult()
                    .name();

    }

    public List<EmployeeDocument> findAll() throws Exception {


        SearchRequest searchRequest = buildSearchRequest(INDEX,TYPE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);

        return getSearchResult(searchResponse);
    }


    public List<EmployeeDocument> findProfileByName(String name) throws Exception{


        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX);
        searchRequest.types(TYPE);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MatchQueryBuilder matchQueryBuilder = QueryBuilders
                .matchQuery("name",name)
                .operator(Operator.AND);

        searchSourceBuilder.query(matchQueryBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);

        return getSearchResult(searchResponse);

    }


    public String deleteProfileDocument(String id) throws Exception {

        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
        DeleteResponse response = client.delete(deleteRequest,RequestOptions.DEFAULT);

        return response
                .getResult()
                .name();

    }

    private Map<String, Object> convertProfileDocumentToMap(EmployeeDocument profileDocument) {
        return objectMapper.convertValue(profileDocument, Map.class);
    }

    private EmployeeDocument convertMapToProfileDocument(Map<String, Object> map){
        return objectMapper.convertValue(map, EmployeeDocument.class);
    }


    public List<EmployeeDocument> searchByTechnology(String technology) throws Exception{

        SearchRequest searchRequest = buildSearchRequest(INDEX,TYPE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder queryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders
                        .matchQuery("technologies.name",technology));

        searchSourceBuilder.query(QueryBuilders.nestedQuery("technologies",queryBuilder,ScoreMode.Avg));

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);

        return getSearchResult(response);
    }

    private List<EmployeeDocument> getSearchResult(SearchResponse response) {

        SearchHit[] searchHit = response.getHits().getHits();

        List<EmployeeDocument> profileDocuments = new ArrayList<>();

        for (SearchHit hit : searchHit){
            profileDocuments
                    .add(objectMapper
                            .convertValue(hit
                                    .getSourceAsMap(), EmployeeDocument.class));
        }

        return profileDocuments;
    }

    private SearchRequest buildSearchRequest(String index, String type) {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.types(type);

        return searchRequest;
    }
}
