package com.example.shinyhunt_android.PagEscolherPokemon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.firebase.FirebaseApp;
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
    private Pokemon selectedPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_escolher_pokemon);
        FirebaseApp.initializeApp(this);

        imageView = findViewById(R.id.imageView2);
        nomePokemonTextView = findViewById(R.id.NomePokemon);
        listaPokemon = findViewById(R.id.listaPokemon);
        searchView = findViewById(R.id.searchView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaPokemon.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pokeApiService = retrofit.create(PokeApiService.class);

        setupSearchView();

        loadPokemonList();



        ImageButton homeButton = findViewById(R.id.Home);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PagEscolherPokemon.this, PagInicio.class);
                startActivity(intent);
                finish();
            }
        });

        Button startButton = findViewById(R.id.Start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPokemon != null) { // Verifica se um Pokémon foi selecionado
                    // Salva o ID do Pokémon selecionado em um Intent e inicia a próxima atividade
                    Intent intent = new Intent(PagEscolherPokemon.this, PagHunt.class);
                    intent.putExtra("selected_pokemon_id", selectedPokemon.getId());
                    startActivity(intent);

                } else {
                    Toast.makeText(PagEscolherPokemon.this, "Selecione um Pokémon primeiro", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configuração do clique na lista de Pokémon
        listaPokemon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pokemon selectedPokemon = (Pokemon) parent.getItemAtPosition(position);

                if (selectedPokemon != null) {
                    selectPokemon(selectedPokemon);
                }
            }
        });
    }

    // Método para configurar a barra de pesquisa
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

    // Método para filtrar os Pokémon na lista de acordo com o texto da barra de pesquisa
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






    // Método para carregar a lista de Pokémon
    private void loadPokemonList() {
        Call<PokemonResponse> call = pokeApiService.getPokemonListLimit();
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
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Toast.makeText(PagEscolherPokemon.this, "Erro ao obter lista de Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para extrair o ID do Pokémon a partir da URL
    private int extractPokemonIdFromUrl(String url) {
        String[] urlParts = url.split("/");
        return Integer.parseInt(urlParts[urlParts.length - 1]);
    }









    // Método para carregar a imagem do Pokémon
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

    /// Método para atualizar o TextView com o nome do Pokémon
    private void updateNomePokemonTextView(String nomePokemon) {
        String capitalizedFirstName = nomePokemon.substring(0, 1).toUpperCase() + nomePokemon.substring(1);
        nomePokemonTextView.setText(capitalizedFirstName);
    }









    // Método para ser chamado quando um Pokémon for selecionado na lista
    private void selectPokemon(Pokemon pokemon) {
        selectedPokemon = pokemon; // Armazena o Pokémon selecionado
        if (selectedPokemon != null) {
            loadShinyPokemonImage(selectedPokemon.getId());
            updateNomePokemonTextView(selectedPokemon.getName());
        }
    }
}

