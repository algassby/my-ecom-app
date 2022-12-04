/**
 * 
 */
package com.barry.securityservice.web;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author algas
 *
 */
@RestController
public class TestRestApi {

	@GetMapping("/dataTest")
    public Map<String, Object> dataTest(){
        return Map.of("Message", "DataTest");
    }
}
