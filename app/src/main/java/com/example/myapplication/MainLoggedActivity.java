package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random; // Import necessário para o sorteio

public class MainLoggedActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logged);

        // --- 1. RECUPERAR DADOS (Intent ou SharedPreferences) ---
        String nomeExtra = getIntent().getStringExtra("nome");
        String sexoExtra = getIntent().getStringExtra("sexo");

        if (nomeExtra == null || nomeExtra.isEmpty()) {
            SharedPreferences prefs = getSharedPreferences("BrilhaKidsPrefs", Context.MODE_PRIVATE);
            nomeExtra = prefs.getString("nome_usuario", "Pequeno Explorador");
            sexoExtra = prefs.getString("sexo_usuario", "Masculino");
        }

        final String nome = nomeExtra.trim();
        final String sexo = sexoExtra.trim();

        // --- 2. REFERÊNCIAS ---
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        // --- 3. CONFIGURAÇÃO DO HEADER, AVATAR E FRASES ---
        configurarInterface(nome, sexo);
        configurarFraseDinamica(); // Nova função para preencher o espaço do menu

        // --- 4. LISTENERS E NAVEGAÇÃO ---
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_home) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            else if (id == R.id.nav_senha) {
                intent = new Intent(this, AlterarSenhaActivity.class);
            }
            else if (id == R.id.nav_sobre) {
                intent = new Intent(this, SobreNos.class);
            }
            else if (id == R.id.nav_perfil) {
                intent = new Intent(this, MeuPerfil.class);
            }
            else if (id == R.id.nav_sair) {
                realizarLogout();
                return true;
            }

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // --- 5. CONFIGURAÇÃO DOS CARDS ---
        configurarCard(JogosVirtuais.class, nome, sexo, R.id.cardJogos, R.id.imagejogosvirtuais, R.id.textjogosvirtuais);
        configurarCard(Licoes.class, nome, sexo, R.id.cardLicoes, R.id.imagelicoes, R.id.textlicoes);
        configurarCard(Musicas.class, nome, sexo, R.id.cardMusicas, R.id.imagemusic, R.id.TextMusic);
        configurarCard(Tutoriais.class, nome, sexo, R.id.cardJogosManuais, R.id.imagejogosmanuais, R.id.textjogosmanuais);
        configurarCard(Dicas.class, nome, sexo, R.id.carddicas, R.id.imagedica, R.id.textdica);
        configurarCard(FaleConosco.class, nome, sexo, R.id.cardfale, R.id.imagefale, R.id.textfale);
    }

    private void configurarInterface(String nome, String sexo) {
        if (navigationView.getHeaderCount() > 0) {
            View headerView = navigationView.getHeaderView(0);
            ImageView imagePerfil = headerView.findViewById(R.id.imagePerfil);
            TextView textViewNome = headerView.findViewById(R.id.textViewNomeUsuario);

            if (imagePerfil != null) {
                imagePerfil.setImageResource(sexo.equalsIgnoreCase("Masculino") ? R.drawable.meny : R.drawable.menx);
            }
            if (textViewNome != null) {
                textViewNome.setText("Olá, " + nome + "!");
            }
        }

        TextView saudacao = findViewById(R.id.tvOlaUsuario);
        if (saudacao != null) {
            saudacao.setText("Olá! " + nome + ", tudo bem?");
        }
    }

    /**
     * Sorteia uma frase motivadora para preencher o rodapé do menu lateral.
     */
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

    private void realizarLogout() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences prefs = getSharedPreferences("BrilhaKidsPrefs", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void configurarCard(Class<?> activityDestino, String nome, String sexo, int... ids) {
        View.OnClickListener listener = v -> {
            Intent intent = new Intent(MainLoggedActivity.this, activityDestino);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) view.setOnClickListener(listener);
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