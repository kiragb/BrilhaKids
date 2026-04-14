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

public class AcoesCotidianas extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private TextView textacoes, textescovar, textvocabulario, textorganizacao;
    private ImageView menuIcon, imageacoes, cardacoes, cardeescovar,
            imageelmoescovar, imagepastaescovar, imagevocabulario, cardvocabulario,
            cardorganizacao, imageorganizacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acoes_cotidianas);
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

        // Escovar
        cardeescovar = findViewById(R.id.cardescovar);
        imageelmoescovar = findViewById(R.id.imageelmoescovar);
        imagepastaescovar = findViewById(R.id.imagepastaescovar);
        textescovar = findViewById(R.id.textescovar);

        View.OnClickListener abrirEscovar = v -> {
            Intent intent = new Intent(AcoesCotidianas.this, EscovarOsDentinhos.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardeescovar.setOnClickListener(abrirEscovar);
        imagepastaescovar.setOnClickListener(abrirEscovar);
        imageelmoescovar.setOnClickListener(abrirEscovar);
        textescovar.setOnClickListener(abrirEscovar);

        // Escovar
        cardvocabulario = findViewById(R.id.cardvocabulario);
        imagevocabulario = findViewById(R.id.imagevocabulario);
        textvocabulario = findViewById(R.id.textvocabulario);

        View.OnClickListener abrirPalavras = v -> {
            Intent intent = new Intent(AcoesCotidianas.this, PalavrasMagicas.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardvocabulario.setOnClickListener(abrirPalavras);
        imagevocabulario.setOnClickListener(abrirPalavras);
        textescovar.setOnClickListener(abrirPalavras);

        // Organização
        cardorganizacao = findViewById(R.id.cardmatematica);
        imageorganizacao = findViewById(R.id.imagematematica);
        textorganizacao = findViewById(R.id.textmatematica);

        View.OnClickListener abrirOrganizacao = v -> {
            Intent intent = new Intent(AcoesCotidianas.this, Organizacao.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardorganizacao.setOnClickListener(abrirOrganizacao);
        imageorganizacao.setOnClickListener(abrirOrganizacao);
        textorganizacao.setOnClickListener(abrirOrganizacao);


    }
}
