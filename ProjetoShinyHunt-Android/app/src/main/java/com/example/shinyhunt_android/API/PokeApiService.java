package com.example.shinyhunt_android.API;

import com.example.shinyhunt_android.PagEscolherPokemon.Pokemon;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokeApiService {
    @GET("pokemon/{id}/")
    Call<Pokemon> getPokemon(@Path("id") int id);

    @GET("pokemon?limit=807") // Ajuste o limite conforme necessário para a 7ª geração
    Call<PokemonResponse> getPokemonList();

    @GET("pokemon/{id}/")
    Call<PokemonResponse> getAllPokemon();





    /*@GET("pokemon?limit=10000") // Altere para um número grande que deve cobrir todos os Pokémon
    Call<PokemonResponse> getAllPokemonFromAllGenerations();*/

}
