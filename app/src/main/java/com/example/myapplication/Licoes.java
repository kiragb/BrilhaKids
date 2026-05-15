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

        // Configuração dos itens de navegação
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

        // Configuração dos cliques nas Lições
        configurarCliquesLicoes(nome, sexo);
    }

    /**
     * Sorteia uma frase e define o personagem no corpo do menu lateral.
     */
    private void configurarMenuDinamico(String sexo) {
        TextView tvFrase = navigationView.findViewById(R.id.tvFraseMenu);


        // Sorteio da frase motivacional
        if (tvFrase != null) {
            String[] frases = {
                    "Você brilha muito! ✨",
                    "Pronto para aprender algo novo? 🍎",
                    "Comer bem é super divertido! 🥦",
                    "Qual será sua descoberta de hoje? 🧐",
                    "Você é nota dez! 🌟",
                    "Vamos brilhar nas lições hoje! ✍️"
            };

            int indice = new Random().nextInt(frases.length);
            tvFrase.setText(frases[indice]);
        }
    }

    private void configurarCliquesLicoes(String nome, String sexo) {
        // Ações Cotidianas
        cardacoes = findViewById(R.id.cardacoes);
        imageacoes = findViewById(R.id.imagacoes);
        textacoes = findViewById(R.id.textacoes);

        View.OnClickListener abrirAcoes = v -> {
            Intent intent = new Intent(Licoes.this, AcoesCotidianas.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
        if(cardacoes != null) cardacoes.setOnClickListener(abrirAcoes);

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
        if(cardpreservarr != null) cardpreservarr.setOnClickListener(abrirPreservar);

        // Vocabulário
        cardvocabulario = findViewById(R.id.cardvocabulario);
        imagevocabulario = findViewById(R.id.imagevocabulario);
        textvocabulario = findViewById(R.id.textvocabulario);

        View.OnClickListener abrirVocabulario = v -> {
            Intent intent = new Intent(Licoes.this, Vocabulario.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };
        if(cardvocabulario != null) cardvocabulario.setOnClickListener(abrirVocabulario);

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
        if(cardmatematica != null) cardmatematica.setOnClickListener(abrirMatematica);
    }
}