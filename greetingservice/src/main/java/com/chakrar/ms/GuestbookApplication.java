package com.chakrar.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GuestbookApplication {

    private static final String template = "Hello, %s!";

	public static void main(String[] args) {
		SpringApplication.run(GuestbookApplication.class, args);
	}
	
	@RequestMapping("/greet")
    public String greet(@RequestParam(value="name", defaultValue="World") String name) {
		    System.out.println(" Greeting CALLED with "+name);
		    
            return String.format(template, name);
    }	
	
}


