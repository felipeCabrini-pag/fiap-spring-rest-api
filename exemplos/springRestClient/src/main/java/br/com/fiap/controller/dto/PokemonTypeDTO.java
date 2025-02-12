package br.com.fiap.controller.dto;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class PokemonTypeDTO {

    private int slot;

    @Embedded
    private TypeDetail type;

    // Getters e Setters
    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public TypeDetail getType() {
        return type;
    }

    public void setType(TypeDetail type) {
        this.type = type;
    }

    @Embeddable
    public static class TypeDetail {

        private String name;
        private String url;

        // Getters e Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}