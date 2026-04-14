package com.example.myapplication;

import static com.example.myapplication.R.id.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class FaleConosco extends AppCompatActivity {

    private EditText nomeEditText, emailEditText, telefoneEditText, comentarioEditText;
    private Button btnEnviar;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fale_conosco);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        // Referências
        nomeEditText = findViewById(R.id.Nome);
        emailEditText = findViewById(R.id.Email);
        telefoneEditText = findViewById(R.id.telefone);
        comentarioEditText = findViewById(R.id.comentario);
        btnEnviar = findViewById(R.id.btnCadastrar);

        btnEnviar.setOnClickListener(v -> {
            String nome = nomeEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String telefone = telefoneEditText.getText().toString();
            String comentario = comentarioEditText.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || comentario.isEmpty()) {
                Toast.makeText(FaleConosco.this, "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Aqui você pode salvar no banco ou enviar para um servidor
            Toast.makeText(FaleConosco.this, "Mensagem enviada com sucesso!", Toast.LENGTH_LONG).show();
            limparCampos();
        });


        // Referências
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        // Abre o menu ao clicar na imagem
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        View headerView = navigationView.getHeaderView(0);

        String sexo = getIntent().getStringExtra("sexo");

        if (sexo != null) {
            ImageView imagePerfil = headerView.findViewById(R.id.imagePerfil); // Pegue a referência do ImageView do header
            if (imagePerfil != null) {
                if (sexo.equalsIgnoreCase("Masculino")) {
                    imagePerfil.setImageResource(R.drawable.meny);
                } else if (sexo.equalsIgnoreCase("Feminino")) {
                    imagePerfil.setImageResource(R.drawable.menx);
                }
            }
        }

        // Mostrar nome do usuário no TextView do header
        String nome = getIntent().getStringExtra("nome");

        if (nome != null) {
            // Atualiza saudação principal no fundo laranja

            // Atualiza nome no header do menu lateral
            TextView textViewNome = headerView.findViewById(R.id.textViewNomeUsuario);
            if (textViewNome != null) {
                textViewNome.setText("Olá, " + nome + "!");
            }
        }

        // Este é o único setNavigationItemSelectedListener que você precisa
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
    }

    private void limparCampos() {
        nomeEditText.setText("");
        emailEditText.setText("");
        telefoneEditText.setText("");
        comentarioEditText.setText("");
    }
}
