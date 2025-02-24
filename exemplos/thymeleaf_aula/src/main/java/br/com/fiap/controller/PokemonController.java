package br.com.fiap.controller;

import br.com.fiap.controller.dto.PokemonDTO;
import br.com.fiap.service.PokemonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pokemon")
public class PokemonController {

    private PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @PostMapping("/capture")
    public String capturePokemon(
            RedirectAttributes redirectAttributes) {

        PokemonDTO capturedPokemon = pokemonService.captureRandomPokemon();
        redirectAttributes.addFlashAttribute("capturedPokemon", capturedPokemon);
        return "redirect:/";
    }
}
