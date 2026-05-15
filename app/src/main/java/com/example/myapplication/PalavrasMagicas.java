package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.WindowManager;
import android.widget.Toast;
import android.content.ActivityNotFoundException;
import android.content.SharedPreferences;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Random;

public class PalavrasMagicas extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private ExoPlayer exoPlayer;
    private boolean isFullscreen = false;
    private PlayerView playerView;
    private ConstraintLayout mainLayout;
    private ConstraintLayout fundoLaranjaLayout;
    private ConstraintLayout.LayoutParams playerViewOriginalParams;
    private ConstraintSet originalFundoLaranjaConstraints;

    private ImageView pdfImageView;
    private CheckBox checkboxConcluido;
    private SharedPreferences preferences;

    // Chaves para o sistema de progresso do BrilhaKids
    private static final String PREFS_NAME = "BrilhaKidsPrefs";
    private static final String KEY_CHECK_LOCAL = "concluido_palavras_magicas";
    private static final String KEY_TOTAL_LICOES = "total_licao_concluida";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_palavras_magicas);

        // Ajuste de Padding para EdgeToEdge
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // --- REFERÊNCIAS DO MENU ---
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        configurarFraseDinamica();

        // --- DADOS DO USUÁRIO ---
        final String nome = getIntent().getStringExtra("nome");
        final String sexo = getIntent().getStringExtra("sexo");

        configurarHeaderMenu(nome, sexo);

        // Listeners do Menu Lateral
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_home) intent = new Intent(this, MainLoggedActivity.class);
            else if (id == R.id.nav_senha) intent = new Intent(this, AlterarSenhaActivity.class);
            else if (id == R.id.nav_sobre) intent = new Intent(this, SobreNos.class);
            else if (id == R.id.nav_perfil) intent = new Intent(this, MeuPerfil.class);
            else if (id == R.id.nav_sair) intent = new Intent(this, MainActivity.class);

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // --- CONFIGURAÇÃO DO PLAYER (ExoPlayer) ---
        playerView = findViewById(R.id.playerView);
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.palavras);
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();

        configurarFullscreen();

        // --- LÓGICA DO PDF ---
        pdfImageView = findViewById(R.id.pdf);
        if (pdfImageView != null) {
            pdfImageView.setOnClickListener(v -> openPdf());
        }

        // --- LÓGICA DE PROGRESSO (SALVAMENTO) ---
        checkboxConcluido = findViewById(R.id.checkboxConcluidoMagicas);
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Carrega estado anterior
        boolean isConcluido = preferences.getBoolean(KEY_CHECK_LOCAL, false);
        if (checkboxConcluido != null) {
            checkboxConcluido.setChecked(isConcluido);

            checkboxConcluido.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_CHECK_LOCAL, isChecked);

                // Atualiza o total de LIÇÕES concluídas para o perfil
                int totalAtual = preferences.getInt(KEY_TOTAL_LICOES, 0);
                if (isChecked) {
                    editor.putInt(KEY_TOTAL_LICOES, totalAtual + 1);
                    Toast.makeText(this, "Palavras Mágicas concluídas! ✨", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putInt(KEY_TOTAL_LICOES, Math.max(0, totalAtual - 1));
                }
                editor.apply();
            });
        }
    }

    private void configurarHeaderMenu(String nome, String sexo) {
        if (navigationView.getHeaderCount() > 0) {
            View headerView = navigationView.getHeaderView(0);
            ImageView imagePerfil = headerView.findViewById(R.id.imagePerfil);
            TextView textViewNome = headerView.findViewById(R.id.textViewNomeUsuario);

            if (sexo != null && imagePerfil != null) {
                imagePerfil.setImageResource(sexo.equalsIgnoreCase("Masculino") ? R.drawable.meny : R.drawable.menx);
            }
            if (nome != null && textViewNome != null) {
                textViewNome.setText("Olá, " + nome + "!");
            }
        }
    }

    private void configurarFullscreen() {
        playerViewOriginalParams = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
        mainLayout = findViewById(R.id.main);
        fundoLaranjaLayout = findViewById(R.id.fundo_laranja);
        originalFundoLaranjaConstraints = new ConstraintSet();
        originalFundoLaranjaConstraints.clone(fundoLaranjaLayout);

        playerView.setFullscreenButtonClickListener(isCurrentFullscreen -> {
            if (isCurrentFullscreen) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ViewGroup parentLayout = (ViewGroup) playerView.getParent();
                if (parentLayout != null) parentLayout.removeView(playerView);
                fundoLaranjaLayout.addView(playerView, 0);
                playerView.setLayoutParams(playerViewOriginalParams);
                originalFundoLaranjaConstraints.applyTo(fundoLaranjaLayout);
                menuIcon.setVisibility(View.VISIBLE);
                findViewById(R.id.logo).setVisibility(View.VISIBLE);
                isFullscreen = false;
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                menuIcon.setVisibility(View.GONE);
                findViewById(R.id.logo).setVisibility(View.GONE);
                ViewGroup parentLayout = (ViewGroup) playerView.getParent();
                if (parentLayout != null) parentLayout.removeView(playerView);
                mainLayout.addView(playerView);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(mainLayout);
                constraintSet.connect(playerView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                constraintSet.connect(playerView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                constraintSet.applyTo(mainLayout);
                isFullscreen = true;
            }
        });
    }

    private void openPdf() {
        File pdfFile = new File(getCacheDir(), "atividade-palavras-magicas.pdf");
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.palavrinhasmagicas);
            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Uri pdfUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Instale um leitor de PDF.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (exoPlayer != null) exoPlayer.release();
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
        if (isFullscreen) {
            playerView.performClick();
        } else if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}