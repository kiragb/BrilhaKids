package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroActivity extends AppCompatActivity {

    EditText etNome, etEmail, etSenha, etReptSenha;
    Button btnCadastrar;
    CheckBox cbMostrarSenha;
    TextView tvJaPossuiCadastro;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    RadioGroup radioGroupSexo; // Adicione esta linha

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Habilita persistência offline para ajudar em conexões instáveis
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            // Caso já tenha sido ativado em outra tela
        }

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etReptSenha = findViewById(R.id.etReptSenha);
        cbMostrarSenha = findViewById(R.id.cbMostrarSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        tvJaPossuiCadastro = findViewById(R.id.tvJaPossuiCadastro);
        radioGroupSexo = findViewById(R.id.radioGroupSexo);

        mAuth = FirebaseAuth.getInstance();

        // Inicialização do Database com a URL do seu projeto BrilhaKidsDB
        mDatabase = FirebaseDatabase.getInstance("https://brilhakidsdb-default-rtdb.firebaseio.com/").getReference();

        cbMostrarSenha.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etSenha.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etReptSenha.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etReptSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            etSenha.setTypeface(ResourcesCompat.getFont(this, R.font.adigianaui));
            etReptSenha.setTypeface(ResourcesCompat.getFont(this, R.font.adigianaui));
        });

        btnCadastrar.setOnClickListener(v -> cadastrarUsuario());
        tvJaPossuiCadastro.setOnClickListener(v -> irParaLogin());
    }

    private void cadastrarUsuario() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String reptSenha = etReptSenha.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || reptSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(reptSenha)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        btnCadastrar.setEnabled(false);
        btnCadastrar.setText("Processando...");

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

// Lógica para pegar o sexo selecionado
                        int idSelecionado = radioGroupSexo.getCheckedRadioButtonId();
                        String sexoSelecionado = "Não Definido";

                        if (idSelecionado != -1) {
                            RadioButton rbSelecionado = findViewById(idSelecionado);
                            sexoSelecionado = rbSelecionado.getText().toString(); // Pegará "Masculino" ou "Feminino"
                        }

// Agora passamos a variável correta para o objeto
                        Usuario usuario = new Usuario(nome, email, sexoSelecionado);

                        Log.d("Cadastro", "Auth OK! UID: " + userId);

                        // 1. TENTA SALVAR NO DATABASE
                        mDatabase.child("usuarios").child(userId).setValue(usuario)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Log.d("Cadastro", "Sucesso total no DB!");
                                        Toast.makeText(CadastroActivity.this, "Cadastro realizado!", Toast.LENGTH_SHORT).show();
                                        irParaLogin();
                                    } else {
                                        Log.e("Cadastro", "Erro no DB: " + dbTask.getException().getMessage());
                                    }
                                });

                        // 2. REDIRECIONAMENTO DE SEGURANÇA (Caso o Database demore a responder)
                        // Se em 3 segundos o código acima não disparar o irParaLogin(), este aqui dispara.
                        new Handler().postDelayed(() -> {
                            if (!isFinishing()) {
                                Log.d("Cadastro", "Redirecionamento automático (Timeout)");
                                Toast.makeText(this, "Cadastro processado com sucesso!", Toast.LENGTH_SHORT).show();
                                irParaLogin();
                            }
                        }, 3000);

                    } else {
                        btnCadastrar.setEnabled(true);
                        btnCadastrar.setText("Criar Conta");
                        String erroAuth = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                        Log.e("Cadastro", "Erro no Auth: " + erroAuth);
                        Toast.makeText(this, "Erro: " + erroAuth, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void irParaLogin() {
        // Removemos o signOut para evitar que o Firebase bloqueie a escrita por falta de login no momento do redirecionamento
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public static class Usuario {
        public String nome, email, sexo;
        public Usuario() {}
        public Usuario(String nome, String email, String sexo) {
            this.nome = nome;
            this.email = email;
            this.sexo = sexo;
        }
    }
}