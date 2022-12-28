/**
 * 
 */
package com.barry.securityservice.web;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author algas
 *
 */
@RestController
public class TestRestApi {

	@PreAuthorize("hasAuthority('SCOPE_USER, SCOPE_ADMIN')")
	@GetMapping("/dataTest")
    public Map<String, Object> dataTest(Authentication authentication){
        return Map.of("Message", "DataTest", "username:", authentication.getName(), "Authorities:", authentication.getAuthorities());
    }
	

	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@PostMapping("/saveData")
    public Map<String, Object> saveData(String data){
        return Map.of("dataSaved", data);
    }
}
