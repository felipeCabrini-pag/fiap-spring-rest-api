package br.com.fiap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ServerErrorException;

import java.io.IOException;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://pokeapi.co/api/v2")
                .defaultStatusHandler(new CustomResponseErrorHandler()) // Define o ErrorHandler
                .build();
    }

    private static class CustomResponseErrorHandler extends DefaultResponseErrorHandler {

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            HttpStatus statusCode = (HttpStatus) response.getStatusCode();
            switch (statusCode) {
                //case NOT_FOUND -> throw new ResourceNotFoundException("Recurso não encontrado na PokéAPI");
                case INTERNAL_SERVER_ERROR -> throw new ServerErrorException("Erro no servidor da PokéAPI", new RuntimeException());
                case BAD_REQUEST -> throw new IllegalArgumentException("Requisição inválida");
                //default -> throw new GeneralErrorException("Erro desconhecido: " + statusCode);
            }
        }
    }
}
