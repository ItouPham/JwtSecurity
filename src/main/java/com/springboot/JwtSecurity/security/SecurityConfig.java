package com.springboot.JwtSecurity.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboot.JwtSecurity.Jwt.JwtTokenFilter;
import com.springboot.JwtSecurity.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private JwtTokenFilter jwtTokenFilter;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http.csrf().disable();
	        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	         
	        http.authorizeRequests()
	                .antMatchers("/auth/login", "/docs/**", "/users").permitAll()
	                .anyRequest().authenticated();
	         
	            http.exceptionHandling()
	                    .authenticationEntryPoint(
	                        (request, response, ex) -> {
	                            response.sendError(
	                                HttpServletResponse.SC_UNAUTHORIZED,
	                                ex.getMessage()
	                            );
	                        }
	                );
	         
	        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
