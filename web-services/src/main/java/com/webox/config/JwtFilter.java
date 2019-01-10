package com.webox.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.Data;

@Data
public class JwtFilter extends GenericFilterBean {

	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

	private String tokenSecret;

	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		final String authHeader = request.getHeader("authorization");

		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
			chain.doFilter(req, res);
		} else if ("GET".equals(request.getMethod())) {
			final String token = request.getParameter("token");
			try {
				final Claims claims = Jwts.parser().setSigningKey(this.getTokenSecret()).parseClaimsJws(token)
						.getBody();
				// logger.debug("claims is {} ", claims);
				// request.setAttribute("claims", claims);
			} catch (final SignatureException e) {
				logger.debug("Invalid token {} ", token);
				throw new ServletException("Invalid token.");
			}
			chain.doFilter(req, res);
		} else {
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				logger.debug("Missing or invalid Authorization header");
				throw new ServletException("Missing or invalid Authorization header");
			}
			final String token = authHeader.substring(7);
			try {
				final Claims claims = Jwts.parser().setSigningKey(this.getTokenSecret()).parseClaimsJws(token)
						.getBody();
				// logger.debug("claims is {} ", claims);
				// request.setAttribute("claims", claims);
			} catch (final SignatureException e) {
				logger.debug("Invalid token {} ", token);
				throw new ServletException("Invalid token.");
			}
			chain.doFilter(req, res);
		}
	}
}
