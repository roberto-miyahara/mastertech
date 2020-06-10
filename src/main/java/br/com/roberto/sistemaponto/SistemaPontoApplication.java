package br.com.roberto.sistemaponto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("br.com.roberto.sistemaponto.persistence.repository")
@EntityScan("br.com.roberto.sistemaponto.persistence.model")
@SpringBootApplication
public class SistemaPontoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaPontoApplication.class, args);
	}
}
