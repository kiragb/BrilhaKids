package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Random;

public class activity_numeros extends AppCompatActivity {

    private int numeroAtual;
    private TextView tvNumeroCentral;
    private EditText etAntecessor, etSucessor;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    // Componentes de Progresso
    private CheckBox checkboxConcluido;
    private SharedPreferences preferences;

    // Chaves padrão BrilhaKids
    private static final String PREFS_NAME = "BrilhaKidsPrefs";
    private static final String KEY_CHECK_LOCAL = "concluido_antecessor_sucessor_jogo";
    private static final String KEY_TOTAL_JOGOS = "total_jogo_concluida";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_numeros2);

        // 1. Inicialização de Componentes
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        tvNumeroCentral = findViewById(R.id.tvNumeroCentral);
        etAntecessor = findViewById(R.id.etAntecessor);
        etSucessor = findViewById(R.id.etSucessor);
        Button btnVerificar = findViewById(R.id.btnVerificar);
        ImageView menuIcon = findViewById(R.id.menuIcon);
        checkboxConcluido = findViewById(R.id.checkboxConcluidoAntecessor);

        configurarFraseDinamica();
        navigationView.setItemIconTintList(null);

        // 2. CONFIGURAÇÃO DOS DADOS DO USUÁRIO
        final String nome = getIntent().getStringExtra("nome");
        final String sexo = getIntent().getStringExtra("sexo");
        setupNavigationHeader(nome, sexo);

        // 3. LÓGICA DE PROGRESSO (SharedPreferences)
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (checkboxConcluido != null) {
            checkboxConcluido.setChecked(preferences.getBoolean(KEY_CHECK_LOCAL, false));
            checkboxConcluido.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_CHECK_LOCAL, isChecked);

                int totalAtual = preferences.getInt(KEY_TOTAL_JOGOS, 0);
                if (isChecked) {
                    editor.putInt(KEY_TOTAL_JOGOS, totalAtual + 1);
                    Toast.makeText(this, "Desafio concluído! ✨", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putInt(KEY_TOTAL_JOGOS, Math.max(0, totalAtual - 1));
                }
                editor.apply();
            });
        }

        // 4. MENU LATERAL
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_home) intent = new Intent(this, MainLoggedActivity.class);
            else if (id == R.id.nav_senha) intent = new Intent(this, AlterarSenhaActivity.class);
            else if (id == R.id.nav_sobre) intent = new Intent(this, SobreNos.class);
            else if (id == R.id.nav_sair) intent = new Intent(this, MainActivity.class);
            else if (id == R.id.nav_perfil) intent = new Intent(this, MeuPerfil.class);

            if (intent != null) {
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        gerarNovoDesafio();
        btnVerificar.setOnClickListener(v -> validarResposta());
    }

    private void gerarNovoDesafio() {
        Random random = new Random();
        numeroAtual = random.nextInt(48) + 2; // Evita que o antecessor seja 0 ou negativo
        tvNumeroCentral.setText(String.valueOf(numeroAtual));

        etAntecessor.setText("");
        etSucessor.setText("");
        etAntecessor.setBackgroundResource(R.drawable.card_numero);
        etSucessor.setBackgroundResource(R.drawable.card_numero);
    }

    private void validarResposta() {
        String respAntS = etAntecessor.getText().toString();
        String respSucS = etSucessor.getText().toString();

        if (respAntS.isEmpty() || respSucS.isEmpty()) {
            Toast.makeText(this, "Preencha os dois campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        int respAnt = Integer.parseInt(respAntS);
        int respSuc = Integer.parseInt(respSucS);

        if (respAnt == (numeroAtual - 1) && respSuc == (numeroAtual + 1)) {
            Toast.makeText(this, "Muito bem! Você acertou! 🎉", Toast.LENGTH_SHORT).show();
            etAntecessor.setBackgroundColor(Color.parseColor("#C8E6C9"));
            etSucessor.setBackgroundColor(Color.parseColor("#C8E6C9"));
            tvNumeroCentral.postDelayed(this::gerarNovoDesafio, 1500);
        } else {
            Toast.makeText(this, "Quase lá! Tente de novo. 💪", Toast.LENGTH_SHORT).show();
            if (respAnt != (numeroAtual - 1)) etAntecessor.setBackgroundColor(Color.parseColor("#FFCDD2"));
            else etAntecessor.setBackgroundColor(Color.parseColor("#C8E6C9"));

            if (respSuc != (numeroAtual + 1)) etSucessor.setBackgroundColor(Color.parseColor("#FFCDD2"));
            else etSucessor.setBackgroundColor(Color.parseColor("#C8E6C9"));
        }
    }

    private void setupNavigationHeader(String nome, String sexo) {
        if (navigationView != null && navigationView.getHeaderCount() > 0) {
            View headerView = navigationView.getHeaderView(0);
            TextView textViewNome = headerView.findViewById(R.id.textViewNomeUsuario);
            ImageView imagePerfil = headerView.findViewById(R.id.imagePerfil);

            if (nome != null && textViewNome != null) {
                textViewNome.setText("Olá, " + nome + "!");
            }
            if (sexo != null && imagePerfil != null) {
                imagePerfil.setImageResource(sexo.equalsIgnoreCase("Masculino") ? R.drawable.meny : R.drawable.menx);
            }
        }
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

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}