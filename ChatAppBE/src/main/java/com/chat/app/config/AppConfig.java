package com.chat.app.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.chat.app.service.impl.AccountServiceImpl;

@Configuration
public class AppConfig {

	@Autowired
	private AccountServiceImpl accountService;

	@Autowired
	private JwtTokenValidator jwtAuthenticationFilter;
	
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> 
                authorize.requestMatchers("/api/v1/chat/**", 
                		"/api/v1/users/**", "/api/v1/friendship/**", "/api/v1/channel/**").authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable).cors(cors -> cors.configurationSource(getCorsConfiguration()));
        		
		return http.build();
	}
	
	@Bean
	protected CorsConfigurationSource getCorsConfiguration() {
		CorsConfiguration cfg = new CorsConfiguration();
		cfg.setAllowedOrigins(Arrays.asList("http://localhost:3000/"));
		cfg.setAllowCredentials(true);
		cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		cfg.setAllowedHeaders(
				Arrays.asList("Authorization", "Cache-Control", "Content-Type", "XSRF-TOKEN", "X-XSRF-TOKEN"));
		cfg.setExposedHeaders(Arrays.asList("Authorization"));
		cfg.setMaxAge(3600L);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/v1/**", cfg);
		
		return source;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(accountService.userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
}
