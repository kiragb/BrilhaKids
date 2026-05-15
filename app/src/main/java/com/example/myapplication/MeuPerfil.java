package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MeuPerfil extends AppCompatActivity {

    private LinearLayout containerProgresso;
    private ProgressBar progressBar;
    private TextView tvTemaNome, tvPorcentagem, tvStatusTexto, tvMensagemIncentivo;
    private DrawerLayout drawerLayout;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil);

        // 1. INICIALIZAÇÃO
        drawerLayout = findViewById(R.id.drawer_layout);
        containerProgresso = findViewById(R.id.containerProgresso);
        progressBar = findViewById(R.id.progressBrilhante);
        tvTemaNome = findViewById(R.id.tvTemaNome);
        tvPorcentagem = findViewById(R.id.tvPorcentagemCentro);


        // Referências para as mensagens embaixo da barra
        tvStatusTexto = findViewById(R.id.tvStatusTexto);
        tvMensagemIncentivo = findViewById(R.id.tvMensagemIncentivo);

        prefs = getSharedPreferences("BrilhaKidsPrefs", Context.MODE_PRIVATE);

        ImageView menuIcon = findViewById(R.id.menuIcon);
        ImageView btnSair = findViewById(R.id.btnSairTopo);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        // 2. DADOS DO USUÁRIO
        String nome = getIntent().getStringExtra("nome");
        String sexo = getIntent().getStringExtra("sexo");

        ((TextView)findViewById(R.id.tvNomePerfil)).setText(nome);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ((TextView)findViewById(R.id.tvEmailInfo)).setText("E-mail: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }
        ((TextView)findViewById(R.id.tvSexoInfo)).setText("Gênero: " + sexo);

        ImageView ivAvatarCentral = findViewById(R.id.ivAvatarPerfil);
        if (sexo != null && ivAvatarCentral != null) {
            ivAvatarCentral.setImageResource(sexo.equalsIgnoreCase("Masculino") ? R.drawable.meny : R.drawable.menx);
        }

        // 3. CONFIGURAÇÃO DO MENU LATERAL (Header)
        View headerView = navigationView.getHeaderView(0);
        TextView tvNomeMenu = headerView.findViewById(R.id.textViewNomeUsuario);
        ImageView ivAvatarMenu = headerView.findViewById(R.id.imagePerfil);

        if (nome != null && tvNomeMenu != null) tvNomeMenu.setText("Olá, " + nome + "!");
        if (sexo != null && ivAvatarMenu != null) {
            ivAvatarMenu.setImageResource(sexo.equalsIgnoreCase("Masculino") ? R.drawable.meny : R.drawable.menx);
        }

        // 4. LÓGICA DOS CLIQUES
        findViewById(R.id.btnTemaVirtuais).setOnClickListener(v -> {
            int concluidos = prefs.getInt("total_jogo_concluida", 0);
            int total = 4;
            toggleProgresso("Jogos Virtuais", concluidos, total, "#00B050", "Continue assim!!");
        });

        findViewById(R.id.btnTemaLicoes).setOnClickListener(v -> {
            int concluidos = prefs.getInt("total_licao_concluida", 0);
            int total = 11;
            toggleProgresso("Lições", concluidos, total, "#D50326", "Você é nota dez!");
        });

        findViewById(R.id.btnTemaMusicas).setOnClickListener(v -> {
            int concluidos = prefs.getInt("total_musica_concluida", 0);
            int total = 5;
            toggleProgresso("Músicas", concluidos, total, "#FFC107", "Que ritmo legal!");
        });

        findViewById(R.id.btnTemaManuais).setOnClickListener(v -> {
            int concluidos = prefs.getInt("total_jogosm_concluida", 0);
            int total = 8;
            toggleProgresso("Jogos Manuais", concluidos, total, "#260066", "Muito criativo!");
        });

        // 5. MENU E SAÍDA
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

    // Método Toggle atualizado com ANIMAÇÃO de Alpha
    private void toggleProgresso(String tema, int concluidos, int total, String corHex, String incentivo) {
        int porcentagem = (total > 0) ? (concluidos * 100) / total : 0;
        int cor = Color.parseColor(corHex);

        // Se o container já está visível e clicamos no mesmo tema, ele desaparece com animação
        if (containerProgresso.getVisibility() == View.VISIBLE && tvTemaNome.getText().toString().equals(tema)) {
            containerProgresso.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction(() -> containerProgresso.setVisibility(View.GONE));
        } else {
            // Se for um novo tema ou estava fechado, ele aparece suavemente
            containerProgresso.setAlpha(0f);
            containerProgresso.setVisibility(View.VISIBLE);
            containerProgresso.animate()
                    .alpha(1f)
                    .setDuration(400)
                    .start();

            // Atualização dos dados
            tvTemaNome.setText(tema);
            tvTemaNome.setTextColor(cor);
            tvPorcentagem.setText(porcentagem + "%");
            tvPorcentagem.setTextColor(cor);

            // Atualiza os textos dinâmicos embaixo da barra
            tvStatusTexto.setText("Você tem " + concluidos + " de " + total + " feitos");
            tvMensagemIncentivo.setText(incentivo);

            // Atualiza a cor e o progresso da barra circular
            progressBar.setProgress(porcentagem);
            progressBar.getProgressDrawable().setColorFilter(cor, PorterDuff.Mode.SRC_IN);
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