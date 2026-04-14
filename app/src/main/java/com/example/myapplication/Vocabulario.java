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

public class Vocabulario extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private TextView textacoes, textselvagens, textdomesticos, textfazenda;
    private ImageView menuIcon, imageacoes, cardacoes, cardfazenda, carddomesticos, cardselvagens,
    imagedomesticos, imageselvagensleao, imageselvagensgirafinha, imagefazenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vocabulario);
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

        // Domesticos
        carddomesticos = findViewById(R.id.carddomesticos);
        imagedomesticos = findViewById(R.id.imagedomesticos);
        textdomesticos = findViewById(R.id.textdomesticos);

        View.OnClickListener abrirDomesticos = v -> {
            Intent intent = new Intent(Vocabulario.this, Domesticos.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        carddomesticos.setOnClickListener(abrirDomesticos);
        imagedomesticos.setOnClickListener(abrirDomesticos);
        textdomesticos.setOnClickListener(abrirDomesticos);

        // Fazenda
        cardfazenda = findViewById(R.id.cardfazenda);
        imagefazenda = findViewById(R.id.imagefazenda);
        textfazenda = findViewById(R.id.textfazenda);

        View.OnClickListener abrirFazenda = v -> {
            Intent intent = new Intent(Vocabulario.this, Fazenda.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardfazenda.setOnClickListener(abrirFazenda);
        textfazenda.setOnClickListener(abrirFazenda);
        imagefazenda.setOnClickListener(abrirFazenda);


        // Selvagens
        cardselvagens= findViewById(R.id.cardselvagens);
        imageselvagensleao= findViewById(R.id.imageselvagensgirafinha);
        imageselvagensgirafinha= findViewById(R.id.imageselvagensleao);
        textselvagens = findViewById(R.id.textselvagens);

        View.OnClickListener abrirSelvagens = v -> {
            Intent intent = new Intent(Vocabulario.this, Selvagens.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardselvagens.setOnClickListener(abrirSelvagens);
        imageselvagensleao.setOnClickListener(abrirSelvagens);
        imageselvagensgirafinha.setOnClickListener(abrirSelvagens);
        textselvagens.setOnClickListener(abrirSelvagens);



    }
}