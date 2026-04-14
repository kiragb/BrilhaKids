package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox; // Importar CheckBox
import android.widget.ImageView;
import android.widget.TextView;
import android.view.WindowManager;
import android.widget.Toast;
import android.content.ActivityNotFoundException;
import android.content.SharedPreferences; // Importar SharedPreferences

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

public class EscovarOsDentinhos extends AppCompatActivity {

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
    private CheckBox checkboxConcluido; // Referência para o CheckBox
    private SharedPreferences sharedPreferences; // Para salvar o estado

    // Chave para SharedPreferences (boa prática usar constantes)
    private static final String PREFS_NAME = "brilhaKidsPrefs";
    private static final String KEY_ESCOVAR_CONCLUIDO = "escovarDentinhosConcluido";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_escovar_os_dentinhos);

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

            // Ao navegar, enviamos o estado atualizado do progresso
            // para a Activity de destino, especialmente para MeuPerfil.
            Intent intent;
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
                // Adicione os dados de progresso ao Intent para MeuPerfil
                intent.putExtra(KEY_ESCOVAR_CONCLUIDO, checkboxConcluido.isChecked());
            } else {
                return true; // Nenhuma ação para outros itens
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("nome", nome);
            intent.putExtra("sexo", sexo);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // PLAYER (seu código existente)
        playerView = findViewById(R.id.playerView);
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.escovarvideo);
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

                    fundoLaranjaLayout.addView(playerView, 0);
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

        // --- Lógica para o PDF (seu código existente) ---
        pdfImageView = findViewById(R.id.pdf);
        pdfImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdf();
            }
        });
        // --- Fim da Lógica para o PDF ---

        // --- NOVO: Lógica para o CheckBox de Conclusão ---
        checkboxConcluido = findViewById(R.id.checkboxConcluido);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Carrega o estado salvo do checkbox
        boolean isConcluido = sharedPreferences.getBoolean(KEY_ESCOVAR_CONCLUIDO, false);
        checkboxConcluido.setChecked(isConcluido);

        // Listener para salvar o estado quando o checkbox é marcado/desmarcado
        checkboxConcluido.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_ESCOVAR_CONCLUIDO, isChecked);
            editor.apply(); // Salva assincronamente

            if (isChecked) {
                Toast.makeText(EscovarOsDentinhos.this, "Atividade 'Escovar os Dentinhos' marcada como concluída!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EscovarOsDentinhos.this, "Atividade 'Escovar os Dentinhos' desmarcada.", Toast.LENGTH_SHORT).show();
            }
            // Não é necessário enviar o Intent para MeuPerfil aqui, pois os dados serão
            // lidos no onResume de MeuPerfil ou quando o usuário navegar para lá.
        });
        // --- Fim da Lógica para o CheckBox ---
    }

    // Método para abrir o PDF (seu código existente)
    private void openPdf() {
        File pdfFile = new File(getCacheDir(), "atividade-escovar-dentinhos.pdf");
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.atividade_escovar_dentinhos);
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
            Toast.makeText(this, "Erro ao copiar o PDF.", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri pdfUri = FileProvider.getUriForFile(
                this,
                getApplicationContext().getPackageName() + ".fileprovider",
                pdfFile
        );

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Nenhum aplicativo encontrado para visualizar PDF.", Toast.LENGTH_SHORT).show();
        }
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