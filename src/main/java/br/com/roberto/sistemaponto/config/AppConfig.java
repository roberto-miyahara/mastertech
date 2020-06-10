package br.com.roberto.sistemaponto.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Sistema de Ponto API")
                        .description("Documentação do teste do Sistema de Ponto."));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
