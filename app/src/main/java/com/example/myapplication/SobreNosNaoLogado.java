package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class SobreNosNaoLogado extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sobre_nos_nao_logado);

        // Ajuste de Insets
        View mainView = findViewById(R.id.drawer_layout);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referências
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        // Abrir Menu
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Configuração do Botão "Entrar no App" dentro do Menu
        View btnEntrarCustom = findViewById(R.id.btn_entrar_custom);
        if (btnEntrarCustom != null) {
            btnEntrarCustom.setOnClickListener(v -> {
                drawerLayout.closeDrawers();
                startActivity(new Intent(this, LoginActivity.class));
            });
        }

        // Lógica dos itens do Rodapé (Footer)
        View btnAjuda = findViewById(R.id.nav_help_footer);
        if (btnAjuda != null) {
            btnAjuda.setOnClickListener(v -> {
                drawerLayout.closeDrawers();
                startActivity(new Intent(SobreNosNaoLogado.this, Ajuda.class));
            });
        }

        // Navigation Item Listener
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Volta para a MainActivity (Apresentação)
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else if (id == R.id.nav_login) {
                // Vai para o Login
                startActivity(new Intent(this, LoginActivity.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
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