package com.gdsc.jupgging;

import com.gdsc.jupgging.config.CorsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CorsConfig.class)
public class JupggingApplication {

	public static void main(String[] args) {
		SpringApplication.run(JupggingApplication.class, args);
	}
}
