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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Random;

public class Activity_letras extends AppCompatActivity {

    private String[] palavras = {"GATO", "BOLA", "CASA", "DADO", "LEÃO"};
    private int[] imagens = {R.drawable.gato, R.drawable.bola, R.drawable.casa, R.drawable.dado, R.drawable.leao};
    private int indiceAtual;

    private ImageView ivObjeto;
    private TextView tvRestoPalavra;
    private EditText etPrimeiraLetra;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CheckBox checkboxConcluido;
    private SharedPreferences preferences;

    // Chaves para o sistema de progresso do BrilhaKids
    private static final String PREFS_NAME = "BrilhaKidsPrefs";
    private static final String KEY_CHECK_LOCAL = "concluido_atividade_letras";
    private static final String KEY_TOTAL_LICOES = "total_licao_concluida";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letras);


        // 1. Inicialização dos Componentes
        ivObjeto = findViewById(R.id.ivObjeto);
        tvRestoPalavra = findViewById(R.id.tvRestoDaPalavra);
        etPrimeiraLetra = findViewById(R.id.etPrimeiraLetra);
        Button btnVerificar = findViewById(R.id.btnVerificarLetra);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        ImageView menuIcon = findViewById(R.id.menuIcon);
        checkboxConcluido = findViewById(R.id.checkboxConcluidoLetra);

        configurarFraseDinamica();

        navigationView.setItemIconTintList(null);

        // 2. CONFIGURAÇÃO DOS DADOS DO USUÁRIO
        final String nome = getIntent().getStringExtra("nome");
        final String sexo = getIntent().getStringExtra("sexo");
        setupNavigationHeader(nome, sexo);

        // 3. LÓGICA DE PROGRESSO (SharedPreferences)
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isConcluido = preferences.getBoolean(KEY_CHECK_LOCAL, false);

        if (checkboxConcluido != null) {
            checkboxConcluido.setChecked(isConcluido);
            checkboxConcluido.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_CHECK_LOCAL, isChecked);

                // Atualiza o total de LIÇÕES concluídas para o gráfico do perfil
                int totalAtual = preferences.getInt(KEY_TOTAL_LICOES, 0);
                if (isChecked) {
                    editor.putInt(KEY_TOTAL_LICOES, totalAtual + 1);
                    Toast.makeText(this, "Muito bem! Lição salva. ✨", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putInt(KEY_TOTAL_LICOES, Math.max(0, totalAtual - 1));
                }
                editor.apply();
            });
        }

        // Abre o menu ao clicar no ícone
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // 4. Listener do Menu Lateral
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

        // 5. Lógica do Botão Verificar
        btnVerificar.setOnClickListener(v -> {
            String letraDigitada = etPrimeiraLetra.getText().toString().toUpperCase();

            if (letraDigitada.isEmpty()) {
                Toast.makeText(this, "Digite uma letra!", Toast.LENGTH_SHORT).show();
                return;
            }

            String letraCorreta = String.valueOf(palavras[indiceAtual].charAt(0));

            if (letraDigitada.equals(letraCorreta)) {
                Toast.makeText(this, "Muito bem! Você acertou! 🌟", Toast.LENGTH_SHORT).show();
                etPrimeiraLetra.setBackgroundColor(Color.parseColor("#C8E6C9")); // Verde
                ivObjeto.postDelayed(this::gerarNovoDesafio, 1500);
            } else {
                Toast.makeText(this, "Tente outra letra! Você consegue! 💪", Toast.LENGTH_SHORT).show();
                etPrimeiraLetra.setBackgroundColor(Color.parseColor("#FFCDD2")); // Vermelho
            }
        });
    }

    private void gerarNovoDesafio() {
        Random random = new Random();
        indiceAtual = random.nextInt(palavras.length);

        String palavraCompleta = palavras[indiceAtual];
        ivObjeto.setImageResource(imagens[indiceAtual]);

        tvRestoPalavra.setText(palavraCompleta.substring(1));
        etPrimeiraLetra.setText("");
        etPrimeiraLetra.setBackgroundResource(R.drawable.card_numero);
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