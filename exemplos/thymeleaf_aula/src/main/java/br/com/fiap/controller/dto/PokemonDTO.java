package br.com.fiap.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PokemonDTO {

    private String name;

    private int level;

    private String type;

    private String imageUrl;
}
