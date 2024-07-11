package com.empik.empikinterviewtask.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GitHubRestClientConfig {

    @Value("${com.empik.interview.task.api.url}")
    private String apiUrl;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .requestInterceptor(new LoggingInterceptor())
                .baseUrl(apiUrl)
                .build();
    }

}
