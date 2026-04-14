package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.WindowManager;
import android.widget.Toast; // Importar para mensagens curtas
import android.content.ActivityNotFoundException; // Importar para lidar com apps não encontrados

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider; // Importar para FileProvider
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

public class Banho extends AppCompatActivity {

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

    private ImageView pdfImageView; // Referência para a ImageView do PDF

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_banho);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        View headerView = navigationView.getHeaderView(0);
        String sexo = getIntent().getStringExtra("sexo");
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

        String nome = getIntent().getStringExtra("nome");
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

        // PLAYER
        playerView = findViewById(R.id.playerView);
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fazenda);
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();

        playerViewOriginalParams = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
        mainLayout = findViewById(R.id.main);
        fundoLaranjaLayout = findViewById(R.id.fundo_laranja);

        originalFundoLaranjaConstraints = new ConstraintSet();
        originalFundoLaranjaConstraints.clone(fundoLaranjaLayout);

        playerView.setFullscreenButtonClickListener(new PlayerView.FullscreenButtonClickListener() {
            @Override
            public void onFullscreenButtonClick(boolean isCurrentFullscreen) {
                if (isCurrentFullscreen) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    ViewCompat.getWindowInsetsController(getWindow().getDecorView())
                            .show(WindowInsetsCompat.Type.systemBars());

                    ViewGroup parentLayout = (ViewGroup) playerView.getParent();
                    if (parentLayout != null) {
                        parentLayout.removeView(playerView);
                    }

                    fundoLaranjaLayout.addView(playerView, 0); // Adiciona o PlayerView de volta ao fundo_laranja
                    playerView.setLayoutParams(playerViewOriginalParams);
                    originalFundoLaranjaConstraints.applyTo(fundoLaranjaLayout);

                    menuIcon.setVisibility(View.VISIBLE);
                    findViewById(R.id.logo).setVisibility(View.VISIBLE);
                    findViewById(R.id.fundo_laranja).setVisibility(View.VISIBLE);

                    isFullscreen = false;
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    ViewCompat.getWindowInsetsController(getWindow().getDecorView())
                            .hide(WindowInsetsCompat.Type.systemBars());

                    menuIcon.setVisibility(View.GONE);
                    findViewById(R.id.logo).setVisibility(View.GONE);
                    findViewById(R.id.fundo_laranja).setVisibility(View.GONE);

                    ViewGroup parentLayout = (ViewGroup) playerView.getParent();
                    if (parentLayout != null) {
                        parentLayout.removeView(playerView);
                    }
                    mainLayout.addView(playerView);

                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(mainLayout);

                    constraintSet.clear(playerView.getId());
                    constraintSet.connect(playerView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                    constraintSet.connect(playerView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
                    constraintSet.connect(playerView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                    constraintSet.connect(playerView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

                    constraintSet.applyTo(mainLayout);

                    isFullscreen = true;
                }
            }
        });

    }




    @Override
    protected void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }

    @Override
    public void onBackPressed() {
        if (isFullscreen) {
            playerView.performClick();
        } else {
            super.onBackPressed();
        }
    }
}