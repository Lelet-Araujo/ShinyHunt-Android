package com.example.shinyhunt_android.FireBase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireBase {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    public FireBase() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public String getUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            return null;
        }
    }

    public void registerUser(String email, String password, final OnRegisterListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            User user = new User(firebaseUser.getEmail(), firebaseUser.getUid());
                            saveUserDataToFirestore(user, listener);
                        } else {
                            listener.onFailure(new Exception("FirebaseUser is null after registration."));
                        }
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    public void loginUser(String email, String password, final OnLoginListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        listener.onSuccess(user);
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    private void saveUserDataToFirestore(User user, final OnRegisterListener listener) {
        db.collection("users").document(user.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> listener.onSuccess(mAuth.getCurrentUser()))
                .addOnFailureListener(listener::onFailure);
    }

    public void savePokemonData(String userId, String pokemonId, String pokemonName, OnSavePokemonListener listener) {
        Map<String, Object> pokemonData = new HashMap<>();
        pokemonData.put("id", pokemonId);
        pokemonData.put("nome", pokemonName);

        db.collection("users").document(userId).collection("pokemons").document(pokemonId)
                .set(pokemonData)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(listener::onFailure);
    }

    public void saveHuntData(String userId, String pokemonId, String tempo, String contagem, String data, String hora,String statusHunt, final OnSaveHuntListener listener) {
        CollectionReference huntsRef = db.collection("users").document(userId)
                .collection("pokemons").document(pokemonId).collection("hunts");

        Map<String, Object> huntData = new HashMap<>();
        huntData.put("tempo", tempo);
        huntData.put("contagem", contagem);
        huntData.put("data", data);
        huntData.put("hora", hora);
        huntData.put("StatusHunt",statusHunt);

        huntsRef.add(huntData)
                .addOnSuccessListener(documentReference -> {
                    listener.onSuccess();
                    Log.d("Firebase", "Dados da caçada salvos com sucesso.");
                })
                .addOnFailureListener(e -> {
                    listener.onFailure(e);
                    Log.e("Firebase", "Falha ao salvar os dados da caçada: " + e.getMessage());
                });
    }

    public void fetchPausedHunts(final OnFetchPausedHuntsListener listener) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference pokemonsRef = db.collection("users").document(userId).collection("pokemons");

            pokemonsRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ArrayList<String> pausedHuntList = new ArrayList<>();
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
                                                pausedHuntList.add(displayText);
                                            }
                                        }
                                        listener.onSuccess(pausedHuntList);
                                    } else {
                                        Log.e(TAG, "Error getting paused hunts: ", huntTask.getException());
                                        listener.onFailure(huntTask.getException());
                                    }
                                });
                    }
                } else {
                    Log.e(TAG, "Error getting pokemons: ", task.getException());
                    listener.onFailure(task.getException());
                }
            });
        } else {
            Log.e(TAG, "User not logged in");
            listener.onFailure(new Exception("User not logged in"));
        }
    }

    public void getHuntData(String userId, String pokemonId, OnGetHuntListener listener) {
        CollectionReference huntsRef = db.collection("users").document(userId)
                .collection("pokemons").document(pokemonId).collection("hunts");

        huntsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            HuntPokemonDB huntData = new HuntPokemonDB();

            for (DocumentSnapshot document : queryDocumentSnapshots) {
                String tempo = document.getString("tempo");
                String contagem = document.getString("contagem");

                huntData.setTempo(tempo);
                huntData.setContagem(contagem);
            }

            listener.onSuccess(huntData);
        }).addOnFailureListener(listener::onFailure);
    }

    public interface OnGetHuntListener {
        void onSuccess(HuntPokemonDB huntData);
        void onFailure(Exception exception);
    }

    public interface OnFetchPausedHuntsListener {
        void onSuccess(ArrayList<String> pausedHuntList);
        void onFailure(Exception exception);
    }

    public interface OnRegisterListener {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception exception);
    }

    public interface OnLoginListener {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception exception);
    }

    public interface OnSavePokemonListener {
        void onSuccess();
        void onFailure(Exception exception);
    }

    public interface OnSaveHuntListener {
        void onSuccess();
        void onFailure(Exception exception);
    }
}