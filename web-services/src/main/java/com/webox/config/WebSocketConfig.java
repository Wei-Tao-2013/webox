package com.webox.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements  WebSocketConfigurer {

  @Value("${encrypted.tokenSecret}")
  private String tokenSecret;
  
  //@Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
  
  WebSocketInterceptor  webSocketInterceptor = new WebSocketInterceptor();
  webSocketInterceptor.setTokenSecret(tokenSecret);
    registry.addHandler(WebSocketPushHandler(), "/webSocketServer").
    addInterceptors(webSocketInterceptor).setAllowedOrigins("*");
    registry.addHandler(WebSocketPushHandler(), "/sockjs/webSocketServer")
        .addInterceptors(new WebSocketInterceptor()).withSockJS();
  }

  @Bean
  public WebSocketHandler WebSocketPushHandler() {
    return new WebSocketPushHandler();
  }

}
