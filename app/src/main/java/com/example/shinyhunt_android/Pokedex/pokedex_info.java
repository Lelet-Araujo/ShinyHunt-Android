package com.example.shinyhunt_android.Pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shinyhunt_android.API.PokeApiService;
import com.example.shinyhunt_android.PagEscolherPokemon.Pokemon;
import com.example.shinyhunt_android.PagEscolherPokemon.PokemonType;
import com.example.shinyhunt_android.PagInicio.PagInicio;
import com.example.shinyhunt_android.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class pokedex_info extends AppCompatActivity {

    private ImageView imageView;
    private PokeApiService pokeApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex_info);

        imageView = findViewById(R.id.imageView_Dex_Info);

        ImageButton buttonBackDex = findViewById(R.id.VoltarDex);
        buttonBackDex.setOnClickListener(v -> onBackPressed());

        ImageButton HomeDex = findViewById(R.id.HomeDex);
        HomeDex.setOnClickListener(v -> {
            Intent intent = new Intent(pokedex_info.this, PagInicio.class);
            startActivity(intent);
            finish();
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pokeApiService = retrofit.create(PokeApiService.class);

        // Obter o ID do Pokémon selecionado do Intent
        int selectedPokemonId = getIntent().getIntExtra("selected_pokemon_id", -1);
        if (selectedPokemonId != -1) {
            loadPokemonInfo(selectedPokemonId); // Carregar informações do Pokémon selecionado
        } else {
            Toast.makeText(this, "ID do Pokémon não encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para carregar a imagem e informações do Pokémon
    private void loadPokemonInfo(int pokemonId) {
        Call<Pokemon> call = pokeApiService.getPokemon(pokemonId);
        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(@NonNull Call<Pokemon> call, @NonNull Response<Pokemon> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pokemon pokemon = response.body();

                    // Carrega o sprite padrão
                    String defaultUrl = pokemon.getSprites().getFrontDefault();
                    Picasso.get().load(defaultUrl).resize(320, 320).into(imageView);

                    // Exibir o nome do Pokémon
                    TextView nameTextView = findViewById(R.id.nome_info_Dex);
                    String capitalizedFirstName = pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1);
                    nameTextView.setText(capitalizedFirstName);

                    // Exibir o número do Pokémon
                    TextView numberTextView = findViewById(R.id.num_info_poke);
                    numberTextView.setText("#" + pokemon.getId());


                    // Exibir os tipos do Pokémon
                    TextView typeTextView = findViewById(R.id.Tipo_Poke_Info);
                    StringBuilder types = new StringBuilder();
                    for (PokemonType type : pokemon.getTypes()) {
                        types.append(type.getName()).append("  ");
                    }
                    typeTextView.setText(types.toString());

                    // Obter a descrição do Pokémon
                    getPokemonDescription(pokemonId);
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Toast.makeText(pokedex_info.this, "Erro ao obter dados do Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obter a descrição do Pokémon
    private void getPokemonDescription(int pokemonId) {
        Call<PokemonSpecies> call = pokeApiService.getPokemonSpecies(pokemonId);
        call.enqueue(new Callback<PokemonSpecies>() {
            @Override
            public void onResponse(Call<PokemonSpecies> call, Response<PokemonSpecies> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonSpecies pokemonSpecies = response.body();
                    List<PokemonSpecies.FlavorTextEntry> flavorTextEntries = pokemonSpecies.getFlavorTextEntries();
                    for (PokemonSpecies.FlavorTextEntry entry : flavorTextEntries) {
                        // Aqui você pode filtrar a descrição com base no idioma, se necessário
                        if (entry.getLanguage().getName().equals("en")) { // Filtrando para descrição em inglês
                            // Exibir a descrição do Pokémon
                            TextView descriptionTextView = findViewById(R.id.Menor);
                            descriptionTextView.setText(entry.getFlavorText());
                            break; // Interrompe após encontrar a primeira descrição em inglês
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PokemonSpecies> call, Throwable t) {
                Toast.makeText(pokedex_info.this, "Erro ao obter descrição do Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
