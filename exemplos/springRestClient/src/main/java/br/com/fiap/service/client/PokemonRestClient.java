package br.com.fiap.service.client;


import br.com.fiap.entity.Pokemon;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/pokemon")
public interface PokemonRestClient {

    @GetExchange("/{name}")
    Pokemon getPokemonByName(@PathVariable("name") String name);
}