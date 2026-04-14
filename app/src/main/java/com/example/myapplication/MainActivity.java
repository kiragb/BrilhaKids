package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View; // Importar a classe View
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    TextView tvOlaUsuario;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationView.setItemIconTintList(null);

        TextView tvOlaUsuario = findViewById(R.id.tvOlaUsuario);
        String nomeUsuario = getIntent().getStringExtra("nomeUsuario");

        if (nomeUsuario != null) {
            tvOlaUsuario.setText("Olá, " + nomeUsuario +"! Tudo Bem?");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView menuIcon = findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(v -> {
            // Abre o menu lateral ao clicar no ícone do menu
            drawerLayout.openDrawer(navigationView);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_login) {
                // Fecha o menu e abre a LoginActivity
                drawerLayout.closeDrawers();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // --- Adicionando OnClickListeners para os elementos dos cards ---

        // Card Jogos Virtuais
        ImageView cardJogos = findViewById(R.id.cardJogos);
        ImageView imageView3 = findViewById(R.id.imageView3); // Imagem dentro do card Jogos Virtuais
        TextView tvJogosVirtuais = findViewById(R.id.textjogos); // Texto não tem ID, usando o ID do card mesmo

        // Configura o OnClickListener para todos os elementos relacionados a "Jogos Virtuais"
        View.OnClickListener jogosClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaLoginActivity();
            }
        };

        cardJogos.setOnClickListener(jogosClickListener);
        imageView3.setOnClickListener(jogosClickListener);


        // Card Lições
        ImageView cardLicoes = findViewById(R.id.cardLicoes);
        ImageView imageView4 = findViewById(R.id.imageView4); // Imagem dentro do card Lições
        TextView tvLicoes = findViewById(R.id.textView2); // TextView "Lições"

        View.OnClickListener licoesClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaLoginActivity();

            }
        };

        cardLicoes.setOnClickListener(licoesClickListener);
        imageView4.setOnClickListener(licoesClickListener);
        tvLicoes.setOnClickListener(licoesClickListener);

        // Card Músicas
        ImageView cardMusicas = findViewById(R.id.cardMusicas);
        ImageView imageView = findViewById(R.id.imageView); // Imagem dentro do card Músicas
        TextView tvMusicas = findViewById(R.id.textView); // TextView "Músicas"

        View.OnClickListener musicasClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaLoginActivity();
            }
        };

        cardMusicas.setOnClickListener(musicasClickListener);
        imageView.setOnClickListener(musicasClickListener);
        tvMusicas.setOnClickListener(musicasClickListener);

        // Card Jogos Manuais
        ImageView cardJogosManuais = findViewById(R.id.cardJogosManuais);
        ImageView imageView2 = findViewById(R.id.imageView2); // Imagem dentro do card Jogos Manuais
        TextView tvJogosManuais = findViewById(R.id.textjogosmanuais); // TextView "Jogos Manuais"

        View.OnClickListener jogosManuaisClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaLoginActivity();
            }
        };

        cardJogosManuais.setOnClickListener(jogosManuaisClickListener);
        imageView2.setOnClickListener(jogosManuaisClickListener);

        // Card Dicas
        ImageView cardDicas = findViewById(R.id.carddicas);
        ImageView imageView6 = findViewById(R.id.imageView6); // Imagem dentro do card Dicas
        TextView tvDicas = findViewById(R.id.textView3); // TextView "Dicas"

        View.OnClickListener dicasClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaLoginActivity();
            }
        };

        cardDicas.setOnClickListener(dicasClickListener);
        imageView6.setOnClickListener(dicasClickListener);
        tvDicas.setOnClickListener(dicasClickListener);

        // Card Fale Conosco
        ImageView cardFaleConosco = findViewById(R.id.cardfale);
        ImageView imageView5 = findViewById(R.id.imageView5); // Imagem dentro do card Fale Conosco
        TextView tvFaleConosco = findViewById(R.id.textView5); // TextView "Fale Conosco"

        View.OnClickListener faleConoscoClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaLoginActivity();
            }
        };

        cardFaleConosco.setOnClickListener(faleConoscoClickListener);
        imageView5.setOnClickListener(faleConoscoClickListener);
        tvFaleConosco.setOnClickListener(faleConoscoClickListener);

    }

    // Método auxiliar para iniciar a LoginActivity
    private void irParaLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}