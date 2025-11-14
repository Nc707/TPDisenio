package edu.inbugwethrust.premier.suite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "edu.inbugwethrust.premier.suite.repositories")
public class SuiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuiteApplication.class, args);
	}
}
