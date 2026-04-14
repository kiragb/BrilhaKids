package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class EmailEnviadoActivity extends AppCompatActivity {
    TextView tvReenviarEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_enviado);

        tvReenviarEmail = findViewById(R.id.tvReenviarEmail);
        tvReenviarEmail.setOnClickListener(v -> {
            Toast.makeText(this, "E-mail reenviado!", Toast.LENGTH_SHORT).show();
        });
    }
}
