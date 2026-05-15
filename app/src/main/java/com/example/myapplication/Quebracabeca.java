package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences; // Importante
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox; // Importante
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Random;

public class Quebracabeca extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private CheckBox checkboxConcluido;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quebracabeca);


        // --- REFERÊNCIAS ---
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);
        checkboxConcluido = findViewById(R.id.checkboxConcluidoPuzzle);

        configurarFraseDinamica();

        // Inicializa SharedPreferences
        preferences = getSharedPreferences("BrilhaKidsPrefs", MODE_PRIVATE);

        // --- LÓGICA DE PROGRESSO ---
        // Verifica se já foi concluído antes
        boolean jaConcluido = preferences.getBoolean("concluido_puzzle_manual", false);
        checkboxConcluido.setChecked(jaConcluido);

        checkboxConcluido.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();

            // 1. Salva o estado deste CheckBox específico
            editor.putBoolean("concluido_puzzle_manual", isChecked);

            // 2. Atualiza a contagem na "gaveta" de JOGOS MANUAIS
            int atualManuais = preferences.getInt("total_jogosm_concluida", 0);

            if (isChecked) {
                editor.putInt("total_jogosm_concluida", atualManuais + 1);
                Toast.makeText(this, "Tutorial concluído! +1 no seu Perfil!", Toast.LENGTH_SHORT).show();
            } else {
                editor.putInt("total_jogosm_concluida", Math.max(0, atualManuais - 1));
            }

            editor.apply();
        });

        navigationView.setItemIconTintList(null);
        final String nome = getIntent().getStringExtra("nome");
        final String sexo = getIntent().getStringExtra("sexo");

        if (navigationView.getHeaderCount() > 0) {
            View headerView = navigationView.getHeaderView(0);
            ImageView imagePerfil = headerView.findViewById(R.id.imagePerfil);
            TextView textViewNome = headerView.findViewById(R.id.textViewNomeUsuario);

            if (sexo != null && imagePerfil != null) {
                imagePerfil.setImageResource(sexo.equalsIgnoreCase("Masculino") ? R.drawable.meny : R.drawable.menx);
            }
            if (nome != null && textViewNome != null) {
                textViewNome.setText("Olá, " + nome + "!");
            }
        }

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

    private void configurarFraseDinamica() {
        if (navigationView == null) return;

        TextView tvFrase = navigationView.findViewById(R.id.tvFraseMenu);

        if (tvFrase != null) {
            String[] frases = {
                    "Você brilha muito! ✨",
                    "Pronto para aprender algo novo? 🍎",
                    "Comer bem é super divertido! 🥦",
                    "Qual será sua descoberta de hoje? 🧐",
                    "Você é nota dez! 🌟",
                    "Que tal um jogo agora? 🎮"
            };
            int indice = new Random().nextInt(frases.length);
            tvFrase.setText(frases[indice]);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}