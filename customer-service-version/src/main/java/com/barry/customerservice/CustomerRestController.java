/**
 * 
 */
package com.barry.customerservice;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author algas
 *
 */
@RestController
@RequestMapping("/customers")
public class CustomerRestController {

	@GetMapping
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public Map<String, Object> getCustomers(Authentication  authentication){
		return Map.of("Name", "Mohmed", "email", "moh@gmail.com", "ville", "Houston",
				"username", authentication.getName(),
				"scope", authentication.getAuthorities());
	}
}
