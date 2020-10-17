package com.zhang.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    RestTemplate restTemplateOne() {
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced   // 此时的 Template就自动具备了负载均衡的功能
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
