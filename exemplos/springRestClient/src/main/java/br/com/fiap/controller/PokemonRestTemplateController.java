package br.com.fiap.controller;

import br.com.fiap.controller.dto.PokemonRequestDTO;
import br.com.fiap.controller.dto.PokemonResponseDTO;
import br.com.fiap.service.PokemonRestTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pokemons/resttemplate")
public class PokemonRestTemplateController {

    private final PokemonRestTemplateService pokemonService;

    public PokemonRestTemplateController(PokemonRestTemplateService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @PostMapping
    public ResponseEntity<PokemonResponseDTO> capturePokemon(@RequestBody PokemonRequestDTO request) {
        PokemonResponseDTO response = pokemonService.captureAndSavePokemon(request.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PokemonResponseDTO>> listCapturedPokemons() {
        return ResponseEntity.ok(pokemonService.getAllCapturedPokemons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PokemonResponseDTO> getPokemonById(@PathVariable Long id) {
        return ResponseEntity.ok(pokemonService.getPokemonById(id));
    }
}