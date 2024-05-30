package com.example.shinyhunt_android.PagInicio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shinyhunt_android.PagEscolherPokemon.PagEscolherPokemon;
import com.example.shinyhunt_android.R;

public class PagInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_inicio);

        Button buttonNovaHunt = findViewById(R.id.NovaHunt);

        buttonNovaHunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PagInicio.this,PagEscolherPokemon.class);
                startActivity(intent);
            }
        });
    }
}