package com.example.shinyhunt_android.Pokedex;

import com.example.shinyhunt_android.PagEscolherPokemon.Pokemon;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PokemonSpecies {

    @SerializedName("flavor_text_entries")
    private List<FlavorTextEntry> flavorTextEntries;

    @SerializedName("evolves_from_species")
    private EvolutionInfo evolutionInfo;

    public List<FlavorTextEntry> getFlavorTextEntries() {
        return flavorTextEntries;
    }

    public EvolutionInfo getEvolutionInfo() {
        return evolutionInfo;
    }


    private List<Pokemon> results;

    public List<Pokemon> getResults() {
        return results;
    }
    public static class FlavorTextEntry {
        @SerializedName("flavor_text")
        private String flavorText;

        @SerializedName("language")
        private Language language;

        public String getFlavorText() {
            return flavorText;
        }

        public Language getLanguage() {
            return language;
        }
    }

    public static class Language {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }
    }

    public static class EvolutionInfo {
        @SerializedName("name")
        private String name;

        @SerializedName("level")
        private int level;

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }
    }
}
