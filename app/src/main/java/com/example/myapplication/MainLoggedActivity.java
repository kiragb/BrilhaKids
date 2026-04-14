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

public class MainLoggedActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon, cardLicoes, imagelicoes, cardMusicas,
    imagemusic, cardJogosManuais, imagejogosmanuais, imagedica, carddicas,
    cardfale, imagefale, cardJogos;
    private TextView textlicoes, TextMusic, textjogosmanuais, textdica, textfale;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logged);

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
            TextView saudacao = findViewById(R.id.tvOlaUsuario);
            if (saudacao != null) {
                saudacao.setText("Olá! " + nome + ", tudo bem?");
            }

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
            }
            else if (id == R.id.nav_senha) {
                Intent intent = new Intent(this, AlterarSenhaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }

            else if (id == R.id.nav_sobre) {
                Intent intent = new Intent(this, SobreNos.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }

            else if (id == R.id.nav_sair) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }

            else if (id == R.id.nav_perfil) {
                Intent intent = new Intent(this, MeuPerfil.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }


            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        cardJogos = findViewById(R.id.cardJogos);

        // Configurar o clique para o card de Jogos Virtuais
        cardJogos.setOnClickListener(v -> {
            // Exibir a mensagem de "Em Manutenção"
            Toast.makeText(MainLoggedActivity.this, "Jogos Virtuais em manutenção. Volte em breve!", Toast.LENGTH_SHORT).show();
        });

        // Lições
        cardLicoes = findViewById(R.id.cardLicoes);
        imagelicoes = findViewById(R.id.imagelicoes);
        textlicoes = findViewById(R.id.textlicoes);

        View.OnClickListener abrirLicoes = v -> {
            Intent intent = new Intent(MainLoggedActivity.this, Licoes.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardLicoes.setOnClickListener(abrirLicoes);
        imagelicoes.setOnClickListener(abrirLicoes);
        textlicoes.setOnClickListener(abrirLicoes);

        // Músicas
        cardMusicas = findViewById(R.id.cardMusicas);
        imagemusic = findViewById(R.id.imagemusic);
        TextMusic = findViewById(R.id.TextMusic);

        View.OnClickListener abrirMusicas = v -> {
            Intent intent = new Intent(MainLoggedActivity.this, Musicas.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardMusicas.setOnClickListener(abrirMusicas);
        imagemusic.setOnClickListener(abrirMusicas);
        TextMusic.setOnClickListener(abrirMusicas);

        // Jogos Manuais
        cardJogosManuais = findViewById(R.id.cardJogosManuais);
        imagejogosmanuais = findViewById(R.id.imagejogosmanuais);
        textjogosmanuais = findViewById(R.id.textjogosmanuais);

        View.OnClickListener abrirJogosManuais = v -> {
            Intent intent = new Intent(MainLoggedActivity.this, Tutoriais.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardJogosManuais.setOnClickListener(abrirJogosManuais);
        imagejogosmanuais.setOnClickListener(abrirJogosManuais);
        textjogosmanuais.setOnClickListener(abrirJogosManuais);


        // Dicas
        carddicas = findViewById(R.id.carddicas);
        imagedica = findViewById(R.id.imagedica);
        textdica = findViewById(R.id.textdica);
    
        View.OnClickListener abrirDicas = v -> {
            Intent intent = new Intent(MainLoggedActivity.this, Dicas.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
    
            carddicas.setOnClickListener(abrirDicas);
            imagedica.setOnClickListener(abrirDicas);
            textdica.setOnClickListener(abrirDicas);

            // Fale
            cardfale = findViewById(R.id.cardfale);
            imagefale = findViewById(R.id.imagefale);
            textfale = findViewById(R.id.textfale);

            View.OnClickListener abrirFale = v -> {
                Intent intent = new Intent(MainLoggedActivity.this, FaleConosco.class);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            };

            cardfale.setOnClickListener(abrirFale);
            imagefale.setOnClickListener(abrirFale);
            textfale.setOnClickListener(abrirFale);
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
