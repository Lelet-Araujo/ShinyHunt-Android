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

import com.example.shinyhunt_android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registro extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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

            if (TextUtils.isEmpty(email) ||  TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Registro do usuário com Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            // Registro bem-sucedido
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getContext(), "Registro realizado com sucesso para " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            // Você pode decidir o que fazer após o registro bem-sucedido, como navegar para outra página
                        } else {
                            // Registro falhou
                            Toast.makeText(getContext(), "Falha ao registrar: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return rootView;
    }
}
