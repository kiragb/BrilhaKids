package com.example.myapplication;

import android.content.Intent;
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

import java.util.Random; // Import necessário para as frases aleatórias

public class Musicas extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private TextView textamigo, textmariana, textcoleta, textalfabeto, textchefe;
    private ImageView menuIcon, cardamigo, cardmariana, cardcoleta, cardalfabeto, cardchefe,
            imageamigo, imageamigodino, imagemariana, imagecoleta, imagealfabeto, imagechefe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_musicas);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referências
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        configurarFraseDinamica();


        // Abre o menu ao clicar no ícone
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        View headerView = navigationView.getHeaderView(0);
        String sexo = getIntent().getStringExtra("sexo");
        String nome = getIntent().getStringExtra("nome");

        // --- 1. CONFIGURAÇÃO DO AVATAR NO HEADER ---
        if (sexo != null) {
            ImageView imagePerfil = headerView.findViewById(R.id.imagePerfil);
            if (imagePerfil != null) {
                if (sexo.equalsIgnoreCase("Masculino")) {
                    imagePerfil.setImageResource(R.drawable.meny);
                } else if (sexo.equalsIgnoreCase("Feminino")) {
                    imagePerfil.setImageResource(R.drawable.menx);
                }
            }
        }

        // --- 2. CONFIGURAÇÃO DO NOME NO HEADER ---
        if (nome != null) {
            TextView textViewNome = headerView.findViewById(R.id.textViewNomeUsuario);
            if (textViewNome != null) {
                textViewNome.setText("Olá, " + nome + "!");
            }
        }

        // --- 3. CHAMADA PARA AS FRASES E IMAGEM DINÂMICA NO MENU ---
        configurarMenuDinamico(sexo);

        // Configuração dos itens de navegação lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_home) intent = new Intent(this, MainLoggedActivity.class);
            else if (id == R.id.nav_senha) intent = new Intent(this, AlterarSenhaActivity.class);
            else if (id == R.id.nav_sobre) intent = new Intent(this, SobreNos.class);
            else if (id == R.id.nav_sair) intent = new Intent(this, MainActivity.class);
            else if (id == R.id.nav_perfil) intent = new Intent(this, MeuPerfil.class);

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // --- 4. CONFIGURAÇÃO DOS CLIQUES NAS MÚSICAS ---
        configurarCliquesMusicas(nome, sexo);
    }

    /**
     * Sorteia uma frase de incentivo e define a imagem do personagem central do menu.
     */
    private void configurarMenuDinamico(String sexo) {
        TextView tvFrase = navigationView.findViewById(R.id.tvFraseMenu);

        // Sorteio da frase motivacional
        if (tvFrase != null) {
            String[] frases = {
                    "Você brilha muito! ✨",
                    "Vamos cantar e aprender? 🎶",
                    "Que ritmo divertido! 🥁",
                    "Você é nota dez! 🌟",
                    "Sinta a música brilhar! 🎵",
                    "Pronto para soltar a voz? 🎤"
            };

            int indice = new Random().nextInt(frases.length);
            tvFrase.setText(frases[indice]);
        }
    }

    private void configurarCliquesMusicas(String nome, String sexo) {
        // Amigossauro
        cardamigo = findViewById(R.id.carddino);
        imageamigo = findViewById(R.id.imageamigo);
        imageamigodino = findViewById(R.id.imageamigodino);
        textamigo = findViewById(R.id.textamigo);

        View.OnClickListener abrirAmigo = v -> {
            Intent intent = new Intent(Musicas.this, Amigossauro.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
        if(cardamigo != null) cardamigo.setOnClickListener(abrirAmigo);
        if(imageamigo != null) imageamigo.setOnClickListener(abrirAmigo);
        if(imageamigodino != null) imageamigodino.setOnClickListener(abrirAmigo);
        if(textamigo != null) textamigo.setOnClickListener(abrirAmigo);

        // Mariana
        cardmariana = findViewById(R.id.cardmariana);
        imagemariana = findViewById(R.id.imagemariana);
        textmariana = findViewById(R.id.textmariana);

        View.OnClickListener abrirMariana = v -> {
            Intent intent = new Intent(Musicas.this, Mariana.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
        if(cardmariana != null) cardmariana.setOnClickListener(abrirMariana);
        if(imagemariana != null) imagemariana.setOnClickListener(abrirMariana);
        if(textmariana != null) textmariana.setOnClickListener(abrirMariana);

        // Coleta
        cardcoleta = findViewById(R.id.cardcoleta);
        imagecoleta = findViewById(R.id.imagecoleta);
        textcoleta = findViewById(R.id.textcoleta);

        View.OnClickListener abrirColeta = v -> {
            Intent intent = new Intent(Musicas.this, Coleta.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
        if(cardcoleta != null) cardcoleta.setOnClickListener(abrirColeta);
        if(imagecoleta != null) imagecoleta.setOnClickListener(abrirColeta);
        if(textcoleta != null) textcoleta.setOnClickListener(abrirColeta);

        // Alfabeto
        cardalfabeto = findViewById(R.id.cardalfabeto);
        imagealfabeto = findViewById(R.id.imagealfabeto);
        textalfabeto = findViewById(R.id.textalfabeto);

        View.OnClickListener abrirAlfabeto = v -> {
            Intent intent = new Intent(Musicas.this, Alfabeto.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
        if(cardalfabeto != null) cardalfabeto.setOnClickListener(abrirAlfabeto);
        if(imagealfabeto != null) imagealfabeto.setOnClickListener(abrirAlfabeto);
        if(textalfabeto != null) textalfabeto.setOnClickListener(abrirAlfabeto);

        // Chefe
        cardchefe = findViewById(R.id.cardchefe);
        imagechefe = findViewById(R.id.imagechefe);
        textchefe = findViewById(R.id.textchefe);

        View.OnClickListener abrirChefe = v -> {
            Intent intent = new Intent(Musicas.this, Chefinho.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
        if(cardchefe != null) cardchefe.setOnClickListener(abrirChefe);
        if(imagechefe != null) imagechefe.setOnClickListener(abrirChefe);
        if(textchefe != null) textchefe.setOnClickListener(abrirChefe);
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
}