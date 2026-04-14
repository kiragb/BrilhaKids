package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etSenha;
    Button btnEntrar;
    TextView tvEsqueceuSenha, tvCadastrar;
    CheckBox cbMostrarSenha;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmailLogin);
        etSenha = findViewById(R.id.etSenhaLogin);
        btnEntrar = findViewById(R.id.btnEntrar);
        tvEsqueceuSenha = findViewById(R.id.tvEsqueceuSenha);
        tvCadastrar = findViewById(R.id.tvCadastrar);
        cbMostrarSenha = findViewById(R.id.cbMostrarSenha);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnEntrar.setOnClickListener(v -> realizarLogin());

        cbMostrarSenha.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etSenha.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            etSenha.setTypeface(ResourcesCompat.getFont(this, R.font.adigianaui));
        });

        tvEsqueceuSenha.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecuperarSenhaActivity.class);
            startActivity(intent);
        });

        tvCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadastroActivity.class);
            startActivity(intent);
        });
    }

    private void realizarLogin() {
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login bem-sucedido
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Buscar dados extras no Realtime Database
                            mDatabase.child("usuarios").child(user.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                String nome = snapshot.child("nome").getValue(String.class);
                                                String sexo = snapshot.child("sexo").getValue(String.class);

                                                Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(LoginActivity.this, MainLoggedActivity.class);
                                                intent.putExtra("nome", nome);
                                                intent.putExtra("sexo", sexo);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Dados do usuário não encontrados.", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            Toast.makeText(LoginActivity.this, "Erro ao buscar dados: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "E-mail ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
