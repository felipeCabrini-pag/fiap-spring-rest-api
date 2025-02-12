package br.com.fiap.service;


import br.com.fiap.controller.dto.PokemonResponseDTO;
import br.com.fiap.entity.Pokemon;
import br.com.fiap.repository.PokemonRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonRestTemplateService {

    private final RestTemplate restTemplate;
    private final PokemonRepository pokemonRepository;

    public PokemonRestTemplateService(RestTemplate restTemplate, PokemonRepository pokemonRepository) {
        this.restTemplate = restTemplate;
        this.pokemonRepository = pokemonRepository;
    }

    @Transactional
    public PokemonResponseDTO captureAndSavePokemon(String name) {
        String url = "https://pokeapi.co/api/v2/pokemon/" + name;
        Pokemon apiPokemon;

        try {
            apiPokemon = restTemplate.getForObject(url, Pokemon.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Pokémon não encontrado: " + name);
            }
            throw new RuntimeException("Erro ao buscar o Pokémon: " + e.getMessage());
        }

        Pokemon existingPokemon = pokemonRepository.findByName(apiPokemon.getName())
                .orElse(null);

        if (existingPokemon != null) {
            existingPokemon.setHeight(apiPokemon.getHeight());
            existingPokemon.setWeight(apiPokemon.getWeight());
            existingPokemon.setTypes(apiPokemon.getTypes());

            // Salva a entidade atualizada no banco
            pokemonRepository.save(existingPokemon);
            return convertToResponse(existingPokemon);
        } else {

            apiPokemon.setId(null);
            Pokemon savedPokemon = pokemonRepository.save(apiPokemon);
            return convertToResponse(savedPokemon);
        }
    }


    public List<PokemonResponseDTO> getAllCapturedPokemons() {
        return pokemonRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PokemonResponseDTO getPokemonById(Long id) {
        Pokemon pokemon = pokemonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pokémon não encontrado com o ID: " + id));
        return convertToResponse(pokemon);
    }

    private PokemonResponseDTO convertToResponse(Pokemon pokemon) {
        return new PokemonResponseDTO(
                pokemon.getId(),
                pokemon.getName(),
                pokemon.getHeight(),
                pokemon.getWeight(),
                pokemon.getTypes().stream()
                        .map(type -> type.getType().getName()) // Extrai o nome do tipo
                        .collect(Collectors.joining(", ")) // Junta os nomes com ", "
        );
    }
}