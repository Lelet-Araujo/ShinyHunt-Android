package com.example.shinyhunt_android.Ranking;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shinyhunt_android.FireBase.FireBase;
import com.example.shinyhunt_android.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Ranking_Pag extends AppCompatActivity {

    private TextView textViewMaiorContagem;
    private TextView textViewMaiorPokemon;
    private TextView textViewMenorContagem;
    private TextView textViewMenorPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_pag);
        FirebaseApp.initializeApp(this);

        textViewMaiorContagem = findViewById(R.id.Maior);
        textViewMaiorPokemon = findViewById(R.id.Nomemaior);
        textViewMenorContagem = findViewById(R.id.Menor);
        textViewMenorPokemon = findViewById(R.id.Nomemenor);

        fetchContagens();


        ImageButton buttonBack = findViewById(R.id.imageButton);
        buttonBack.setOnClickListener(v -> onBackPressed());
    }

    private void fetchContagens() {
        FireBase firebase = new FireBase();
        String userId = firebase.getUserId();

        if (userId != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            final int[] maiorContagem = {Integer.MIN_VALUE};
            final int[] menorContagem = {Integer.MAX_VALUE};
            final String[] maiorPokemon = {""};
            final String[] menorPokemon = {""};
            final boolean[] hasContagens = {false};

            db.collection("users").document(userId).collection("pokemons")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String pokemonId = document.getId();
                                String pokemonName = document.getString("nome");

                                db.collection("users").document(userId).collection("pokemons")
                                        .document(pokemonId).collection("hunts")
                                        .get()
                                        .addOnCompleteListener(huntTask -> {
                                            if (huntTask.isSuccessful()) {
                                                for (QueryDocumentSnapshot huntDocument : huntTask.getResult()) {
                                                    String contagemStr = huntDocument.getString("contagem");
                                                    if (contagemStr != null && !contagemStr.isEmpty()) {
                                                        int contagem = Integer.parseInt(contagemStr);
                                                        if (contagem > maiorContagem[0]) {
                                                            maiorContagem[0] = contagem;
                                                            maiorPokemon[0] = pokemonName;
                                                        }
                                                        if (contagem < menorContagem[0]) {
                                                            menorContagem[0] = contagem;
                                                            menorPokemon[0] = pokemonName;
                                                        }
                                                        hasContagens[0] = true;
                                                    }
                                                }

                                                if (hasContagens[0]) {
                                                    textViewMaiorContagem.setText("" + maiorContagem[0]);
                                                    textViewMaiorPokemon.setText(""+maiorPokemon[0]);
                                                    textViewMenorContagem.setText("" + menorContagem[0]);
                                                    textViewMenorPokemon.setText("" + menorPokemon[0]);
                                                } else {
                                                    textViewMaiorContagem.setText("Nenhuma contagem encontrada");
                                                    textViewMenorContagem.setText("Nenhuma contagem encontrada");
                                                    textViewMaiorPokemon.setText("");
                                                    textViewMenorPokemon.setText("");
                                                }
                                            } else {
                                                Toast.makeText(Ranking_Pag.this, "Erro ao recuperar dados de caça.", Toast.LENGTH_SHORT).show();
                                                Log.e("Ranking_Pag", "Erro ao recuperar dados de caça: ", huntTask.getException());
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(Ranking_Pag.this, "Erro ao recuperar dados.", Toast.LENGTH_SHORT).show();
                            Log.e("Ranking_Pag", "Erro ao recuperar dados: ", task.getException());
                        }
                    });
        } else {
            Log.e("Ranking_Pag", "ID do usuário é nulo");
        }
    }
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}