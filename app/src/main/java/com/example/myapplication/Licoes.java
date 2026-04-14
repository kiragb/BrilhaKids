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

public class Licoes extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private TextView textacoes, textpreservar, textvocabulario, textmatematica;
    private ImageView menuIcon, imageacoes, cardacoes, cardpreservarr, imagepreservar,
    cardvocabulario, imagevocabulario, cardmatematica, imagematematica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_licoes);
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

        // Acoes Cotidanas
        cardacoes = findViewById(R.id.cardacoes);
        imageacoes = findViewById(R.id.imagacoes);
        textacoes = findViewById(R.id.textacoes);

        View.OnClickListener abrirFale = v -> {
            Intent intent = new Intent(Licoes.this, AcoesCotidianas.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardacoes.setOnClickListener(abrirFale);
        imageacoes.setOnClickListener(abrirFale);
        textacoes.setOnClickListener(abrirFale);

        // Preservando a Natureza
        cardpreservarr = findViewById(R.id.cardpreservar);
        imagepreservar = findViewById(R.id.imagepreservar);
        textpreservar = findViewById(R.id.textpreservar);

        View.OnClickListener abrirPreservar = v -> {
            Intent intent = new Intent(Licoes.this, PreservandoANatureza.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardpreservarr.setOnClickListener(abrirPreservar);
        imagepreservar.setOnClickListener(abrirPreservar);
        textpreservar.setOnClickListener(abrirPreservar);

        // Voocabulario
        cardvocabulario = findViewById(R.id.cardvocabulario);
        imagevocabulario = findViewById(R.id.imagevocabulario);
        textvocabulario = findViewById(R.id.textvocabulario);

        View.OnClickListener abrirVocabulario = v -> {
            Intent intent = new Intent(Licoes.this, Vocabulario.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardvocabulario.setOnClickListener(abrirVocabulario);
        imagevocabulario.setOnClickListener(abrirVocabulario);
        textvocabulario.setOnClickListener(abrirVocabulario);

        // Matemática
        cardmatematica = findViewById(R.id.cardmatematica);
        imagematematica = findViewById(R.id.imagematematica);
        textmatematica = findViewById(R.id.textmatematica);

        View.OnClickListener abrirMatematica = v -> {
            Intent intent = new Intent(Licoes.this, Matematica.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardmatematica.setOnClickListener(abrirMatematica);
        imagematematica.setOnClickListener(abrirMatematica);
        textmatematica.setOnClickListener(abrirMatematica);

    }
}