package com.example.shinyhunt_android.PagLogin;

import android.content.Intent;
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
import com.example.shinyhunt_android.PagInicio.PagInicio;
import com.example.shinyhunt_android.R;
import com.google.firebase.auth.FirebaseUser;

public class Login extends Fragment {

    private FireBase fireBase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireBase = new FireBase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        EditText emailUsernameField = rootView.findViewById(R.id.editTextTextEmailAddress3);
        EditText passwordField = rootView.findViewById(R.id.editTextTextPassword);
        Button loginButton = rootView.findViewById(R.id.Login);

        loginButton.setOnClickListener(view -> {
            String emailUsername = emailUsernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (TextUtils.isEmpty(emailUsername) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            fireBase.loginUser(emailUsername, password, new FireBase.OnLoginListener() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    Toast.makeText(getContext(), "Login realizado com sucesso para " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), PagInicio.class);
                    startActivity(intent);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getContext(), "Falha ao fazer login: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return rootView;
    }
}
//igoreduardodev@gmail.com