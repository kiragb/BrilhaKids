package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Random; // Import necessário para o sorteio das frases

public class JogosVirtuais extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon, cardLicoes, imagelicoes, cardMusicas,
            imagemusic, cardpuzzleManuais, imagejogosmanuais, imagedica, carddicas,
            cardfale, imagefale, cardpuzzle, imagpuzzle, imagejogodavelha, cardjogodavelha,
            cardantecessor, imageantecessor, imageletras, cardletras;
    private TextView textlicoes, TextMusic, textjogosmanuais, textdica, textfale, textpuzzle,
            textjogodavelha, textantecessor, textletras;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogos_virtuais);

        // Referências
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        View headerView = navigationView.getHeaderView(0);
        String nome = getIntent().getStringExtra("nome");
        String sexo = getIntent().getStringExtra("sexo");

        // Configuração do Avatar no Header
        if (sexo != null) {
            ImageView imagePerfil = headerView.findViewById(R.id.imagePerfil);
            if (sexo.equalsIgnoreCase("Masculino")) {
                imagePerfil.setImageResource(R.drawable.meny);
            } else if (sexo.equalsIgnoreCase("Feminino")) {
                imagePerfil.setImageResource(R.drawable.menx);
            }
        }

        if (nome != null) {
            TextView textViewNome = headerView.findViewById(R.id.textViewNomeUsuario);
            if (textViewNome != null) {
                textViewNome.setText("Olá, " + nome + "!");
            }
        }

        // --- CHAMADA PARA AS FRASES E IMAGEM DINÂMICA NO MENU ---
        configurarFraseEImagemDinamica(sexo);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_home) {
                intent = new Intent(this, MainLoggedActivity.class);
            } else if (id == R.id.nav_senha) {
                intent = new Intent(this, AlterarSenhaActivity.class);
            } else if (id == R.id.nav_sobre) {
                intent = new Intent(this, SobreNos.class);
            } else if (id == R.id.nav_sair) {
                intent = new Intent(this, MainActivity.class);
            } else if (id == R.id.nav_perfil) {
                intent = new Intent(this, MeuPerfil.class);
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

        // Configuração dos cards de jogos (Puzzle, Velha, Antecessor, Letras)
        configurarCliquesJogos(nome, sexo);
    }


    private void configurarFraseEImagemDinamica(String sexo) {
        TextView tvFrase = navigationView.findViewById(R.id.tvFraseMenu);

        if (tvFrase != null) {
            String[] frases = {
                    "Você brilha muito! ✨",
                    "Pronto para aprender algo novo? 🍎",
                    "Comer bem é super divertido! 🥦",
                    "Qual será sua descoberta de hoje? 🧐",
                    "Você é nota dez! 🌟",
                    "Vamos jogar e aprender juntos? 🎮"
            };

            int indice = new Random().nextInt(frases.length);
            tvFrase.setText(frases[indice]);
        }
    }

    private void configurarCliquesJogos(String nome, String sexo) {
        // Lógica para o Puzzle
        cardpuzzle = findViewById(R.id.cardpuzzle);
        imagpuzzle = findViewById(R.id.imagpuzzle);
        textpuzzle = findViewById(R.id.textpuzzle);

        View.OnClickListener abrirpuzzle = v -> {
            Intent intent = new Intent(JogosVirtuais.this, Activity_puzzle.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
        if(cardpuzzle != null) cardpuzzle.setOnClickListener(abrirpuzzle);

        // Lógica para Jogo da Velha
        cardjogodavelha = findViewById(R.id.cardjogodavelha);
        imagejogodavelha = findViewById(R.id.imagejogodavelha);
        textjogodavelha = findViewById(R.id.textjogodavelha);

        View.OnClickListener abrirjogodavelha = v -> {
            Intent intent = new Intent(JogosVirtuais.this, Activity_jogodavelha.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
        if(cardjogodavelha != null) cardjogodavelha.setOnClickListener(abrirjogodavelha);

        // Lógica para Antecessores
        cardantecessor = findViewById(R.id.cardantecessor);
        imageantecessor = findViewById(R.id.imageantecessor);
        textantecessor = findViewById(R.id.textantecessor);

        View.OnClickListener abrirAntecessor = v -> {
            Intent intent = new Intent(JogosVirtuais.this, activity_numeros.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
        if(cardantecessor != null) cardantecessor.setOnClickListener(abrirAntecessor);

        // Lógica para Letras
        cardletras = findViewById(R.id.cardletras);
        imageletras = findViewById(R.id.imageletras);
        textletras = findViewById(R.id.textletras);

        View.OnClickListener abrirLetras = v -> {
            Intent intent = new Intent(JogosVirtuais.this, Activity_letras.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
        if(cardletras != null) cardletras.setOnClickListener(abrirLetras);
    }
}