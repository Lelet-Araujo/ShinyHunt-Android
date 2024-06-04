package com.example.shinyhunt_android.PagLogin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shinyhunt_android.FireBase.FireBase;
import com.example.shinyhunt_android.R;
import com.google.firebase.auth.FirebaseUser;

public class Registro extends Fragment {

    private FireBase fireBase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireBase = new FireBase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registro, container, false);

        EditText emailField = rootView.findViewById(R.id.Email);
        EditText passwordField = rootView.findViewById(R.id.Senha);
        Button registerButton = rootView.findViewById(R.id.Registro);

        registerButton.setOnClickListener(view -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            fireBase.registerUser(email, password, new FireBase.OnRegisterListener() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    Toast.makeText(getContext(), "Registro realizado com sucesso para " + user.getEmail(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getContext(), "Falha ao registrar: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return rootView;
    }
}
