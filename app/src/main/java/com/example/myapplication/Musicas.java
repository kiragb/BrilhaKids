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
        // Abre o menu ao clicar na imagem
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        View headerView = navigationView.getHeaderView(0);

        String sexo = getIntent().getStringExtra("sexo");

        if (sexo != null) {
            ImageView imagePerfil = headerView.findViewById(R.id.imagePerfil); // Pegue a referência do ImageView do header
            if (imagePerfil != null) {
                if (sexo.equalsIgnoreCase("Masculino")) {
                    imagePerfil.setImageResource(R.drawable.meny);
                } else if (sexo.equalsIgnoreCase("Feminino")) {
                    imagePerfil.setImageResource(R.drawable.menx);
                }
            }
        }

        // Mostrar nome do usuário no TextView do header
        String nome = getIntent().getStringExtra("nome");

        if (nome != null) {
            // Atualiza saudação principal no fundo laranja

            // Atualiza nome no header do menu lateral
            TextView textViewNome = headerView.findViewById(R.id.textViewNomeUsuario);
            if (textViewNome != null) {
                textViewNome.setText("Olá, " + nome + "!");
            }
        }

        // Este é o único setNavigationItemSelectedListener que você precisa
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

        // Amigossauro
        cardamigo = findViewById(R.id.carddino);
        imageamigo= findViewById(R.id.imageamigo);
        imageamigodino= findViewById(R.id.imageamigodino);
        textamigo = findViewById(R.id.textamigo);

        View.OnClickListener abrirAmigo = v -> {
            Intent intent = new Intent(Musicas.this, Amigossauro.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardamigo.setOnClickListener(abrirAmigo);
        imageamigo.setOnClickListener(abrirAmigo);
        imageamigodino.setOnClickListener(abrirAmigo);
        textamigo.setOnClickListener(abrirAmigo);

        // Mariana
        cardmariana = findViewById(R.id.cardmariana);
        imagemariana= findViewById(R.id.imagemariana);
        textmariana = findViewById(R.id.textmariana);

        View.OnClickListener abrirMariana = v -> {
            Intent intent = new Intent(Musicas.this, Mariana.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardmariana.setOnClickListener(abrirMariana);
        imagemariana.setOnClickListener(abrirMariana);
        textmariana.setOnClickListener(abrirMariana);

        // Coleta
        cardcoleta = findViewById(R.id.cardcoleta);
        imagecoleta= findViewById(R.id.imagecoleta);
        textcoleta = findViewById(R.id.textcoleta);

        View.OnClickListener abrirColeta = v -> {
            Intent intent = new Intent(Musicas.this, Coleta.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardcoleta.setOnClickListener(abrirColeta);
        imagecoleta.setOnClickListener(abrirColeta);
        textcoleta.setOnClickListener(abrirColeta);

        // Alfabeto
        cardalfabeto = findViewById(R.id.cardalfabeto);
        imagealfabeto= findViewById(R.id.imagealfabeto);
        textalfabeto = findViewById(R.id.textalfabeto);

        View.OnClickListener abrirAlfabeto = v -> {
            Intent intent = new Intent(Musicas.this, Alfabeto.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardalfabeto.setOnClickListener(abrirAlfabeto);
        imagealfabeto.setOnClickListener(abrirAlfabeto);
        textalfabeto.setOnClickListener(abrirAlfabeto);

        // Chefe
        cardchefe = findViewById(R.id.cardchefe);
        imagechefe= findViewById(R.id.imagechefe);
        textchefe = findViewById(R.id.textchefe);

        View.OnClickListener abrirChefe = v -> {
            Intent intent = new Intent(Musicas.this, Chefinho.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardchefe.setOnClickListener(abrirChefe);
        imagechefe.setOnClickListener(abrirChefe);
        textchefe.setOnClickListener(abrirChefe);

    }

    }


