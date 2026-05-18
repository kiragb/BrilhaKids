package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

// IMPORTS DA BIBLIOTECA KONFETTI
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class MainLoggedActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private NavigationView navigationView;
    private KonfettiView konfettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- 1. CONFIGURAÇÃO DE UI (Transparência) ---
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_logged);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getWindow().setNavigationBarContrastEnforced(false);
        }
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightNavigationBars(true);

        // --- 2. RECUPERAR DADOS DO USUÁRIO ---
        String nomeExtra = getIntent().getStringExtra("nome");
        String sexoExtra = getIntent().getStringExtra("sexo");

        if (nomeExtra == null || nomeExtra.isEmpty()) {
            SharedPreferences prefs = getSharedPreferences("BrilhaKidsPrefs", Context.MODE_PRIVATE);
            nomeExtra = prefs.getString("nome_usuario", "Pequeno Explorador");
            sexoExtra = prefs.getString("sexo_usuario", "Masculino");
        }

        final String nome = nomeExtra.trim();
        final String sexo = sexoExtra.trim();

        // --- 3. REFERÊNCIAS ---
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);
        konfettiView = findViewById(R.id.konfettiView);
        navigationView.setItemIconTintList(null);

        // --- 4. CONFIGURAÇÕES INTERFACE E ANIMAÇÃO ---
        configurarInterface(nome, sexo);
        configurarFraseEPersonagemMenu(sexo);

        // Dispara a chuva de confetes festiva 🎉
        dispararConfetes();

        // --- 5. LISTENERS DE NAVEGAÇÃO ---
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_home) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.nav_perfil) {
                intent = new Intent(this, MeuPerfil.class);
            } else if (id == R.id.nav_sair) {
                realizarLogout();
                return true;
            }

            if (intent != null) {
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // --- 6. CONFIGURAÇÃO DOS CARDS ---
        configurarCard(JogosVirtuais.class, nome, sexo, R.id.cardJogos);
        configurarCard(Licoes.class, nome, sexo, R.id.cardLicoes);
        configurarCard(Musicas.class, nome, sexo, R.id.cardMusicas);
        configurarCard(Tutoriais.class, nome, sexo, R.id.cardJogosManuais);
        configurarCard(Dicas.class, nome, sexo, R.id.carddicas);
        configurarCard(FaleConosco.class, nome, sexo, R.id.cardfale);
    }

    /**
     * Configura e inicia uma chuva de confetes coloridos vinda do topo da tela.
     * Atualizado para cobrir toda a extensão horizontal do topo.
     */
    private void dispararConfetes() {
        if (konfettiView == null) return;

        // Configuração da quantidade e tempo de liberação dos confetes
        EmitterConfig emitterConfig = new Emitter(3, TimeUnit.SECONDS).perSecond(60); // Aumentei um pouco o fluxo

        // Criando a festa de confetes!
        Party party = new PartyFactory(emitterConfig)
                .colors(Arrays.asList(
                        Color.parseColor("#00B050"), // Verde BrilhaKids
                        Color.parseColor("#FFC107"), // Amarelo
                        Color.parseColor("#D50326"), // Rosa/Vermelho
                        Color.parseColor("#00B0FF")  // Azul claro
                ))
                .angle(90) // Ângulo para baixo (queda livre)
                // MODIFICAÇÃO:spread(90) concentrará a queda para baixo mais uniformemente a partir do topo
                .spread(90)
                .setSpeedBetween(2f, 7f) // Velocidade um pouco maior e mais variada
                // CORREÇÃO: Usamos Position.Relative(0.0, 0.0) para definir o ponto de partida
                .position(new Position.Relative(0.0, 0.0).between(new Position.Relative(1.0, 0.0)))
                .build();

        konfettiView.start(party);
    }

    private void configurarInterface(String nome, String sexo) {
        TextView saudacao = findViewById(R.id.tvOlaUsuario);
        if (saudacao != null) {
            saudacao.setText("Olá! " + nome + ", tudo bem?");
        }

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
    }

    private void configurarFraseEPersonagemMenu(String sexo) {
        TextView tvFrase = navigationView.findViewById(R.id.tvFraseMenu);

        if (tvFrase != null) {
            String[] frases = {
                    "Você brilha muito! ✨",
                    "Pronto para aprender algo novo? 🍎",
                    "Você é nota dez! 🌟",
                    "Qual será sua descoberta de hoje? 🧐"
            };
            tvFrase.setText(frases[new Random().nextInt(frases.length)]);
        }
    }

    private void configurarCard(Class<?> activityDestino, String nome, String sexo, int idCard) {
        View card = findViewById(idCard);
        if (card != null) {
            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, activityDestino);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            });
        }
    }

    private void realizarLogout() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences prefs = getSharedPreferences("BrilhaKidsPrefs", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
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