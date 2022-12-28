package com.barry.customerservice;

import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;


	@ConfigurationProperties(prefix = "rsa")
	
	public record RsaKeysConfig(RSAPublicKey publicKey) {
	
	}
	


