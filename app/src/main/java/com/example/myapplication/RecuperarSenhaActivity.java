package com.example.myapplication;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RecuperarSenhaActivity extends AppCompatActivity {

    EditText etEmailUsuario;
    Button btnEnviarLink;
    TextView tvCancelar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        etEmailUsuario = findViewById(R.id.etEmailUsuario);
        btnEnviarLink = findViewById(R.id.btnEnviarLink);
        tvCancelar = findViewById(R.id.tvCancelar);

        mAuth = FirebaseAuth.getInstance();

        btnEnviarLink.setOnClickListener(v -> {
            String email = etEmailUsuario.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Informe o e-mail ou usuário", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "E-mail de recuperação enviado!", Toast.LENGTH_LONG).show();
                            finish(); // fecha a activity após o envio
                        } else {
                            Toast.makeText(this, "Falha ao enviar e-mail: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        tvCancelar.setOnClickListener(v -> finish());
    }
}
