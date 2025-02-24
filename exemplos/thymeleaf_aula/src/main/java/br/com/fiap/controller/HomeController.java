package br.com.fiap.controller;


import br.com.fiap.controller.dto.PokemonDTO;
import br.com.fiap.service.PokemonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private PokemonService pokemonService;

    public HomeController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<PokemonDTO> pokemonDTOS = pokemonService.getAllPokemons();
        model.addAttribute("pokemons",pokemonDTOS);
        model.addAttribute("rawHtml","<h2>Conteúdo HTML não escapado</h2>");
        return "home";
    }
}
