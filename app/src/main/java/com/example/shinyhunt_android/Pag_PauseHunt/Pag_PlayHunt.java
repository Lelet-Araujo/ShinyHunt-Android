package com.example.shinyhunt_android.Pag_PauseHunt;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shinyhunt_android.FireBase.FireBase;
import com.example.shinyhunt_android.R;

import java.util.ArrayList;

public class Pag_PlayHunt extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private final ArrayList<String> huntList = new ArrayList<>();
    private static final String TAG = "Pag_PlayHunt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_play_hunt);
        ListView listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, huntList);
        listView.setAdapter(adapter);

        fetchPausedHuntsFromFirebase();

        ImageButton buttonBack = findViewById(R.id.imageButtonHome2);
        buttonBack.setOnClickListener(v -> onBackPressed());
    }

    private void fetchPausedHuntsFromFirebase() {
        FireBase firebase = new FireBase();
        firebase.fetchPausedHunts(new FireBase.OnFetchPausedHuntsListener() {
            @Override
            public void onSuccess(ArrayList<String> pausedHuntList) {
                adapter.clear();
                adapter.addAll(pausedHuntList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "Error fetching paused hunts: ", exception);
                Toast.makeText(Pag_PlayHunt.this, "Error fetching paused hunts", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
