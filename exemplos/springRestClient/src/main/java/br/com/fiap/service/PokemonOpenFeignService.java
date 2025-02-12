package br.com.fiap.service;


import br.com.fiap.controller.dto.PokemonResponseDTO;
import br.com.fiap.entity.Pokemon;
import br.com.fiap.repository.PokemonRepository;
import br.com.fiap.service.client.PokemonFeignClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonOpenFeignService {

    private final PokemonFeignClient pokemonClient;
    private final PokemonRepository pokemonRepository;

    public PokemonOpenFeignService(PokemonFeignClient pokemonClient, PokemonRepository pokemonRepository) {
        this.pokemonClient = pokemonClient;
        this.pokemonRepository = pokemonRepository;
    }

    public PokemonResponseDTO captureAndSavePokemon(String name) {
        // Chama a API usando o Feign Client
        Pokemon apiPokemon = pokemonClient.getPokemonByName(name);

        if (apiPokemon == null) {
            throw new RuntimeException("Pokémon não encontrado: " + name);
        }

        // Salva o Pokémon no banco
        apiPokemon.setId(null);
        Pokemon savedPokemon = pokemonRepository.save(apiPokemon);

        // Converte a entidade para DTO de resposta
        return convertToResponse(savedPokemon);
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