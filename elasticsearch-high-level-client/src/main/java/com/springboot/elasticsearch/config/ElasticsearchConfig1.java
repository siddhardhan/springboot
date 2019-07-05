package com.springboot.elasticsearch.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration

public class ElasticsearchConfig1 {

    @Value("${elasticsearch.2.host}")
    private String host2;

    @Value("${elasticsearch.2.port}")
    private int port2;

    @Value("${elasticsearch.2.username}")
    private String userName2;

    @Value("${elasticsearch.2.password}")
    private String password2;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restClient2() throws IOException {

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(userName2, password2));

        RestClientBuilder builder = RestClient.builder(new HttpHost(host2, port2))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        RestHighLevelClient client = new RestHighLevelClient(builder);

        return client;

    }
}
