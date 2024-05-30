package com.example.shinyhunt_android.PagHunt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shinyhunt_android.PagInicio.PagInicio;
import com.example.shinyhunt_android.R;
import com.google.firebase.FirebaseApp;

public class PagHunt extends AppCompatActivity {

    private TextView textView2;
    private int counter = 0;
    private int increment = 1; // Valor inicial do incremento

    private TextView tvTimer;
    private Handler handler;
    private Runnable runnable;
    private int seconds = 0;
    private boolean isRunning = false;

    private EditText editIncrement;
    private Button button1, button2, buttonEdit;

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
        buttonEdit = findViewById(R.id.button6);

        // Exibir tempo inicial
        updateTimerDisplay();

        // Botão de Finish
        Button buttonFinish = findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishHunt();
            }
        });

        // Contador
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementCounter();
            }
        });

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

        // Botão Home
        ImageButton buttonHome = findViewById(R.id.Home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PagHunt.this, PagInicio.class);
                startActivity(intent);
            }
        });

        // Cronômetro
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    seconds++;
                    updateTimerDisplay(); // Atualizar exibição do tempo
                    handler.postDelayed(this, 1000);
                }
            }
        };

        // Botão Start
        Button btnStart = findViewById(R.id.button5);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    isRunning = true;
                    handler.post(runnable); // Iniciar cronômetro
                }
            }
        });

        // Botão Stop
        Button btnStop = findViewById(R.id.button4);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
            }
        });

        // Botão Edit
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editIncrement.getVisibility() == View.GONE) {
                    editIncrement.setVisibility(View.VISIBLE);
                } else {
                    updateIncrement();
                    editIncrement.setVisibility(View.GONE);
                }
            }
        });
    }

    // Método para atualizar a exibição do cronômetro
    private void updateTimerDisplay() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        String time = String.format("%02d H, %02d Min, %02d S", hours, minutes, secs);
        tvTimer.setText(time);
    }

    // Contagem
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

    // Atualizar incremento
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


    // Método para finalizar a hunt e retornar para a tela inicial
    private void finishHunt() {

        // Volta para a tela inicial
        Intent intent = new Intent(PagHunt.this, PagInicio.class);
        startActivity(intent);
        finish(); // Finaliza a atividade atual
    }
}
