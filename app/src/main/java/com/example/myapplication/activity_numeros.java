package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Random;

public class activity_numeros extends AppCompatActivity {

    private int numeroAtual;
    private TextView tvNumeroCentral;
    private EditText etAntecessor, etSucessor;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numeros2);

        // Inicialização
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        tvNumeroCentral = findViewById(R.id.tvNumeroCentral);
        etAntecessor = findViewById(R.id.etAntecessor);
        etSucessor = findViewById(R.id.etSucessor);
        Button btnVerificar = findViewById(R.id.btnVerificar);
        ImageView menuIcon = findViewById(R.id.menuIcon);

        // Menu Lateral
        String nome = getIntent().getStringExtra("nome");
        String sexo = getIntent().getStringExtra("sexo");
        setupNavigationHeader(nome, sexo);
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        gerarNovoDesafio();

        btnVerificar.setOnClickListener(v -> {
            validarResposta();
        });
    }

    private void gerarNovoDesafio() {
        Random random = new Random();
        // Gera um número entre 1 e 50 para o centro
        numeroAtual = random.nextInt(49) + 1;
        tvNumeroCentral.setText(String.valueOf(numeroAtual));

        // Limpa as respostas anteriores
        etAntecessor.setText("");
        etSucessor.setText("");
        etAntecessor.setBackgroundResource(R.drawable.card_numero);
        etSucessor.setBackgroundResource(R.drawable.card_numero);
    }

    private void validarResposta() {
        String respAntS = etAntecessor.getText().toString();
        String respSucS = etSucessor.getText().toString();

        if (respAntS.isEmpty() || respSucS.isEmpty()) {
            Toast.makeText(this, "Preencha os dois campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        int respAnt = Integer.parseInt(respAntS);
        int respSuc = Integer.parseInt(respSucS);

        if (respAnt == (numeroAtual - 1) && respSuc == (numeroAtual + 1)) {
            Toast.makeText(this, "Parabéns! Você acertou!", Toast.LENGTH_SHORT).show();
            // Feedback visual verde
            etAntecessor.setBackgroundColor(Color.parseColor("#C8E6C9"));
            etSucessor.setBackgroundColor(Color.parseColor("#C8E6C9"));

            // Espera um pouco e gera novo desafio
            tvNumeroCentral.postDelayed(this::gerarNovoDesafio, 1500);
        } else {
            Toast.makeText(this, "Ops! Tente novamente.", Toast.LENGTH_SHORT).show();
            // Feedback visual vermelho nos erros
            if (respAnt != (numeroAtual - 1)) etAntecessor.setBackgroundColor(Color.parseColor("#FFCDD2"));
            if (respSuc != (numeroAtual + 1)) etSucessor.setBackgroundColor(Color.parseColor("#FFCDD2"));
        }
    }

    private void setupNavigationHeader(String nome, String sexo) {
        if (navigationView != null && navigationView.getHeaderCount() > 0) {
            View headerView = navigationView.getHeaderView(0);
            TextView textViewNome = headerView.findViewById(R.id.textViewNomeUsuario);
            ImageView imagePerfil = headerView.findViewById(R.id.imagePerfil);
            if (nome != null && textViewNome != null) textViewNome.setText("Olá, " + nome + "!");
            if (sexo != null && imagePerfil != null) {
                imagePerfil.setImageResource(sexo.equalsIgnoreCase("Masculino") ? R.drawable.meny : R.drawable.menx);
            }
        }
    }
}