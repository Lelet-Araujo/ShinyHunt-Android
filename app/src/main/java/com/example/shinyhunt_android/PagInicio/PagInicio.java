package com.example.shinyhunt_android.PagInicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton; // Importe a classe ImageButton

import androidx.appcompat.app.AppCompatActivity;

import com.example.shinyhunt_android.PagEscolherPokemon.PagEscolherPokemon;
import com.example.shinyhunt_android.PagLogin.PagLogin;
import com.example.shinyhunt_android.Pokedex.PokemonDex;
import com.example.shinyhunt_android.R;
import com.google.firebase.auth.FirebaseAuth;

public class PagInicio extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_inicio);

        mAuth = FirebaseAuth.getInstance();

        Button buttonNovaHunt = findViewById(R.id.NovaHunt);
        Button buttonPokedex = findViewById(R.id.Pokedex);
        ImageButton logoutButton = findViewById(R.id.Deslogar);


        //BotãoNovaHunt
        buttonNovaHunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PagInicio.this, PagEscolherPokemon.class);
                startActivity(intent);
            }
        });
        //BotãoNovaHunt

        //Botão Pokedex
        buttonPokedex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redireciona para outra página (PagOutraPagina)
                Intent intent = new Intent(PagInicio.this, PokemonDex.class);
                startActivity(intent);
            }
        });
        //Botão Pokedex




        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                // Após fazer logout, redirecione para a tela de login
                startActivity(new Intent(PagInicio.this, PagLogin.class));
                finish(); // Finaliza a atividade atual
            }
        });
    }
}
