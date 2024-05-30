package com.example.shinyhunt_android.API;

import com.example.shinyhunt_android.PagEscolherPokemon.Pokemon;
import com.example.shinyhunt_android.Pokedex.PokemonSpecies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokeApiService {
    @GET("pokemon/{id}/")
    Call<Pokemon> getPokemon(@Path("id") int id);


    @GET("pokemon?limit=2000")
    Call<PokemonResponse> getPokemonListLimit();

    @GET("pokemon-species/{id}/")
    Call<PokemonSpecies> getPokemonSpecies(@Path("id") int id);



}
