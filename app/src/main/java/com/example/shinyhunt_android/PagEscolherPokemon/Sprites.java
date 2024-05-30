package com.example.shinyhunt_android.PagEscolherPokemon;

import com.google.gson.annotations.SerializedName;

public class Sprites {
    @SerializedName("front_shiny")
    private String frontShiny;

    @SerializedName("front_default") // Adicionando o URL do sprite do pokemon normal
    private String frontDefault;

    public String getFrontShiny() {
        return frontShiny;
    }

    public String getFrontDefault() {
        return frontDefault;
    }
}
