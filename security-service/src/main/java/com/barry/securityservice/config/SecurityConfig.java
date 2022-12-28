/**
 * 
 */
package com.barry.securityservice.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

/**
 * @author algas
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {
	
	private RsaKeysConfig rsaKeysConfig;
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired
	public SecurityConfig(RsaKeysConfig rsaKeysConfig, PasswordEncoder passwordEncoder) {
		super();
		this.rsaKeysConfig = rsaKeysConfig;
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	public UserDetailsService inMemoryUserDetailsManager() {
		return new InMemoryUserDetailsManager(
				User.withUsername("user1").password(passwordEncoder.encode("1234")).authorities("USER").build(),
				User.withUsername("user2").password(passwordEncoder.encode("1234")).authorities("USER").build(),
				User.withUsername("user3").password(passwordEncoder.encode("1234")).authorities("USER, ADMIN").build()
				);
	}
	
	
	@Bean
	AuthenticationManager authenticationManager(UserDetailsService userDetailsService ) {
		var authProvider =  new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(passwordEncoder);
		authProvider.setUserDetailsService(userDetailsService);
		 return new ProviderManager(authProvider);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
		return httpSecurity
				.csrf().disable()
				.authorizeHttpRequests(auth->auth.antMatchers("token").permitAll())
				.authorizeHttpRequests(auth->auth.anyRequest().authenticated())
				.sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				.httpBasic(Customizer.withDefaults())
				.build();
				
		
	}
	
	@Bean
	JwtEncoder encoder() {
		
		JWK  jwk = new RSAKey.Builder(rsaKeysConfig.publicKey()).privateKey(rsaKeysConfig.privateKey()).build();
		JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwkSource);
	}
	
	@Bean
	JwtDecoder decoder() {
		return NimbusJwtDecoder.withPublicKey(rsaKeysConfig.publicKey()).build();
	}

}
