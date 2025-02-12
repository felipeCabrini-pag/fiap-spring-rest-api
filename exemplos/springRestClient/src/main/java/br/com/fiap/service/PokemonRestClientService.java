package br.com.fiap.service;



import br.com.fiap.controller.dto.PokemonResponseDTO;
import br.com.fiap.entity.Pokemon;
import br.com.fiap.repository.PokemonRepository;
import br.com.fiap.service.client.PokemonRestClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonRestClientService {

    private final PokemonRestClient pokemonClient;
    private final PokemonRepository pokemonRepository;

    public PokemonRestClientService(PokemonRestClient pokemonClient, PokemonRepository pokemonRepository) {
        this.pokemonClient = pokemonClient;
        this.pokemonRepository = pokemonRepository;
    }

    public PokemonResponseDTO captureAndSavePokemon(String name) {
        // Faz a requisição HTTP usando o cliente REST nativo
        Pokemon apiPokemon = pokemonClient.getPokemonByName(name);

        if (apiPokemon == null) {
            throw new RuntimeException("Erro ao buscar o Pokémon: " + name);
        }

        // Salva no banco de dados
        apiPokemon.setId(null);
        Pokemon savedPokemon = pokemonRepository.save(apiPokemon);

        // Converte a entidade para o DTO de resposta
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