package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

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

        // --- 2. CONFIGURAÇÃO DO MENU LATERAL E DADOS DO USUÁRIO ---
        String nome = getIntent().getStringExtra("nome");
        String sexo = getIntent().getStringExtra("sexo");
        setupNavigationHeader(nome, sexo);

        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(this, MainLoggedActivity.class);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            } else if (id == R.id.nav_senha) {
                Intent intent = new Intent(this, AlterarSenhaActivity.class);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            } else if (id == R.id.nav_sobre) {
                Intent intent = new Intent(this, SobreNos.class);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            } else if (id == R.id.nav_sair) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_perfil) {
                Intent intent = new Intent(this, MeuPerfil.class);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // --- 3. LÓGICA DE CRIAÇÃO DO TABULEIRO ---
        gridVelha.removeAllViews();
        for (int i = 0; i < 9; i++) {
            Button btn = new Button(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.columnSpec = GridLayout.spec(i % 3, 1f);
            params.rowSpec = GridLayout.spec(i / 3, 1f);
            params.setMargins(10, 10, 10, 10);
            btn.setLayoutParams(params);

            // Estilização das Células
            btn.setBackgroundResource(R.drawable.card_velha);
            btn.setStateListAnimator(null);
            btn.setElevation(8f);
            btn.setTextSize(36);
            btn.setTypeface(null, android.graphics.Typeface.BOLD);

            final int index = i;
            btn.setOnClickListener(v -> onCellClick(btn, index));

            buttons[index] = btn;
            gridVelha.addView(btn);
        }

        btnReset.setOnClickListener(v -> resetGame());
    }

    private void onCellClick(Button btn, int index) {
        if (!btn.getText().toString().equals("")) return;

        // Animação Pop-in
        btn.setScaleX(0.5f);
        btn.setScaleY(0.5f);
        btn.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();

        if (isPlayerX) {
            btn.setText("X");
            btn.setTextColor(Color.parseColor("#E74C3C")); // Vermelho
            tvStatus.setText("VEZ DO 'O'");
            tvStatus.setTextColor(Color.parseColor("#27AE60"));
        } else {
            btn.setText("O");
            btn.setTextColor(Color.parseColor("#27AE60")); // Verde
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
        for (int i = 0; i < 9; i++) {
            field[i / 3][i % 3] = buttons[i].getText().toString();
        }

        // Linhas e Colunas
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals(""))
                return true;
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals(""))
                return true;
        }

        // Diagonais
        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals(""))
            return true;
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals(""))
            return true;

        return false;
    }

    private void win(String message) {
        tvStatus.setText(message.toUpperCase());
        tvStatus.setTextSize(30); // Correção: Apenas o valor numérico (sem "sp")

        // Animação de Vitória
        tvStatus.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(500)
                .setInterpolator(new android.view.animation.BounceInterpolator())
                .start();

        if (message.contains("X")) {
            tvStatus.setTextColor(Color.parseColor("#E74C3C"));
        } else if (message.contains("O")) {
            tvStatus.setTextColor(Color.parseColor("#27AE60"));
        } else {
            tvStatus.setTextColor(Color.GRAY);
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        for (Button b : buttons) b.setEnabled(false);
    }

    private void resetGame() {
        for (Button b : buttons) {
            b.setText("");
            b.setEnabled(true);
            b.setScaleX(1.0f);
            b.setScaleY(1.0f);
        }
        roundCount = 0;
        isPlayerX = true;
        tvStatus.setText("VEZ DO 'X'");
        tvStatus.setTextColor(Color.parseColor("#2D3436"));
        tvStatus.setTextSize(24);
    }

    private void setupNavigationHeader(String nome, String sexo) {
        if (navigationView != null && navigationView.getHeaderCount() > 0) {
            View headerView = navigationView.getHeaderView(0);
            TextView textViewNome = headerView.findViewById(R.id.textViewNomeUsuario);
            ImageView imagePerfil = headerView.findViewById(R.id.imagePerfil);

            if (nome != null && textViewNome != null) textViewNome.setText("Olá, " + nome + "!");
            if (sexo != null && imagePerfil != null) {
                if (sexo.equalsIgnoreCase("Masculino")) {
                    imagePerfil.setImageResource(R.drawable.meny);
                } else {
                    imagePerfil.setImageResource(R.drawable.menx);
                }
            }
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