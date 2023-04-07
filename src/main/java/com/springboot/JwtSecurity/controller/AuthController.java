package com.springboot.JwtSecurity.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.JwtSecurity.Jwt.JwtTokenUtil;
import com.springboot.JwtSecurity.model.request.AuthRequest;
import com.springboot.JwtSecurity.model.response.AuthResponse;
import com.springboot.JwtSecurity.security.CustomUserDetails;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
public class AuthController {
	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtTokenUtil jwtUtil;

	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
			String accessToken = jwtUtil.generateAccessToken(user.getUsername());
			AuthResponse response = new AuthResponse(user.getUsername(), accessToken);

			return ResponseEntity.ok().body(response);

		} catch (BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	@GetMapping("/auth/validate")
	public ResponseEntity<?> validateToken(@RequestParam String token, @AuthenticationPrincipal User user){
		try {
			Boolean isValidToken = jwtUtil.validateToken(token, user);
			return ResponseEntity.ok(isValidToken);
		} catch (ExpiredJwtException e) {
			return ResponseEntity.ok(false);
		}
	}
}
