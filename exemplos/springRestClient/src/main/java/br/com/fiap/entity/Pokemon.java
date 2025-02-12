package br.com.fiap.entity;

import br.com.fiap.controller.dto.PokemonTypeDTO;
import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "pokemons")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int height;
    private int weight;

    @Version
    private int version;

    @ElementCollection
    @CollectionTable(name = "pokemon_types", joinColumns = @JoinColumn(name = "pokemon_id"))
    private List<PokemonTypeDTO> types;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<PokemonTypeDTO> getTypes() {
        return types;
    }

    public void setTypes(List<PokemonTypeDTO> types) {
        this.types = types;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {

    }
}