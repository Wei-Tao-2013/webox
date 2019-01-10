package com.webox.config;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeInterceptor;


import lombok.Data;

@Data
/**
 * for getting login user's info then handover ot websocket
 */
public class WebSocketInterceptor implements HandshakeInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(WebSocketInterceptor.class);

  private String tokenSecret;

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse arg1, WebSocketHandler arg2,
      Map<String, Object> arg3) throws Exception {

    // convert ServerHttpRequest to request for getting user's info

    if (request instanceof ServletServerHttpRequest) {
      ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
      HttpServletRequest httpRequest = servletRequest.getServletRequest();
      /*
       * String authHeader = httpRequest.getHeader("authorization");
       * 
       * if (authHeader == null || !authHeader.startsWith("Bearer ")) {
       * logger.debug("Missing or invalid Authorization header"); throw new
       * ServletException("Missing or invalid Authorization header"); } final String
       * token = authHeader.substring(7); try {
       * Jwts.parser().setSigningKey(this.getTokenSecret()).parseClaimsJws(token).
       * getBody(); } catch (final SignatureException e) {
       * logger.debug("Invalid token {} ", token); throw new
       * ServletException("Invalid token."); }
       */

    }
    // System.out.println("Connected me ...");
    return true;
  }

  @Override
  public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
    // TODO Auto-generated method stub
  }

}