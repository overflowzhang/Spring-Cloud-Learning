package com.zhang.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@RestController
public class UseHelloController {

    @Autowired
    @Qualifier("restTemplateOne")
    RestTemplate restTemplateOne;

    @Autowired
    @Qualifier("restTemplate")
    RestTemplate restTemplate;

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/hello1")
    public String hello1() throws IOException {
        HttpURLConnection con = null;
        URL url = new URL("http://localhost:1113/hello");
        con = (HttpURLConnection) url.openConnection();
        if (con.getResponseCode() == 200) {
            InputStream in;
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String s = br.readLine();
            br.close();
            return s;
        }
        return "error";
    }

    @GetMapping("/hello2")
    public String hello2() {
        List<ServiceInstance> list = discoveryClient.getInstances("provider");
        ServiceInstance instance = list.get(0);
        String host = instance.getHost();
        int port = instance.getPort();
        StringBuilder sb = new StringBuilder();
        sb.append("http://")
                .append(host)
                .append(":")
                .append(port)
                .append("/hello");
        // 用 RestTemplate 一行代码就实现了 HTTP 调用
        return restTemplateOne.getForObject(sb.toString(),String.class);
    }

    // 实现线性负载均衡
    int count = 0;
    @GetMapping("/hello3")
    // 使用 Ribbon 实现负载均衡
    public String hello3() {
        return restTemplate.getForObject("http://provider/hello", String.class);
    }
}
