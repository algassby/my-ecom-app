/**
 * 
 */
package com.barry.customerservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * @author algas
 *
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private RsaKeysConfig rsaKeysConfig;

	
	@Bean
	public SecurityFilterChain setSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(csrf->csrf.disable())
				.authorizeRequests(auth->auth.anyRequest().authenticated())
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				.build();
		
	}
	
	@Bean
	public JwtDecoder decoder() {
		return NimbusJwtDecoder.withPublicKey(rsaKeysConfig.publicKey()).build();
	}

}
