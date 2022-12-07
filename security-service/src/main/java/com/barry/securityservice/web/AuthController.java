/**
 * 
 */
package com.barry.securityservice.web;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author algas
 *
 */
@RestController
public class AuthController {
	
	
	private JwtEncoder encoder;

	
	@Autowired
	public AuthController(JwtEncoder encoder) {
		this.encoder = encoder;
	}
	
	@PostMapping("/token")
	 public Map<String, String> jwtToken(Authentication authentication ){
		Instant instant = Instant.now();
		String scope = authentication.getAuthorities()
				.stream()
				.map(auth->auth.getAuthority()).collect(Collectors.joining(" "));
		
		JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
				.subject(authentication.getName())
				.issuedAt(instant)
				.expiresAt(instant.plus(5, ChronoUnit.MINUTES))
				.issuer("security-service")
				.claim("scope", scope)
				.build();
		
		String jwtAccessToken = encoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        return Map.of("accessToken", jwtAccessToken);
    } 
	
	

}
