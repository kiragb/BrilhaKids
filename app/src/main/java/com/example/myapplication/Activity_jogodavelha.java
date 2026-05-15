package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Activity_jogodavelha extends AppCompatActivity {

    private Button[] buttons = new Button[9];
    private boolean isPlayerX = true; // True = X, False = O
    private int roundCount = 0;
    private TextView tvStatus;

    // Componentes de Menu e Progresso
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CheckBox checkboxConcluido;
    private SharedPreferences preferences;

    // Chaves padrão BrilhaKids
    private static final String PREFS_NAME = "BrilhaKidsPrefs";
    private static final String KEY_CHECK_VELHA = "concluido_jogo_da_velha";
    private static final String KEY_TOTAL_JOGOS = "total_jogo_concluida";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogodavelha);

        // --- 1. INICIALIZAÇÃO DE COMPONENTES ---
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        ImageView menuIcon = findViewById(R.id.menuIcon);
        tvStatus = findViewById(R.id.tvStatus);
        GridLayout gridVelha = findViewById(R.id.gridVelha);
        Button btnReset = findViewById(R.id.btnResetVelha);
        checkboxConcluido = findViewById(R.id.checkboxConcluidoVelha);

        navigationView.setItemIconTintList(null);

        // --- 2. CONFIGURAÇÃO DOS DADOS DO USUÁRIO ---
        final String nome = getIntent().getStringExtra("nome");
        final String sexo = getIntent().getStringExtra("sexo");
        setupNavigationHeader(nome, sexo);

        // --- 3. LÓGICA DE PROGRESSO (SharedPreferences) ---
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (checkboxConcluido != null) {
            checkboxConcluido.setChecked(preferences.getBoolean(KEY_CHECK_VELHA, false));
            checkboxConcluido.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_CHECK_VELHA, isChecked);

                int totalAtual = preferences.getInt(KEY_TOTAL_JOGOS, 0);
                if (isChecked) {
                    editor.putInt(KEY_TOTAL_JOGOS, totalAtual + 1);
                    Toast.makeText(this, "Boa jogada! Progresso salvo. ✨", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putInt(KEY_TOTAL_JOGOS, Math.max(0, totalAtual - 1));
                }
                editor.apply();
            });
        }

        // --- 4. MENU LATERAL ---
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

        // --- 5. LÓGICA DO TABULEIRO ---
        gridVelha.removeAllViews();
        for (int i = 0; i < 9; i++) {
            Button btn = new Button(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; params.height = 0;
            params.columnSpec = GridLayout.spec(i % 3, 1f);
            params.rowSpec = GridLayout.spec(i / 3, 1f);
            params.setMargins(10, 10, 10, 10);
            btn.setLayoutParams(params);

            btn.setBackgroundResource(R.drawable.card_velha);
            btn.setStateListAnimator(null);
            btn.setElevation(8f);
            btn.setTextSize(36);
            btn.setTypeface(null, Typeface.BOLD);

            final int index = i;
            btn.setOnClickListener(v -> onCellClick(btn, index));

            buttons[index] = btn;
            gridVelha.addView(btn);
        }

        btnReset.setOnClickListener(v -> resetGame());
    }

    private void onCellClick(Button btn, int index) {
        if (!btn.getText().toString().equals("")) return;

        btn.setScaleX(0.5f); btn.setScaleY(0.5f);
        btn.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();

        if (isPlayerX) {
            btn.setText("X");
            btn.setTextColor(Color.parseColor("#E74C3C"));
            tvStatus.setText("VEZ DO 'O'");
            tvStatus.setTextColor(Color.parseColor("#27AE60"));
        } else {
            btn.setText("O");
            btn.setTextColor(Color.parseColor("#27AE60"));
            tvStatus.setText("VEZ DO 'X'");
            tvStatus.setTextColor(Color.parseColor("#E74C3C"));
        }

        roundCount++;
        if (checkForWin()) {
            win(isPlayerX ? "X venceu!" : "O venceu!");
        } else if (roundCount == 9) {
            win("Empate!");
        } else {
            isPlayerX = !isPlayerX;
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 9; i++) field[i/3][i%3] = buttons[i].getText().toString();

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) return true;
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) return true;
        }
        return (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) ||
                (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals(""));
    }

    private void win(String message) {
        tvStatus.setText(message.toUpperCase());
        tvStatus.setTextSize(30);
        tvStatus.animate().scaleX(1.2f).scaleY(1.2f).setDuration(500).setInterpolator(new BounceInterpolator()).start();

        if (message.contains("X")) tvStatus.setTextColor(Color.parseColor("#E74C3C"));
        else if (message.contains("O")) tvStatus.setTextColor(Color.parseColor("#27AE60"));
        else tvStatus.setTextColor(Color.GRAY);

        for (Button b : buttons) b.setEnabled(false);
    }

    private void resetGame() {
        for (Button b : buttons) { b.setText(""); b.setEnabled(true); b.setScaleX(1.0f); b.setScaleY(1.0f); }
        roundCount = 0; isPlayerX = true;
        tvStatus.setText("VEZ DO 'X'");
        tvStatus.setTextColor(Color.parseColor("#2D3436"));
        tvStatus.setTextSize(24);
    }

    private void setupNavigationHeader(String nome, String sexo) {
        if (navigationView != null && navigationView.getHeaderCount() > 0) {
            View headerView = navigationView.getHeaderView(0);
            TextView tvNome = headerView.findViewById(R.id.textViewNomeUsuario);
            ImageView imgPerfil = headerView.findViewById(R.id.imagePerfil);
            if (nome != null) tvNome.setText("Olá, " + nome + "!");
            if (sexo != null) imgPerfil.setImageResource(sexo.equalsIgnoreCase("Masculino") ? R.drawable.meny : R.drawable.menx);
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