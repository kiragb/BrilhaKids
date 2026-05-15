package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FaleConosco extends AppCompatActivity {

    private EditText nomeEditText, emailEditText, telefoneEditText, comentarioEditText;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fale_conosco);

        // Ajuste de preenchimento para barras do sistema
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Referências do Formulário
        nomeEditText = findViewById(R.id.Nome);
        emailEditText = findViewById(R.id.Email);
        telefoneEditText = findViewById(R.id.telefone);
        comentarioEditText = findViewById(R.id.comentario);
        btnEnviar = findViewById(R.id.btnCadastrar);

        // Lógica de Envio
        btnEnviar.setOnClickListener(v -> {
            String nome = nomeEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String comentario = comentarioEditText.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || comentario.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Mensagem enviada com sucesso!", Toast.LENGTH_LONG).show();
            limparCampos();
            finish(); // Retorna automaticamente para a tela de Ajuda após o envio
        });
    }

    private void limparCampos() {
        nomeEditText.setText("");
        emailEditText.setText("");
        telefoneEditText.setText("");
        comentarioEditText.setText("");
    }
}