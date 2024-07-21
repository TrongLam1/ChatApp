package com.chat.app.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.chat.app.service.impl.AccountServiceImpl;
import com.chat.app.service.impl.JwtServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class JwtTokenValidator extends OncePerRequestFilter {

	@Autowired
	private AccountServiceImpl accountService;
	
	@Autowired
	private JwtServiceImpl jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    final String authHeader = request.getHeader("Authorization");
	    final String jwt;
	    final String userEmail;

	    try {
	        if (authHeader == null || authHeader.trim().isEmpty() || !authHeader.trim().startsWith("Bearer ")) {
	            filterChain.doFilter(request, response);
	            return;
	        }

	        jwt = authHeader.substring(7);
	        userEmail = jwtService.extractUsername(jwt);

	        if (userEmail != null && !userEmail.trim().isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
	            UserDetails userDetails = accountService.userDetailsService().loadUserByUsername(userEmail);
	            if (jwtService.isTokenValid(jwt, userDetails)) {
	                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

	                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null,
	                        userDetails.getAuthorities());

	                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	                securityContext.setAuthentication(token);
	                SecurityContextHolder.setContext(securityContext);
	            } else {
	                throw new RuntimeException("Token is expired");
	            }
	        }
	    } catch (Exception e) {
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write(e.toString());
	        response.getWriter().flush();
	        return;
	    }

	    response.setHeader("Access-Control-Allow-Origin", "*");

	    filterChain.doFilter(request, response);
	}
}
