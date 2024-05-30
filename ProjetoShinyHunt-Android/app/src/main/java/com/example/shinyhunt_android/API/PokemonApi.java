package com.example.shinyhunt_android.API;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokemonApi {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/";
    private PokeApiService pokeApiService;

    public PokemonApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pokeApiService = retrofit.create(PokeApiService.class);
    }

    public PokeApiService getPokeApiService() {
        return pokeApiService;
    }

}
