package com.example.shinyhunt_android.PagEscolherPokemon;

public class Pokemon {
    private int id;
    private String name;
    private Sprites sprites;
    private String url;

    public Sprites getSprites() {
        return sprites;
    }

    public Pokemon(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }



    @Override
    public String toString() {
        return "#"+ id  + " - " + name;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }
}
