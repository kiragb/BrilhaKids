package com.example.myapplication;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlterarSenhaActivity extends AppCompatActivity {
    EditText etNovaSenha, etConfirmarSenha;
    CheckBox cbMostrarSenha;
    Button btnAlterarSenha;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        etNovaSenha = findViewById(R.id.etNovaSenha);
        etConfirmarSenha = findViewById(R.id.etConfirmarSenha);
        cbMostrarSenha = findViewById(R.id.cbMostrarSenha);
        btnAlterarSenha = findViewById(R.id.btnAlterarSenha);

        mAuth = FirebaseAuth.getInstance();

        cbMostrarSenha.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etNovaSenha.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etConfirmarSenha.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etNovaSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etConfirmarSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

            // Reaplica a fonte personalizada
            etNovaSenha.setTypeface(ResourcesCompat.getFont(this, R.font.adigianaui));
            etConfirmarSenha.setTypeface(ResourcesCompat.getFont(this, R.font.adigianaui));
        });

        btnAlterarSenha.setOnClickListener(v -> {
            String senha = etNovaSenha.getText().toString().trim();
            String confirmar = etConfirmarSenha.getText().toString().trim();

            if (senha.length() < 6 || senha.length() > 10) {
                Toast.makeText(this, "A senha deve ter entre 6 e 10 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(confirmar)) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                user.updatePassword(senha).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
                        finish(); // Fecha a activity após sucesso
                    } else {
                        Toast.makeText(this, "Erro ao alterar senha: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(this, "Usuário não autenticado. Faça login novamente.", Toast.LENGTH_LONG).show();
                // Aqui você pode redirecionar para tela de login
            }
        });
    }
}
