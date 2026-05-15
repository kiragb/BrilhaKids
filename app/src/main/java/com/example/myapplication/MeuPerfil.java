package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build; // Importante para transparência
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge; // Importante para transparência
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowInsetsControllerCompat; // Importante para ícones escuros
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class MeuPerfil extends AppCompatActivity {

    private LinearLayout containerProgresso;
    private ProgressBar progressBar;
    private TextView tvTemaNome, tvPorcentagem, tvStatusTexto, tvMensagemIncentivo;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView; // Movido para global para facilitar acesso
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. ATIVAR TRANSPARÊNCIA E TELA CHEIA
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meu_perfil);

        // Configurações específicas para a transparência não ficar cinza/branca
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getWindow().setNavigationBarContrastEnforced(false);
        }
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        // Garante que os ícones do sistema (bateria, hora) fiquem escuros no fundo creme
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightNavigationBars(true);
        controller.setAppearanceLightStatusBars(true);

        // 2. INICIALIZAÇÃO DE COMPONENTES
        drawerLayout = findViewById(R.id.drawer_layout);
        containerProgresso = findViewById(R.id.containerProgresso);
        progressBar = findViewById(R.id.progressBrilhante);
        tvTemaNome = findViewById(R.id.tvTemaNome);
        tvPorcentagem = findViewById(R.id.tvPorcentagemCentro);
        tvStatusTexto = findViewById(R.id.tvStatusTexto);
        tvMensagemIncentivo = findViewById(R.id.tvMensagemIncentivo);
        navigationView = findViewById(R.id.navigation_view);

        prefs = getSharedPreferences("BrilhaKidsPrefs", Context.MODE_PRIVATE);

        ImageView menuIcon = findViewById(R.id.menuIcon);
        ImageView btnSair = findViewById(R.id.btnSairTopo);
        navigationView.setItemIconTintList(null);

        // 3. DADOS DO USUÁRIO E DINÂMICA DO MENU
        String nome = getIntent().getStringExtra("nome");
        String sexo = getIntent().getStringExtra("sexo");

        configurarFraseDinamica();

        ((TextView)findViewById(R.id.tvNomePerfil)).setText(nome);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ((TextView)findViewById(R.id.tvEmailInfo)).setText("E-mail: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }
        ((TextView)findViewById(R.id.tvSexoInfo)).setText("Gênero: " + sexo);

        ImageView ivAvatarCentral = findViewById(R.id.ivAvatarPerfil);
        if (sexo != null && ivAvatarCentral != null) {
            ivAvatarCentral.setImageResource(sexo.equalsIgnoreCase("Masculino") ? R.drawable.meny : R.drawable.menx);
        }

        // Header do Menu Lateral
        if (navigationView.getHeaderCount() > 0) {
            View headerView = navigationView.getHeaderView(0);
            TextView tvNomeMenu = headerView.findViewById(R.id.textViewNomeUsuario);
            ImageView ivAvatarMenu = headerView.findViewById(R.id.imagePerfil);

            if (nome != null && tvNomeMenu != null) tvNomeMenu.setText("Olá, " + nome + "!");
            if (sexo != null && ivAvatarMenu != null) {
                ivAvatarMenu.setImageResource(sexo.equalsIgnoreCase("Masculino") ? R.drawable.meny : R.drawable.menx);
            }
        }

        // 4. LÓGICA DOS BOTÕES DE PROGRESSO
        findViewById(R.id.btnTemaVirtuais).setOnClickListener(v -> {
            int concluidos = prefs.getInt("total_jogo_concluida", 0);
            toggleProgresso("Jogos Virtuais", concluidos, 4, "#00B050", "Continue assim!!");
        });

        findViewById(R.id.btnTemaLicoes).setOnClickListener(v -> {
            int concluidos = prefs.getInt("total_licao_concluida", 0);
            toggleProgresso("Lições", concluidos, 11, "#D50326", "Você é nota dez!");
        });

        findViewById(R.id.btnTemaMusicas).setOnClickListener(v -> {
            int concluidos = prefs.getInt("total_musica_concluida", 0);
            toggleProgresso("Músicas", concluidos, 5, "#FFC107", "Que ritmo legal!");
        });

        findViewById(R.id.btnTemaManuais).setOnClickListener(v -> {
            int concluidos = prefs.getInt("total_jogosm_concluida", 0);
            toggleProgresso("Jogos Manuais", concluidos, 8, "#260066", "Muito criativo!");
        });

        // 5. NAVEGAÇÃO
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        btnSair.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;
            if (id == R.id.nav_home) intent = new Intent(this, MainLoggedActivity.class);
            else if (id == R.id.nav_sobre) intent = new Intent(this, SobreNos.class);

            if (intent != null) {
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void toggleProgresso(String tema, int concluidos, int total, String corHex, String incentivo) {
        int porcentagem = (total > 0) ? (concluidos * 100) / total : 0;
        int cor = Color.parseColor(corHex);

        if (containerProgresso.getVisibility() == View.VISIBLE && tvTemaNome.getText().toString().equals(tema)) {
            containerProgresso.animate().alpha(0f).setDuration(200).withEndAction(() -> containerProgresso.setVisibility(View.GONE));
        } else {
            containerProgresso.setAlpha(0f);
            containerProgresso.setVisibility(View.VISIBLE);
            containerProgresso.animate().alpha(1f).setDuration(400).start();

            tvTemaNome.setText(tema);
            tvTemaNome.setTextColor(cor);
            tvPorcentagem.setText(porcentagem + "%");
            tvPorcentagem.setTextColor(cor);
            tvStatusTexto.setText("Você tem " + concluidos + " de " + total + " feitos");
            tvMensagemIncentivo.setText(incentivo);

            progressBar.setProgress(porcentagem);
            progressBar.getProgressDrawable().setColorFilter(cor, PorterDuff.Mode.SRC_IN);
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}