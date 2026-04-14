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

public class Matematica extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private TextView textacoes, textformas, textnumeros, textquarto;
    private ImageView menuIcon, imageacoes, cardacoes, cardformas, cardnumeros, cardquarto,
    imagequarto, imagepocoyo, imageblocos, imagenumeros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_matematica);
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
        // Formas
        cardformas = findViewById(R.id.cardformas);
        imagepocoyo = findViewById(R.id.imagepocoyo);
        imageblocos = findViewById(R.id.imagebloco);
        textformas = findViewById(R.id.textformas);

        View.OnClickListener abrirFormas = v -> {
            Intent intent = new Intent(Matematica.this, Formas.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardformas.setOnClickListener(abrirFormas);
        imagepocoyo.setOnClickListener(abrirFormas);
        imageblocos.setOnClickListener(abrirFormas);
        textformas.setOnClickListener(abrirFormas);

        // Quarto
        cardquarto = findViewById(R.id.cardquarto);
        imagequarto = findViewById(R.id.imagequarto);
        textquarto = findViewById(R.id.textquarto);

        View.OnClickListener abrirQuarto = v -> {
            Intent intent = new Intent(Matematica.this, Quarto.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardquarto.setOnClickListener(abrirQuarto);
        imagequarto.setOnClickListener(abrirQuarto);
        textquarto.setOnClickListener(abrirQuarto);

        // Numeros
        cardnumeros = findViewById(R.id.cardnumeros);
        imagenumeros = findViewById(R.id.imagenumeros);
        textnumeros = findViewById(R.id.textnumeros);

        View.OnClickListener abrirNumeros = v -> {
            Intent intent = new Intent(Matematica.this, Numeros.class);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
        };

        cardnumeros.setOnClickListener(abrirNumeros);
        imagenumeros.setOnClickListener(abrirNumeros);
        textnumeros.setOnClickListener(abrirNumeros);


    }
}