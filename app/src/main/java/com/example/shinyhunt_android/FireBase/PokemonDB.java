package com.example.shinyhunt_android.FireBase;

public class PokemonDB {
    private String id;
    private String nome;

    public PokemonDB(){

    }

    public PokemonDB(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
