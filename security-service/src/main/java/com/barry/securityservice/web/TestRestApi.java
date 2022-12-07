/**
 * 
 */
package com.barry.securityservice.web;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author algas
 *
 */
@RestController
public class TestRestApi {

	@GetMapping("/dataTest")
    public Map<String, Object> dataTest(Authentication authentication){
        return Map.of("Message", "DataTest", "username:", authentication.getName(), "Authorities:", authentication.getAuthorities());
    }
}
