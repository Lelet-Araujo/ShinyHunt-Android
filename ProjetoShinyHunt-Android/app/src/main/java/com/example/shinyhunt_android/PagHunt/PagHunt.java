package com.example.shinyhunt_android.PagHunt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shinyhunt_android.PagInicio.PagInicio;  // Importe a classe PagInicio
import com.example.shinyhunt_android.R;
import com.google.firebase.Firebase;

public class PagHunt extends AppCompatActivity {

    private TextView textView2;
    private int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_hunt);

        textView2 = findViewById(R.id.textView2);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementCounter();
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementCounter();
            }
        });

        Button buttonReset = findViewById(R.id.button3);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCounter();
            }
        });

        // Configuração do botão Home para navegar para a página PagInicio
        Button buttonHome = findViewById(R.id.Home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crie um Intent para navegar para a próxima atividade (PagInicio)
                Intent intent = new Intent(PagHunt.this, PagInicio.class);

                // Inicie a próxima atividade
                startActivity(intent);
            }
        });
    }

    private void incrementCounter() {
        counter++;
        updateCounter();
    }

    private void decrementCounter() {
        counter--;
        updateCounter();
    }

    private void resetCounter() {
        counter = 0;
        updateCounter();
    }

    private void updateCounter() {
        textView2.setText(String.valueOf(counter));
    }
}
