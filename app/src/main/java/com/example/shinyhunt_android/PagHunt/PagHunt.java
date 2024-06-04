package com.example.shinyhunt_android.PagHunt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shinyhunt_android.FireBase.FireBase;
import com.example.shinyhunt_android.PagInicio.PagInicio;
import com.example.shinyhunt_android.R;
import com.google.firebase.FirebaseApp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PagHunt extends AppCompatActivity {
    private TextView textView2;
    private int counter = 0;
    private int increment = 1;
    private TextView tvTimer;
    private Handler handler;
    private Runnable runnable;
    private int seconds = 0;
    private boolean isRunning = false;
    private EditText editIncrement;
    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_hunt);
        FirebaseApp.initializeApp(this);

        textView2 = findViewById(R.id.textView2);
        tvTimer = findViewById(R.id.timer);
        editIncrement = findViewById(R.id.editIncrement);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        Button buttonEdit = findViewById(R.id.button6);

        updateTimerDisplay();

        Button buttonFinish = findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(v -> finishHunt());

        Button buttonPausehunt = findViewById(R.id.buttonPausehunt);
        buttonPausehunt.setOnClickListener(v -> pauseHunt());




        button1.setOnClickListener(v -> incrementCounter());
        button2.setOnClickListener(v -> decrementCounter());

        Button buttonReset = findViewById(R.id.button3);
        buttonReset.setOnClickListener(v -> resetCounter());

        ImageButton buttonHome = findViewById(R.id.Home);
        buttonHome.setOnClickListener(v -> {
            Intent intent = new Intent(PagHunt.this, PagInicio.class);
            startActivity(intent);
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    seconds++;
                    updateTimerDisplay();
                    handler.postDelayed(this, 1000);
                }
            }
        };

        Button btnStart = findViewById(R.id.button5);
        btnStart.setOnClickListener(v -> {
            if (!isRunning) {
                isRunning = true;
                handler.post(runnable);
            }
        });

        Button btnStop = findViewById(R.id.button4);
        btnStop.setOnClickListener(v -> isRunning = false);

        buttonEdit.setOnClickListener(v -> {
            if (editIncrement.getVisibility() == View.GONE) {
                editIncrement.setVisibility(View.VISIBLE);
            } else {
                updateIncrement();
                editIncrement.setVisibility(View.GONE);
            }
        });
    }

    private void updateTimerDisplay() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        @SuppressLint("DefaultLocale") String time = String.format("%02d H, %02d Min, %02d S", hours, minutes, secs);
        tvTimer.setText(time);
    }

    private void incrementCounter() {
        counter += increment;
        updateCounter();
    }

    private void decrementCounter() {
        counter -= increment;
        updateCounter();
    }

    private void resetCounter() {
        counter = 0;
        updateCounter();
    }

    private void updateCounter() {
        textView2.setText(String.valueOf(counter));
    }

    @SuppressLint("SetTextI18n")
    private void updateIncrement() {
        String incrementText = editIncrement.getText().toString();
        if (!incrementText.isEmpty()) {
            increment = Integer.parseInt(incrementText);
            Toast.makeText(this, "Incremento atualizado para " + increment, Toast.LENGTH_SHORT).show();
            button1.setText("+" + increment);
            button2.setText("-" + increment);
        } else {
            Toast.makeText(this, "Por favor, insira um valor válido.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String DataAtual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        return dateFormat.format(data);
    }

    @SuppressLint("SimpleDateFormat")
    private String HoraAtual() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date data = new Date();
        return timeFormat.format(data);
    }


    private void pauseHunt() {
        FireBase firebase = new FireBase();
        String userId = firebase.getUserId();
        String pokemonId = getIntent().getStringExtra("selected_pokemon_id");


        if (userId != null && pokemonId != null) {
            String dataAtual = DataAtual();
            String horaAtual = HoraAtual();
            String tempo = tvTimer.getText().toString();
            String contagem = textView2.getText().toString();
            String statusHunt = "pausada"; // Definindo statusHunt como "pausada"

            firebase.saveHuntData(userId, pokemonId, tempo, contagem, dataAtual, horaAtual, statusHunt, new FireBase.OnSaveHuntListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(PagHunt.this, "Dados da caçada salvos com sucesso.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PagHunt.this, PagInicio.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(PagHunt.this, "Erro ao salvar os dados da caçada.", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Falha ao salvar os dados da caçada: " + exception.getMessage());
                }
            });
        } else {
            // Aqui você pode lidar com o caso em que o ID do usuário ou do Pokémon é nulo
            Log.e("PagHunt", "ID do usuário ou do Pokémon é nulo");
        }
    }

    private void finishHunt() {
        FireBase firebase = new FireBase();
        String userId = firebase.getUserId();
        String pokemonId = getIntent().getStringExtra("selected_pokemon_id");


        if (userId != null && pokemonId != null) {
            String dataAtual = DataAtual();
            String horaAtual = HoraAtual();
            String tempo = tvTimer.getText().toString();
            String contagem = textView2.getText().toString();
            String statusHunt = "finalizada"; // Definindo statusHunt como "finalizada"

            firebase.saveHuntData(userId, pokemonId, tempo, contagem, dataAtual, horaAtual, statusHunt, new FireBase.OnSaveHuntListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(PagHunt.this, "Dados da caçada salvos com sucesso.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PagHunt.this, PagInicio.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(PagHunt.this, "Erro ao salvar os dados da caçada.", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Falha ao salvar os dados da caçada: " + exception.getMessage());
                }
            });
        } else {
            // Aqui você pode lidar com o caso em que o ID do usuário ou do Pokémon é nulo
            Log.e("PagHunt", "ID do usuário ou do Pokémon é nulo");
        }
    }
}
