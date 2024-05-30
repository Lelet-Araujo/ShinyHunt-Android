package com.example.shinyhunt_android.PagEscolherPokemon;

import java.util.List;

public class Pokemon {
    private int id;
    private String name;
    private Sprites sprites;
    private String url;
    private List<PokemonType> types; // Incluído para armazenar os tipos do Pokémon

    public Pokemon(String name, int id, Sprites sprites, String url, List<PokemonType> types) {
        this.name = name;
        this.id = id;
        this.sprites = sprites;
        this.url = url;
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public String getUrl() {
        return url;
    }

    public List<PokemonType> getTypes() {
        return types;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "#" + id + " - " + name;
    }

    private List<Pokemon> results;

    public List<Pokemon> getResults() {
        return results;
    }
}
