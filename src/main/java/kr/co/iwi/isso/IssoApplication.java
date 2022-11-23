package kr.co.iwi.isso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = { "kr.co.iwi.isso" })
public class IssoApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssoApplication.class, args);
	}

}
