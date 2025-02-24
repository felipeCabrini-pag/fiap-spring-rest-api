package br.com.fiap.service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PokemonRestDTO {

    private String name;

    private Sprites sprites;

    private List<TypeWrapper> types;

    public String getImageUrl() {
        return sprites != null ? sprites.getFrontDefault() : null;
    }

    public String geType() {
        return types != null && !types.isEmpty() ? types.get(0).getType().getName() : null;
    }


    @Data
    public static class Sprites {
        @JsonProperty("front_default")
        private String frontDefault;
    }

    @Data
    public static class TypeWrapper {
        private Type type;

        @Data
        public static class Type {
            private String name;
        }
    }
}
