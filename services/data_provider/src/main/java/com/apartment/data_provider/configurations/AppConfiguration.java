package com.apartment.data_provider.configurations;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.http.HttpClient;

@Configuration
public class AppConfiguration {

    @Value("${proxy.host}")
    private String proxyHost;

    @Value("${proxy.port}")
    private int proxyPort;

    @Bean
    @Primary
    public HttpClient getHttpClient() {
        return HttpClient.newHttpClient();
    }

    @Bean(name = "torHttpClient")
    public HttpClient getTorHttpClient(){

        // --- ВАЖНО: включаем SOCKS5 для JVM ---
        System.setProperty("socksProxyHost", proxyHost);
        System.setProperty("socksProxyPort", String.valueOf(proxyPort));

        // HttpClient больше НЕ использует ProxySelector,
        // он работает через SOCKS5 прозрачным образом.
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper =  new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
}





//package com.apartment.data_provider.configurations;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//import java.net.*;
//import java.net.http.HttpClient;
//
//@Configuration
//public class AppConfiguration {
//
//    @Value("${proxy.host}")
//    private String proxyHost;
//
//    @Value("${proxy.port}")
//    private int proxyPort;
//
//    @Bean
//    public HttpClient getHttpClient(){
//
//        ProxySelector proxySelector = new ProxySelector() {
//            @Override
//            public java.util.List<Proxy> select(URI uri) {
//                return java.util.List.of(
//                        new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort))
//                );
//            }
//
//            @Override
//            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
//                ioe.printStackTrace();
//            }
//        };
//
//        return HttpClient.newBuilder()
//                .proxy(proxySelector)
//                .build();
//    }
//
//    @Bean
//    public ObjectMapper getObjectMapper() {
//        ObjectMapper objectMapper =  new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.findAndRegisterModules();
//        return objectMapper;
//    }
//}
