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

public class JogosVirtuais extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon, cardLicoes, imagelicoes, cardMusicas,
            imagemusic, cardpuzzleManuais, imagejogosmanuais, imagedica, carddicas,
            cardfale, imagefale, cardpuzzle, imagpuzzle, imagejogodavelha, cardjogodavelha,
            cardantecessor, imageantecessor;
    private TextView textlicoes, TextMusic, textjogosmanuais, textdica, textfale, textpuzzle,
            textjogodavelha, textantecessor;
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

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(this, MainLoggedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            } else if (id == R.id.nav_senha) {
                Intent intent = new Intent(this, AlterarSenhaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            } else if (id == R.id.nav_sobre) {
                Intent intent = new Intent(this, SobreNos.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            } else if (id == R.id.nav_sair) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            } else if (id == R.id.nav_perfil) {
                Intent intent = new Intent(this, MeuPerfil.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }


            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // s puzzle
        cardpuzzle = findViewById(R.id.cardpuzzle);
        imagpuzzle = findViewById(R.id.imagpuzzle);
        textpuzzle = findViewById(R.id.textpuzzle);

        View.OnClickListener abrirpuzzle = v -> {
            Intent intent = new Intent(JogosVirtuais.this, Activity_puzzle.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardpuzzle.setOnClickListener(abrirpuzzle);
        imagpuzzle.setOnClickListener(abrirpuzzle);
        textpuzzle.setOnClickListener(abrirpuzzle);


        // jogo da velha
        cardjogodavelha = findViewById(R.id.cardjogodavelha);
        imagejogodavelha = findViewById(R.id.imagejogodavelha);
        textjogodavelha = findViewById(R.id.textjogodavelha);

        View.OnClickListener abrirjogodavelha = v -> {
            Intent intent = new Intent(JogosVirtuais.this, Activity_jogodavelha.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardjogodavelha.setOnClickListener(abrirjogodavelha);
        imagejogodavelha.setOnClickListener(abrirjogodavelha);
        textjogodavelha.setOnClickListener(abrirjogodavelha);


        // Antecessores
        cardantecessor = findViewById(R.id.cardantecessor);
        imageantecessor = findViewById(R.id.imageantecessor);
        textantecessor = findViewById(R.id.textantecessor);

        View.OnClickListener abrirAntecessor = v -> {
            Intent intent = new Intent(JogosVirtuais.this, activity_numeros.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardantecessor.setOnClickListener(abrirAntecessor);
        imageantecessor.setOnClickListener(abrirAntecessor);
        textantecessor.setOnClickListener(abrirAntecessor);


    }
}
