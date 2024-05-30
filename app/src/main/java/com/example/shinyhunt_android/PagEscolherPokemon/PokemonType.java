package com.example.shinyhunt_android.PagEscolherPokemon;

public class PokemonType {
    private String name;

    public PokemonType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
