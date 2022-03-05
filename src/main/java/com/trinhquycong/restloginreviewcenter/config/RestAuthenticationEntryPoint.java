package com.trinhquycong.restloginreviewcenter.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		logger.error("Responding with unauthorized error. Message - {}", e.getMessage());
		
		// use HttpServletResponse.SC_UNAUTHORIZED is more appropriate, but 
		// it causes the client redirect to the /login route, which is 
		// an unexpected thing
		response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getLocalizedMessage());
	}

}
