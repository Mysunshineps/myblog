package com.cy;

import com.cy.pj.sys.utils.SpringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsApplication.class, args);
	}

	@Bean
	public SpringUtils springUtilsBean(){
		return new SpringUtils();
	}

}
