package com.example.shinyhunt_android.Pag_PauseHunt;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shinyhunt_android.FireBase.HuntPokemonDB;
import com.example.shinyhunt_android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Pag_PlayHunt extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> huntList = new ArrayList<>();
    private static final String TAG = "Pag_PlayHunt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_play_hunt);
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, huntList);
        listView.setAdapter(adapter);

        fetchPausedHunts();

        ImageButton buttonBack = findViewById(R.id.imageButtonHome2);
        buttonBack.setOnClickListener(v -> onBackPressed());
    }

    private void fetchPausedHunts() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference pokemonsRef = db.collection("users").document(userId).collection("pokemons");

            pokemonsRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    huntList.clear();
                    for (QueryDocumentSnapshot pokemonDocument : task.getResult()) {
                        String pokemonName = pokemonDocument.getString("nome");
                        CollectionReference huntsRef = pokemonDocument.getReference().collection("hunts");
                        huntsRef.whereEqualTo("StatusHunt", "pausada")
                                .get()
                                .addOnCompleteListener(huntTask -> {
                                    if (huntTask.isSuccessful()) {
                                        for (DocumentSnapshot huntDocument : huntTask.getResult()) {
                                            HuntPokemonDB hunt = huntDocument.toObject(HuntPokemonDB.class);
                                            if (hunt != null) {
                                                String displayText = " " + pokemonName + "  -  " + hunt.getContagem();
                                                huntList.add(displayText);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Log.e(TAG, "Error getting hunts: ", huntTask.getException());
                                        Toast.makeText(Pag_PlayHunt.this, "Error getting hunts", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Log.e(TAG, "Error getting pokemons: ", task.getException());
                    Toast.makeText(Pag_PlayHunt.this, "Error getting pokemons", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "User not logged in");
            Toast.makeText(Pag_PlayHunt.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
