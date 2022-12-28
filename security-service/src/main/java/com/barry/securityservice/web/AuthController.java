/**
 * 
 */
package com.barry.securityservice.web;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author algas
 *
 */
@RestController
@Slf4j
public class AuthController {
	
	
	private JwtEncoder encoder;
	
	private JwtDecoder jwtDecoder;
	private AuthenticationManager authManager;
	private UserDetailsService userDetailsService;
	
	
	@Autowired
	public AuthController(JwtEncoder encoder, JwtDecoder jwtDecoder, AuthenticationManager authManager, UserDetailsService userDetailsService) {
		this.encoder = encoder;
		this.jwtDecoder = jwtDecoder;
		this.authManager = authManager;
		this.userDetailsService = userDetailsService;
	}
	
	@PostMapping("/token")
	 public ResponseEntity<Map<String, String>> jwtToken(String grantType, String username, String password, boolean withRefreshToken, String refreshToken){
		//Authentication authentication = <;
		String subject =null;
		String scope  = null;
		Jwt decodeJWT =null;
		if(grantType.equals("password")) {
			
			log.info(subject);
			
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password));
			
			subject = authentication.getName();
			scope = authentication.getAuthorities()
					.stream()
					.map(auth->auth.getAuthority()).collect(Collectors.joining(" "));
			log.info(subject);
			 
		 }
		else if(grantType.equals("refreshToken")) {
			if(refreshToken == null) {
				return new ResponseEntity<Map<String,String>>(Map.of("Error message", "Refresh token is required"), HttpStatus.UNAUTHORIZED);
			}
			try {
				decodeJWT = jwtDecoder.decode(refreshToken);
				
			} catch (Exception e) {
				return new ResponseEntity<Map<String,String>>(Map.of("Error message", e.getMessage()), HttpStatus.UNAUTHORIZED);

			}
		
			
			subject = decodeJWT.getSubject();
			UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
			 Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
			 scope = authorities
						.stream()
						.map(auth->auth.getAuthority()).collect(Collectors.joining(" "));
				 
			
		}
 		log.info(subject);

		
		Map<String, String> idToken = new HashMap<>();

		Instant instant = Instant.now();


		JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
				.subject(subject)
				.issuedAt(instant)
				.expiresAt(instant.plus(withRefreshToken ? 1: 5, ChronoUnit.MINUTES))
				.issuer("security-service")
				.claim("scope", scope)
				.build();
		String jwtAccessToken = encoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
		idToken.put("accessToken", jwtAccessToken);
		
		if(withRefreshToken) {
			log.info(subject);
			JwtClaimsSet jwtClaimsRefresh = JwtClaimsSet.builder()
					.subject(subject)
					.issuedAt(instant)
					.expiresAt(instant.plus(5, ChronoUnit.MINUTES))
					.issuer("security-service")
					.build();
			String jwtRefreshToken = encoder.encode(JwtEncoderParameters.from(jwtClaimsRefresh)).getTokenValue();
			idToken.put("refreshToken", jwtRefreshToken);

		}
		
        return new ResponseEntity<>(idToken, HttpStatus.OK);
    } 
	
	

}
