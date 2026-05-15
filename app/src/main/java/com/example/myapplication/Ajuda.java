package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class Ajuda extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajuda);

        // 1. REFERÊNCIAS (Declaradas apenas uma vez)
        drawerLayout = findViewById(R.id.drawer_layout_ajuda);
        navigationView = findViewById(R.id.navigation_view);
        ImageView btnMenu = findViewById(R.id.btnMenuAjuda);

        CardView cardFaleConosco = findViewById(R.id.cardFaleConoscoAjuda);
        CardView cardComoUsar = findViewById(R.id.cardComoUsar);
        CardView cardEspacoPais = findViewById(R.id.cardEspacoPais);

        // Ajuste de Insets
        ViewCompat.setOnApplyWindowInsetsListener(drawerLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. AÇÕES DO MENU
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }

        View btnEntrarMenu = navigationView.findViewById(R.id.btn_entrar_custom);
        if (btnEntrarMenu != null) {
            btnEntrarMenu.setOnClickListener(v -> {
                drawerLayout.closeDrawers();
                startActivity(new Intent(Ajuda.this, LoginActivity.class));
            });
        }

        // 3. CLIQUE NO CARD COMO EXPLORAR
        cardComoUsar.setOnClickListener(v -> {
            android.app.Dialog dialog = new android.app.Dialog(Ajuda.this);
            dialog.setContentView(R.layout.layout_como_explorar);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            Button btnFechar = dialog.findViewById(R.id.btnFecharGuia);
            if (btnFechar != null) {
                btnFechar.setOnClickListener(view -> dialog.dismiss());
            }

            dialog.show();
        });

        // 4. CLIQUE NO CARD ESPAÇO DOS PAIS (CORRIGIDO: Removido 'CardView' duplicado)
        cardEspacoPais.setOnClickListener(v -> {
            android.app.Dialog dialog = new android.app.Dialog(Ajuda.this);
            dialog.setContentView(R.layout.layout_espaco_pais);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            Button btnFechar = dialog.findViewById(R.id.btnFecharPais);
            if (btnFechar != null) {
                btnFechar.setOnClickListener(view -> dialog.dismiss());
            }

            dialog.show();
        });

        cardFaleConosco.setOnClickListener(v -> {
            Intent intent = new Intent(Ajuda.this, FaleConosco.class);
            intent.putExtra("nome", getIntent().getStringExtra("nome"));
            intent.putExtra("sexo", getIntent().getStringExtra("sexo"));
            startActivity(intent);
        });

        // 6. RODAPÉ E ITENS DO MENU
        View footerSobre = navigationView.findViewById(R.id.nav_about_footer);
        if (footerSobre != null) {
            footerSobre.setOnClickListener(v -> {
                drawerLayout.closeDrawers();
                startActivity(new Intent(this, SobreNosNaoLogado.class));
            });
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (id == R.id.nav_login) {
                startActivity(new Intent(this, LoginActivity.class));
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
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