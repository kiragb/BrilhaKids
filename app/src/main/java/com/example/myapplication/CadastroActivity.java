package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
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
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etReptSenha = findViewById(R.id.etReptSenha);
        cbMostrarSenha = findViewById(R.id.cbMostrarSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        cbMostrarSenha.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etSenha.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etReptSenha.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etReptSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            // Define a fonte para os campos de senha
            etSenha.setTypeface(ResourcesCompat.getFont(this, R.font.adigianaui));
            etReptSenha.setTypeface(ResourcesCompat.getFont(this, R.font.adigianaui));
        });

        btnCadastrar.setOnClickListener(v -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String reptSenha = etReptSenha.getText().toString().trim();

        // Validação de campos vazios
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || reptSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação de senhas coincidentes
        if (!senha.equals(reptSenha)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação de seleção de sexo
        RadioGroup radioGroupSexo = findViewById(R.id.radioGroupSexo);
        int selectedId = radioGroupSexo.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Selecione o sexo", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedSexo = findViewById(selectedId);
        String sexo = selectedSexo.getText().toString();

        Log.d("Cadastro", "Tentando criar usuário no Firebase Auth...");
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("Cadastro", "Usuário criado no Auth com sucesso.");
                        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

                        if (userId == null) {
                            Toast.makeText(this, "Erro: userId é nulo", Toast.LENGTH_SHORT).show();
                            Log.e("Cadastro", "userId é nulo após criação do usuário.");
                            return;
                        }

                        Usuario usuario = new Usuario(nome, email, sexo);
                        Log.d("Cadastro", "Salvando dados do usuário no Realtime Database...");

                        // Modificado: Lida com sucesso e falha dentro deste addOnCompleteListener
                        mDatabase.child("usuarios").child(userId).setValue(usuario)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Log.d("Cadastro", "Dados do usuário salvos com sucesso no Database.");
                                        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, LoginActivity.class));
                                        finish();
                                    } else {
                                        Log.e("Cadastro", "Erro ao salvar dados do usuário.", dbTask.getException());
                                        Toast.makeText(this, "Erro ao salvar dados do usuário: " + dbTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                        // Removido o addOnFailureListener separado para a operação do Firebase Database
                    } else {
                        Log.e("Cadastro", "Erro ao criar usuário: ", task.getException());
                        Toast.makeText(this, "Erro ao cadastrar: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Classe modelo para armazenar dados do usuário no Firebase Database
    public static class Usuario {
        public String nome;
        public String email;
        public String sexo;

        public Usuario() {
            // Construtor vazio necessário para Firebase
        }

        public Usuario(String nome, String email, String sexo) {
            this.nome = nome;
            this.email = email;
            this.sexo = sexo;
        }
    }
}
