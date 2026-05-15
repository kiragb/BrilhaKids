package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Random; // Necessário para o sorteio das frases

// Importe estas classes
import android.graphics.Color;
import androidx.core.view.WindowInsetsControllerCompat;

public class Tutoriais extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutoriais);


        // 1. REFERÊNCIAS DO LAYOUT E MENU
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        final String nome = getIntent().getStringExtra("nome");
        final String sexo = getIntent().getStringExtra("sexo");

        // Configuração do Header do Menu
        View headerView = navigationView.getHeaderView(0);
        configurarHeader(headerView, nome, sexo);

        // --- CHAMADA PARA AS FRASES E IMAGEM DINÂMICA NO MENU ---
        configurarMenuDinamico(sexo);

        // 2. CONFIGURAÇÃO DE TODOS OS INTENTS DOS CARDS
        configurarClique(Quebracabeca.class, nome, sexo, R.id.cardQuebraCabeca, R.id.imageQuebraCabeca, R.id.tvQuebraCabeca);
        configurarClique(Jogoemocoes.class, nome, sexo, R.id.cardEmocoes, R.id.imageEmocoes, R.id.tvEmocoes);
        configurarClique(Alfabetodivertido.class, nome, sexo, R.id.cardAlfabeto, R.id.imageAlfabeto, R.id.tvAlfabeto);
        configurarClique(Antecessores.class, nome, sexo, R.id.cardAntecessores, R.id.imageAntecessores, R.id.tvAntecessores);
        configurarClique(Jogodavelha.class, nome, sexo, R.id.cardVelha, R.id.imageVelha, R.id.tvVelha);
        configurarClique(CaixaCuriosa.class, nome, sexo, R.id.cardCaixa, R.id.imageCaixa, R.id.tvCaixa);
        configurarClique(Silabas.class, nome, sexo, R.id.cardSilabas, R.id.imageSilabas, R.id.tvSilabas);
        configurarClique(Jogodasdecisoes.class, nome, sexo, R.id.cardDecisoes, R.id.imageDecisoes, R.id.tvDecisoes);

        // 3. LISTENERS DE NAVEGAÇÃO
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

    /**
     * Sorteia uma frase de incentivo e define o personagem no menu lateral.
     */
    private void configurarMenuDinamico(String sexo) {
        TextView tvFrase = navigationView.findViewById(R.id.tvFraseMenu);


        // Sorteio da frase motivacional
        if (tvFrase != null) {
            String[] frases = {
                    "Você brilha muito! ✨",
                    "Mãos à obra, Pequeno Explorador! 🛠️",
                    "Pronto para criar algo incrível? 🎨",
                    "Você é nota dez! 🌟",
                    "A criatividade não tem limites! 🚀",
                    "Vamos fazer um brinquedo novo hoje? 🧸"
            };

            int indice = new Random().nextInt(frases.length);
            tvFrase.setText(frases[indice]);
        }
    }

    private void configurarClique(Class<?> destino, String nome, String sexo, int... ids) {
        View.OnClickListener listener = v -> {
            Intent intent = new Intent(Tutoriais.this, destino);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) view.setOnClickListener(listener);
        }
    }

    private void configurarHeader(View header, String nome, String sexo) {
        ImageView ivPerfil = header.findViewById(R.id.imagePerfil);
        TextView tvNome = header.findViewById(R.id.textViewNomeUsuario);

        if (nome != null && tvNome != null) tvNome.setText("Olá, " + nome + "!");
        if (sexo != null && ivPerfil != null) {
            if (sexo.equalsIgnoreCase("Masculino")) ivPerfil.setImageResource(R.drawable.meny);
            else if (sexo.equalsIgnoreCase("Feminino")) ivPerfil.setImageResource(R.drawable.menx);
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