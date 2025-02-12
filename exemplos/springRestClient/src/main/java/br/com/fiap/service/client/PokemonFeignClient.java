package br.com.fiap.service.client;

import br.com.fiap.entity.Pokemon;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "pokemonClient", url = "https://pokeapi.co/api/v2")
public interface PokemonFeignClient {

    @GetMapping("/pokemon/{name}")
    Pokemon getPokemonByName(@PathVariable("name") String name);
}