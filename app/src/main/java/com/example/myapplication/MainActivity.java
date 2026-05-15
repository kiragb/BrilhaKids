package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- 1. LÓGICA DE SEGURANÇA PARA REINSTALAÇÃO E SESSÃO ---
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getSharedPreferences("BrilhaKidsPrefs", Context.MODE_PRIVATE);

        // Buscamos o nome sem valor padrão para saber se o arquivo existe
        String nomeSalvo = prefs.getString("nome_usuario", null);

        if (usuarioAtual != null && nomeSalvo != null) {
            // Caso PERFEITO: Logado no Firebase E com dados salvos no celular
            Intent intent = new Intent(MainActivity.this, MainLoggedActivity.class);
            intent.putExtra("nome", nomeSalvo);
            intent.putExtra("sexo", prefs.getString("sexo_usuario", "Masculino"));
            startActivity(intent);
            finish();
            return;
        }
        else if (usuarioAtual != null && nomeSalvo == null) {
            // Caso de REINSTALAÇÃO: O Firebase lembra o login, mas o celular esqueceu o nome.
            // Forçamos o logout para que ele veja a tela de apresentação e logue de novo.
            FirebaseAuth.getInstance().signOut();
        }

        // --- 2. CONFIGURAÇÃO DA INTERFACE (SÓ APARECE SE NÃO ESTIVER LOGADO) ---
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        // Ajuste de Insets para telas modernas
        View mainView = findViewById(R.id.drawer_layout);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Texto de Boas-vindas (caso venha de algum redirecionamento)
        TextView tvOlaUsuario = findViewById(R.id.tvOlaUsuario);
        String nomeUsuario = getIntent().getStringExtra("nomeUsuario");
        if (nomeUsuario != null) {
            tvOlaUsuario.setText("Olá, " + nomeUsuario + "! Tudo Bem?");
        }

        // Configuração do Menu Lateral
        ImageView menuIcon = findViewById(R.id.menuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));
        }

        // Botão Entrar que leva ao Login
        View btnEntrarCustom = findViewById(R.id.btn_entrar_custom);
        if (btnEntrarCustom != null) {
            btnEntrarCustom.setOnClickListener(v -> {
                drawerLayout.closeDrawers();
                irParaLoginActivity();
            });
        }

        // Botões do Rodapé
        configurarRodape();

        // Cliques nos Cards da Home (mostram o aviso de login)
        configurarCliquesCards();
    }

    private void configurarRodape() {
        View btnSobre = findViewById(R.id.nav_about_footer);
        View btnAjuda = findViewById(R.id.nav_help_footer);

        if (btnSobre != null) {
            btnSobre.setOnClickListener(v -> {
                drawerLayout.closeDrawers();
                startActivity(new Intent(MainActivity.this, SobreNosNaoLogado.class));
            });
        }

        if (btnAjuda != null) {
            btnAjuda.setOnClickListener(v -> {
                drawerLayout.closeDrawers();
                startActivity(new Intent(MainActivity.this, Ajuda.class));
            });
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_login) {
                drawerLayout.closeDrawers();
                irParaLoginActivity();
                return true;
            }
            return false;
        });
    }

    private void configurarCliquesCards() {
        View.OnClickListener avisoLogin = v -> {
            Toast.makeText(MainActivity.this,
                    "Faça login para explorar essas atividades! ✨",
                    Toast.LENGTH_SHORT).show();
        };

        // IDs de todos os componentes que devem disparar o aviso
        int[] ids = {
                R.id.cardJogos, R.id.imageView3, R.id.textjogos,
                R.id.cardLicoes, R.id.imageView4, R.id.textView2,
                R.id.cardMusicas, R.id.imageView, R.id.textView,
                R.id.cardJogosManuais, R.id.imageView2, R.id.textjogosmanuais,
                R.id.carddicas, R.id.imageView6, R.id.textView3,
                R.id.cardfale, R.id.imageView5, R.id.textView5
        };

        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) view.setOnClickListener(avisoLogin);
        }
    }

    private void irParaLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}