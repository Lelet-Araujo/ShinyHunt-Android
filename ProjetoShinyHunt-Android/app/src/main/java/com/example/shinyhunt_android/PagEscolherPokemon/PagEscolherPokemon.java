package com.example.shinyhunt_android.PagEscolherPokemon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shinyhunt_android.API.PokeApiService;
import com.example.shinyhunt_android.API.PokemonResponse;

import com.example.shinyhunt_android.PagHunt.PagHunt;
import com.example.shinyhunt_android.PagInicio.PagInicio;

import com.example.shinyhunt_android.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PagEscolherPokemon extends AppCompatActivity {

    private ImageView imageView;
    private TextView nomePokemonTextView;
    private PokeApiService pokeApiService;
    private ListView listaPokemon;
    private ArrayAdapter<Pokemon> adapter;
    private List<Pokemon> allPokemonList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_escolher_pokemon);
        imageView = findViewById(R.id.imageView2);
        nomePokemonTextView = findViewById(R.id.NomePokemon);
        listaPokemon = findViewById(R.id.listaPokemon);
        searchView = findViewById(R.id.searchView);  // Inicializando a SearchView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaPokemon.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pokeApiService = retrofit.create(PokeApiService.class);

        setupSearchView();

        loadPokemonList();




        Button homeButton = findViewById(R.id.Home);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criar um Intent para iniciar a PagInicioActivity
                Intent intent = new Intent(PagEscolherPokemon.this, PagInicio.class);
                startActivity(intent);
                finish();
            }
        });

        Button startButton = findViewById(R.id.Start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PagEscolherPokemon.this, PagHunt.class);
                startActivity(intent);

            }
        });
    }



    //Barra de pesquisa
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
    //Barra de pesquisa]


    //Lista
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

                    if (!pokemonList.isEmpty()) {
                        Pokemon firstPokemon = pokemonList.get(0);
                        loadShinyPokemonImage(firstPokemon.getId());
                        updateNomePokemonTextView(firstPokemon.getName());
                    }

                    listaPokemon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Pokemon selectedPokemon = (Pokemon) parent.getItemAtPosition(position);

                            if (selectedPokemon != null) {
                                loadShinyPokemonImage(selectedPokemon.getId());
                                updateNomePokemonTextView(selectedPokemon.getName());
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Toast.makeText(PagEscolherPokemon.this, "Erro ao obter lista de Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private int extractPokemonIdFromUrl(String url) {
        String[] urlParts = url.split("/");
        return Integer.parseInt(urlParts[urlParts.length - 1]);
    }
    //Lista


    //Imagem
    private void loadShinyPokemonImage(int pokemonId) {
        Call<Pokemon> call = pokeApiService.getPokemon(pokemonId);
        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(@NonNull Call<Pokemon> call, @NonNull Response<Pokemon> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String shinyUrl = response.body().getSprites().getFrontShiny();
                    Picasso.get().load(shinyUrl).resize(320, 320).into(imageView);
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Toast.makeText(PagEscolherPokemon.this, "Erro ao obter dados do Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateNomePokemonTextView(String nomePokemon) {
        nomePokemonTextView.setText(nomePokemon);
    }
    //Imagem







}
