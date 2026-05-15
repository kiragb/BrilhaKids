package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences; // Importado para persistência
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox; // Importado para o progresso
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

public class Jogodavelha extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private CheckBox checkboxConcluido; // Referência para o CheckBox
    private SharedPreferences preferences; // Referência para o banco local

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jogodavelha2);

        // Ajuste de Padding para EdgeToEdge
        View mainView = findViewById(R.id.drawer_layout);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // --- REFERÊNCIAS ---
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);

        configurarFraseDinamica();

        // Supondo que o id no seu XML de Jogo da Velha seja checkboxConcluidoVelha
        checkboxConcluido = findViewById(R.id.checkboxConcluidoVelha);

        // Inicializa SharedPreferences
        preferences = getSharedPreferences("BrilhaKidsPrefs", Context.MODE_PRIVATE);
        navigationView.setItemIconTintList(null);

        // --- DADOS DO USUÁRIO ---
        final String nome = getIntent().getStringExtra("nome");
        final String sexo = getIntent().getStringExtra("sexo");

        configurarHeaderMenu(nome, sexo);

        // --- LÓGICA DE PROGRESSO (SALVAMENTO) ---
        // Verifica se esta atividade específica já foi marcada antes
        boolean jaConcluido = preferences.getBoolean("concluido_jogo_velha", false);
        if (checkboxConcluido != null) {
            checkboxConcluido.setChecked(jaConcluido);

            checkboxConcluido.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = preferences.edit();

                // 1. Salva o estado individual da página
                editor.putBoolean("concluido_jogo_velha", isChecked);

                // 2. Soma ou subtrai da contagem global de JOGOS MANUAIS
                int atualManuais = preferences.getInt("total_jogosm_concluida", 0);

                if (isChecked) {
                    editor.putInt("total_jogosm_concluida", atualManuais + 1);
                    Toast.makeText(this, "Boa! Jogo da Velha concluído! ❌⭕", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putInt("total_jogosm_concluida", Math.max(0, atualManuais - 1));
                }

                editor.apply();
            });
        }

        // --- LISTENERS DO MENU ---
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_home) intent = new Intent(this, MainLoggedActivity.class);
            else if (id == R.id.nav_senha) intent = new Intent(this, AlterarSenhaActivity.class);
            else if (id == R.id.nav_sobre) intent = new Intent(this, SobreNos.class);
            else if (id == R.id.nav_perfil) intent = new Intent(this, MeuPerfil.class);
            else if (id == R.id.nav_sair) intent = new Intent(this, MainActivity.class);

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void configurarHeaderMenu(String nome, String sexo) {
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
    }

    private void configurarFraseDinamica() {
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