package com.example.shinyhunt_android.Pokedex;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shinyhunt_android.API.PokeApiService;
import com.example.shinyhunt_android.API.PokemonResponse;
import com.example.shinyhunt_android.PagEscolherPokemon.Pokemon;
import com.example.shinyhunt_android.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Pokedex extends AppCompatActivity {

    private PokeApiService pokeApiService;
    private ListView listaPokemon;
    private ArrayAdapter<Pokemon> adapter;
    private List<Pokemon> allPokemonList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);
        listaPokemon = findViewById(R.id.lista);
        searchView = findViewById(R.id.pesquisa);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaPokemon.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pokeApiService = retrofit.create(PokeApiService.class);

        setupSearchView();

        loadPokemonList();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPokemon(newText);
                return true;
            }
        });
    }

    private void filterPokemon(String query) {
        List<Pokemon> filteredList = new ArrayList<>();

        for (Pokemon pokemon : allPokemonList) {
            if (pokemon.getName().toLowerCase().contains(query.toLowerCase()) ||
                    String.valueOf(pokemon.getId()).contains(query)) {
                filteredList.add(pokemon);
            }
        }

        adapter.clear();
        adapter.addAll(filteredList);
    }

    private void loadPokemonList() {
        Call<PokemonResponse> call = pokeApiService.getPokemonList();
        call.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pokemon> pokemonList = response.body().getResults();
                    allPokemonList = new ArrayList<>(pokemonList);

                    for (Pokemon pokemon : pokemonList) {
                        int pokemonId = extractPokemonIdFromUrl(pokemon.getUrl());
                        pokemon.setId(pokemonId);
                    }
                    adapter.addAll(pokemonList);
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Toast.makeText(Pokedex.this, "Erro ao obter lista de Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int extractPokemonIdFromUrl(String url) {
        String[] urlParts = url.split("/");
        return Integer.parseInt(urlParts[urlParts.length - 1]);
    }
}
