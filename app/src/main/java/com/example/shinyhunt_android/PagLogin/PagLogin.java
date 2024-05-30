package com.example.shinyhunt_android.PagLogin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shinyhunt_android.PagInicio.PagInicio;
import com.example.shinyhunt_android.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PagLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_login);

        mAuth = FirebaseAuth.getInstance();

        // Verificar se o usuário já está logado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Se o usuário já estiver logado, vá para a MainActivity
            startActivity(new Intent(PagLogin.this, PagInicio.class));
            finish(); // Finaliza esta atividade para que o usuário não possa voltar pressionando o botão de voltar
        }

        ViewPager2 viewPager = findViewById(R.id.viewPager2);
        MyPageAdapter adapter = new MyPageAdapter(this);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Login");
                    } else if (position == 1) {
                        tab.setText("Registro");
                    }
                }
        ).attach();
    }
}
