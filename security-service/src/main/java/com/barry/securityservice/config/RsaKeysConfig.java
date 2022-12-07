package com.barry.securityservice.config;

import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;

@SuppressWarnings("preview")
@ConfigurationProperties(prefix = "rsa")

public record RsaKeysConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey ) {

}
