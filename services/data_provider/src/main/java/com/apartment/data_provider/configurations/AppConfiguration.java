//package com.apartment.data_provider.configurations;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import java.net.ProxySelector;
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
//    @Primary
//    public HttpClient getHttpClient() {
//        return HttpClient.newHttpClient();
//    }
//
//    @Bean(name = "torHttpClient")
//    public HttpClient getTorHttpClient() {
//        ProxySelector proxySelector = new ProxySelector() {
//            @Override
//            public java.util.List<java.net.Proxy> select(java.net.URI uri) {
//                return java.util.List.of(
//                        new java.net.Proxy(java.net.Proxy.Type.SOCKS, new java.net.InetSocketAddress(proxyHost, proxyPort))
//                );
//            }
//
//            @Override
//            public void connectFailed(java.net.URI uri, java.net.SocketAddress sa, java.io.IOException ioe) {
//                ioe.printStackTrace();
//            }
//        };
//
//        return HttpClient.newBuilder()
//                .version(HttpClient.Version.HTTP_2)
//                .proxy(proxySelector)
//                .build();
//    }
//
//
//    @Bean
//    public ObjectMapper getObjectMapper() {
//        ObjectMapper objectMapper =  new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.findAndRegisterModules();
//        return objectMapper;
//    }
//}





package com.apartment.data_provider.configurations;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.net.*;
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

    @Bean(name = "socksHttpClient")
    public HttpClient getTorHttpClient() {
        ProxySelector proxySelector = new ProxySelector() {
            @Override
            public java.util.List<java.net.Proxy> select(java.net.URI uri) {
                return java.util.List.of(
                        new java.net.Proxy(java.net.Proxy.Type.SOCKS, new java.net.InetSocketAddress(proxyHost, proxyPort))
                );
            }

            @Override
            public void connectFailed(java.net.URI uri, java.net.SocketAddress sa, java.io.IOException ioe) {
                ioe.printStackTrace();
            }
        };
        return HttpClient.newBuilder()
                .proxy(proxySelector)
                .build();
    }


    @Bean(name = "proxyHttpClient")
    public HttpClient getProxyHttpClient(){

        ProxySelector proxySelector = new ProxySelector() {
            @Override
            public java.util.List<Proxy> select(URI uri) {
                return java.util.List.of(
                        new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort))
                );
            }

            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                ioe.printStackTrace();
            }
        };

        return HttpClient.newBuilder()
                .proxy(proxySelector)
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
