package br.com.fiap.service;

import br.com.fiap.controller.dto.PokemonDTO;
import br.com.fiap.entity.Pokemon;
import br.com.fiap.repository.PokemonRepository;
import br.com.fiap.service.dto.PokemonRestDTO;
import ch.qos.logback.classic.spi.IThrowableProxy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Random;

@Service
public class PokemonService {

    private PokemonRepository pokemonRepository;
    private final Random random = new Random();
    private RestClient restClient;

    public PokemonService(PokemonRepository pokemonRepository,RestClient restClient) {
        this.pokemonRepository = pokemonRepository;
        this.restClient = restClient;
    }


    public List<PokemonDTO> getAllPokemons() {
        return pokemonRepository.findAll()
                .stream().map(
                        pokemon -> new PokemonDTO(pokemon.getName(), pokemon.getLevel(),pokemon.getType(),pokemon.getImageUrl())).toList();
    }

    public PokemonDTO captureRandomPokemon() {
        int randomId = random.nextInt(1025) + 1;
        PokemonRestDTO pokemonRestDTO = restClient.get()
                .uri("https://pokeapi.co/api/v2/pokemon/{id}",randomId)
                .retrieve()
                .body(PokemonRestDTO.class);

        if(pokemonRestDTO != null) {
            Pokemon pokemon = convertToEntity(pokemonRestDTO);
            pokemonRepository.save(pokemon);
            return convertToDTO(pokemon);
        } else {
             throw new RuntimeException("Pokemon not found");
        }
    }

    private PokemonDTO convertToDTO(Pokemon pokemon) {
        return new PokemonDTO(pokemon.getName(), pokemon.getLevel(),pokemon.getType(),pokemon.getImageUrl());
    }

    private Pokemon convertToEntity(PokemonRestDTO pokemonRestDTO) {
        Pokemon pokemon = new Pokemon();
        pokemon.setName(pokemonRestDTO.getName());
        pokemon.setType(pokemonRestDTO.geType());
        pokemon.setImageUrl(pokemonRestDTO.getImageUrl());
        return pokemon;
    }
}
