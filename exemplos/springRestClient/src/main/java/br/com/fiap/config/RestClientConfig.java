package br.com.fiap.config;


import br.com.fiap.service.client.PokemonFeignClient;
import br.com.fiap.service.client.PokemonRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public PokemonRestClient pokemonRestClient() {
        RestClient restClient = RestClient.builder().baseUrl("https://pokeapi.co/api/v2").build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        PokemonRestClient pokemonFeignClient = factory.createClient(PokemonRestClient.class);
        return pokemonFeignClient;

    }
}