package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Activity_puzzle extends AppCompatActivity {

    // Componentes do Jogo
    private GridLayout puzzleGrid;
    private int[] board = new int[9];
    private Bitmap[] imagePieces;
    private int moves = 0;
    private TextView tvMoves;
    private boolean isImageMode = true;

    // Componentes do Menu Lateral (Padrão BrilhaKids)
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        // --- 1. CONFIGURAÇÃO DO MENU LATERAL E LOGO ---
        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);

        // Abre o menu ao clicar no ícone
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Recupera dados do usuário para o Header do Menu
        String nome = getIntent().getStringExtra("nome");
        String sexo = getIntent().getStringExtra("sexo");
        setupNavigationHeader(nome, sexo);

        // Lógica dos cliques no Menu Lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(this, MainLoggedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            } else if (id == R.id.nav_senha) {
                Intent intent = new Intent(this, AlterarSenhaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            } else if (id == R.id.nav_sobre) {
                Intent intent = new Intent(this, SobreNos.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            } else if (id == R.id.nav_sair) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            } else if (id == R.id.nav_perfil) {
                Intent intent = new Intent(this, MeuPerfil.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }


            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        com.google.android.material.switchmaterial.SwitchMaterial switchMode = findViewById(R.id.switchMode);
        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isImageMode = isChecked;
            renderBoard(); // Redesenha o tabuleiro com o novo modo
        });

        // --- 2. CONFIGURAÇÃO DO JOGO (QUEBRA-CABEÇA) ---
        puzzleGrid = findViewById(R.id.puzzleGrid);
        tvMoves = findViewById(R.id.tvMoves);
        Button btnShuffle = findViewById(R.id.btnReset);      // Botão Verde
        Button btnRestart = findViewById(R.id.btnreiniciar);  // Botão Vermelho
        Button btnHint = findViewById(R.id.btnHint);          // Botão Turquesa

        // Carregamento da Imagem (Girassol)
        try {
            Bitmap fullBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.girassol);
            if (fullBitmap != null) {
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(fullBitmap, 600, 600, true);
                imagePieces = splitImage(scaledBitmap);
            } else {
                Toast.makeText(this, "Imagem 'girassol' não encontrada!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initBoard();
        renderBoard();

        // Clique: Embaralhar (Verde)
        btnShuffle.setOnClickListener(v -> {
            shuffleBoard();
            Toast.makeText(this, "Boa sorte!", Toast.LENGTH_SHORT).show();
        });

        // Clique: Reiniciar (Vermelho)
        btnRestart.setOnClickListener(v -> {
            initBoard();
            moves = 0;
            updateMoveText();
            renderBoard();
            Toast.makeText(this, "Jogo resetado!", Toast.LENGTH_SHORT).show();
        });

        // Clique: Dica (Turquesa)
        btnHint.setOnClickListener(v -> {
            Toast.makeText(this, "Dica: Tente organizar as bordas primeiro!", Toast.LENGTH_LONG).show();
        });
    }

    // Configura o Nome e o Avatar no Menu Lateral
    private void setupNavigationHeader(String nome, String sexo) {
        View headerView = navigationView.getHeaderView(0);
        TextView textViewNome = headerView.findViewById(R.id.textViewNomeUsuario);
        ImageView imagePerfil = headerView.findViewById(R.id.imagePerfil);

        if (nome != null && textViewNome != null) {
            textViewNome.setText("Olá, " + nome + "!");
        }
        if (sexo != null && imagePerfil != null) {
            if (sexo.equalsIgnoreCase("Masculino")) {
                imagePerfil.setImageResource(R.drawable.meny);
            } else {
                imagePerfil.setImageResource(R.drawable.menx);
            }
        }
    }

    // Divide a imagem em 9 pedaços (3x3)
    private Bitmap[] splitImage(Bitmap bitmap) {
        Bitmap[] pieces = new Bitmap[9];
        int width = bitmap.getWidth() / 3;
        int height = bitmap.getHeight() / 3;

        for (int i = 0; i < 9; i++) {
            int x = (i % 3) * width;
            int y = (i / 3) * height;
            pieces[i] = Bitmap.createBitmap(bitmap, x, y, width, height);
        }
        return pieces;
    }

    private void initBoard() {
        for (int i = 0; i < 8; i++) board[i] = i + 1;
        board[8] = 0; // 0 é o espaço vazio
    }

    private void renderBoard() {
        puzzleGrid.removeAllViews();
        for (int i = 0; i < 9; i++) {
            // Criamos um FrameLayout para conter o número centralizado sobre o fundo
            android.widget.FrameLayout container = new android.widget.FrameLayout(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.columnSpec = GridLayout.spec(i % 3, 1f);
            params.rowSpec = GridLayout.spec(i / 3, 1f);
            params.setMargins(8, 8, 8, 8);
            container.setLayoutParams(params);

            int pieceValue = board[i];

            if (pieceValue == 0) {
                container.setVisibility(View.INVISIBLE);
            } else {
                ImageView img = new ImageView(this);
                img.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
                img.setScaleType(ImageView.ScaleType.FIT_XY);

                if (isImageMode && imagePieces != null) {
                    // Modo Imagem
                    img.setImageBitmap(imagePieces[pieceValue - 1]);
                    img.setBackground(null);
                } else {
                    // Modo Número
                    // ... dentro do loop for no renderBoard ...
                    if (isImageMode && imagePieces != null) {
                        // Modo Imagem (Mantém como estava)
                        img.setImageBitmap(imagePieces[pieceValue - 1]);
                    } else {
                        // --- MODO NÚMERO ESTILIZADO ---
                        img.setImageBitmap(null);

                        // Aplicamos o fundo arredondado que criamos
                        container.setBackgroundResource(R.drawable.card_numero);

                        TextView tv = new TextView(this);
                        tv.setText(String.valueOf(pieceValue));
                        tv.setTextSize(32); // Aumentamos o tamanho
                        tv.setTextColor(Color.parseColor("#2DA33E")); // Cor combinando com a borda
                        tv.setTypeface(null, android.graphics.Typeface.BOLD); // Negrito

                        // Centralização absoluta do número
                        android.widget.FrameLayout.LayoutParams textParams = new android.widget.FrameLayout.LayoutParams(
                                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                                android.view.Gravity.CENTER
                        );
                        tv.setLayoutParams(textParams);

                        container.addView(tv);
                    }
// ...

                    // Adiciona um TextView para o número
                    TextView tv = new TextView(this);
                    tv.setText(String.valueOf(pieceValue));
                    tv.setTextSize(24);
                    tv.setTextColor(Color.BLACK);
                    tv.setGravity(android.view.Gravity.CENTER);
                    container.addView(tv);
                }

                container.addView(img, 0); // Adiciona a imagem no fundo
                container.setBackgroundColor(Color.LTGRAY);
            }

            final int currentIndex = i;
            container.setOnClickListener(v -> movePiece(currentIndex));
            puzzleGrid.addView(container);
        }
    }

    private void movePiece(int index) {
        int emptyIndex = findEmptyIndex();
        if (isAdjacent(index, emptyIndex)) {
            int temp = board[index];
            board[index] = board[emptyIndex];
            board[emptyIndex] = temp;

            moves++;
            updateMoveText();
            renderBoard();
            checkWin();
        }
    }

    private void updateMoveText() {
        tvMoves.setText("MOVIMENTOS: " + moves);
    }

    private boolean isAdjacent(int i1, int i2) {
        int row1 = i1 / 3, col1 = i1 % 3;
        int row2 = i2 / 3, col2 = i2 % 3;
        return Math.abs(row1 - row2) + Math.abs(col1 - col2) == 1;
    }

    private int findEmptyIndex() {
        for (int i = 0; i < 9; i++) if (board[i] == 0) return i;
        return -1;
    }

    private void shuffleBoard() {
        initBoard();
        Random rnd = new Random();
        for (int i = 0; i < 100; i++) {
            int empty = findEmptyIndex();
            List<Integer> neighbors = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                if (isAdjacent(empty, j)) neighbors.add(j);
            }
            int nextMove = neighbors.get(rnd.nextInt(neighbors.size()));
            board[empty] = board[nextMove];
            board[nextMove] = 0;
        }
        moves = 0;
        updateMoveText();
        renderBoard();
    }

    private void checkWin() {
        boolean win = true;
        for (int i = 0; i < 8; i++) {
            if (board[i] != i + 1) {
                win = false;
                break;
            }
        }
        if (win && board[8] == 0) {
            Toast.makeText(this, "Parabéns! Você venceu!", Toast.LENGTH_LONG).show();
        }
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