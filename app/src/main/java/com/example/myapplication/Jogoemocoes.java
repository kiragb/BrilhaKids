package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Jogoemocoes extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPreferences preferences;
    private CheckBox checkboxConcluido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogoemocoes);

        // 1. REFERÊNCIAS
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        ImageView menuIcon = findViewById(R.id.menuIcon);
        checkboxConcluido = findViewById(R.id.checkboxConcluidoEmocoes);

        // Inicializa SharedPreferences
        preferences = getSharedPreferences("BrilhaKidsPrefs", Context.MODE_PRIVATE);
        navigationView.setItemIconTintList(null);

        // 2. DADOS DO USUÁRIO
        String nome = getIntent().getStringExtra("nome");
        String sexo = getIntent().getStringExtra("sexo");

        // Configurar Header do Menu
        View headerView = navigationView.getHeaderView(0);
        TextView tvNome = headerView.findViewById(R.id.textViewNomeUsuario);
        ImageView ivPerfil = headerView.findViewById(R.id.imagePerfil);

        if (nome != null) tvNome.setText("Olá, " + nome + "!");
        if (sexo != null && ivPerfil != null) {
            ivAvatarMenu(ivPerfil, sexo);
        }

        // 3. LÓGICA DE PROGRESSO (SALVAMENTO)
        // Verifica se esta atividade já foi concluída antes
        boolean jaConcluido = preferences.getBoolean("concluido_jogo_emocoes", false);
        checkboxConcluido.setChecked(jaConcluido);

        checkboxConcluido.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();

            // Salva o estado individual deste CheckBox
            editor.putBoolean("concluido_jogo_emocoes", isChecked);

            // Atualiza a contagem na gaveta de JOGOS MANUAIS
            int atualManuais = preferences.getInt("total_jogosm_concluida", 0);

            if (isChecked) {
                editor.putInt("total_jogosm_concluida", atualManuais + 1);
                Toast.makeText(this, "Parabéns! Mais um ponto no seu perfil!", Toast.LENGTH_SHORT).show();
            } else {
                editor.putInt("total_jogosm_concluida", Math.max(0, atualManuais - 1));
            }

            editor.apply();
        });

        // 4. NAVEGAÇÃO
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_home) intent = new Intent(this, MainLoggedActivity.class);
            else if (id == R.id.nav_perfil) intent = new Intent(this, MeuPerfil.class);
            else if (id == R.id.nav_sair) intent = new Intent(this, MainActivity.class);

            if (intent != null) {
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void ivAvatarMenu(ImageView iv, String sexo) {
        if (sexo.equalsIgnoreCase("Masculino")) {
            iv.setImageResource(R.drawable.meny);
        } else {
            iv.setImageResource(R.drawable.menx);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}