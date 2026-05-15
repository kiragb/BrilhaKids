package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

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

    // Componentes do Menu Lateral e Progresso
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CheckBox checkboxConcluido;
    private SharedPreferences preferences;

    // Chaves para o sistema de progresso BrilhaKids
    private static final String PREFS_NAME = "BrilhaKidsPrefs";
    private static final String KEY_CHECK_PUZZLE = "concluido_puzzle_deslizante";
    private static final String KEY_TOTAL_JOGOS = "total_jogo_concluida";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        // --- 1. CONFIGURAÇÃO DO MENU LATERAL E LOGO ---
        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView menuIcon = findViewById(R.id.menuIcon);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        configurarFraseDinamica();

        String nome = getIntent().getStringExtra("nome");
        String sexo = getIntent().getStringExtra("sexo");
        setupNavigationHeader(nome, sexo);

        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_home) intent = new Intent(this, MainLoggedActivity.class);
            else if (id == R.id.nav_senha) intent = new Intent(this, AlterarSenhaActivity.class);
            else if (id == R.id.nav_sobre) intent = new Intent(this, SobreNos.class);
            else if (id == R.id.nav_sair) intent = new Intent(this, MainActivity.class);
            else if (id == R.id.nav_perfil) intent = new Intent(this, MeuPerfil.class);

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("nome", nome);
                intent.putExtra("sexo", sexo);
                startActivity(intent);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // --- 2. LÓGICA DE PROGRESSO (SharedPreferences) ---
        checkboxConcluido = findViewById(R.id.checkboxConcluidoPuzzle);
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (checkboxConcluido != null) {
            checkboxConcluido.setChecked(preferences.getBoolean(KEY_CHECK_PUZZLE, false));
            checkboxConcluido.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_CHECK_PUZZLE, isChecked);

                int totalAtual = preferences.getInt(KEY_TOTAL_JOGOS, 0);
                if (isChecked) {
                    editor.putInt(KEY_TOTAL_JOGOS, totalAtual + 1);
                    Toast.makeText(this, "Desafio concluído! Você é fera! 🧩", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putInt(KEY_TOTAL_JOGOS, Math.max(0, totalAtual - 1));
                }
                editor.apply();
            });
        }

        // --- 3. CONFIGURAÇÃO DO JOGO ---
        puzzleGrid = findViewById(R.id.puzzleGrid);
        tvMoves = findViewById(R.id.tvMoves);
        SwitchMaterial switchMode = findViewById(R.id.switchMode);
        Button btnShuffle = findViewById(R.id.btnReset);
        Button btnRestart = findViewById(R.id.btnreiniciar);
        Button btnHint = findViewById(R.id.btnHint);

        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isImageMode = isChecked;
            renderBoard();
        });

        // Carregamento do Girassol
        try {
            Bitmap fullBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.girassol);
            if (fullBitmap != null) {
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(fullBitmap, 600, 600, true);
                imagePieces = splitImage(scaledBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initBoard();
        renderBoard();

        btnShuffle.setOnClickListener(v -> shuffleBoard());
        btnRestart.setOnClickListener(v -> {
            initBoard();
            moves = 0;
            updateMoveText();
            renderBoard();
        });
        btnHint.setOnClickListener(v -> Toast.makeText(this, "Dica: O espaço vazio deve ficar no canto inferior direito!", Toast.LENGTH_LONG).show());
    }

    private void setupNavigationHeader(String nome, String sexo) {
        if (navigationView.getHeaderCount() > 0) {
            View headerView = navigationView.getHeaderView(0);
            TextView textViewNome = headerView.findViewById(R.id.textViewNomeUsuario);
            ImageView imagePerfil = headerView.findViewById(R.id.imagePerfil);
            if (nome != null) textViewNome.setText("Olá, " + nome + "!");
            if (sexo != null) {
                imagePerfil.setImageResource(sexo.equalsIgnoreCase("Masculino") ? R.drawable.meny : R.drawable.menx);
            }
        }
    }

    private Bitmap[] splitImage(Bitmap bitmap) {
        Bitmap[] pieces = new Bitmap[9];
        int width = bitmap.getWidth() / 3;
        int height = bitmap.getHeight() / 3;
        for (int i = 0; i < 9; i++) {
            pieces[i] = Bitmap.createBitmap(bitmap, (i % 3) * width, (i / 3) * height, width, height);
        }
        return pieces;
    }

    private void initBoard() {
        for (int i = 0; i < 8; i++) board[i] = i + 1;
        board[8] = 0;
    }

    private void renderBoard() {
        puzzleGrid.removeAllViews();
        for (int i = 0; i < 9; i++) {
            FrameLayout container = new FrameLayout(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; params.height = 0;
            params.columnSpec = GridLayout.spec(i % 3, 1f);
            params.rowSpec = GridLayout.spec(i / 3, 1f);
            params.setMargins(8, 8, 8, 8);
            container.setLayoutParams(params);

            int pieceValue = board[i];
            if (pieceValue == 0) {
                container.setVisibility(View.INVISIBLE);
            } else {
                ImageView img = new ImageView(this);
                img.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                img.setScaleType(ImageView.ScaleType.FIT_XY);

                if (isImageMode && imagePieces != null) {
                    img.setImageBitmap(imagePieces[pieceValue - 1]);
                    container.addView(img);
                } else {
                    container.setBackgroundResource(R.drawable.card_numero);
                    TextView tv = new TextView(this);
                    tv.setText(String.valueOf(pieceValue));
                    tv.setTextSize(32);
                    tv.setTextColor(Color.parseColor("#2DA33E"));
                    tv.setTypeface(null, Typeface.BOLD);
                    FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(-2, -2, Gravity.CENTER);
                    tv.setLayoutParams(textParams);
                    container.addView(tv);
                }
            }
            final int currentIndex = i;
            container.setOnClickListener(v -> movePiece(currentIndex));
            puzzleGrid.addView(container);
        }
    }

    private void movePiece(int index) {
        int emptyIndex = findEmptyIndex();
        if (isAdjacent(index, emptyIndex)) {
            board[emptyIndex] = board[index];
            board[index] = 0;
            moves++;
            updateMoveText();
            renderBoard();
            checkWin();
        }
    }

    private void updateMoveText() { tvMoves.setText("MOVIMENTOS: " + moves); }

    private boolean isAdjacent(int i1, int i2) {
        return Math.abs(i1 / 3 - i2 / 3) + Math.abs(i1 % 3 - i2 % 3) == 1;
    }

    private int findEmptyIndex() {
        for (int i = 0; i < 9; i++) if (board[i] == 0) return i;
        return -1;
    }

    private void shuffleBoard() {
        Random rnd = new Random();
        for (int i = 0; i < 100; i++) {
            int empty = findEmptyIndex();
            List<Integer> neighbors = new ArrayList<>();
            for (int j = 0; j < 9; j++) if (isAdjacent(empty, j)) neighbors.add(j);
            int move = neighbors.get(rnd.nextInt(neighbors.size()));
            board[empty] = board[move];
            board[move] = 0;
        }
        moves = 0; updateMoveText(); renderBoard();
    }

    private void checkWin() {
        boolean win = true;
        for (int i = 0; i < 8; i++) if (board[i] != i + 1) win = false;
        if (win && board[8] == 0) Toast.makeText(this, "Incrível! Você montou o girassol! 🌻", Toast.LENGTH_LONG).show();
    }

    private void configurarFraseDinamica() {
        TextView tvFrase = navigationView.findViewById(R.id.tvFraseMenu);
        if (tvFrase != null) {
            String[] frases = {
                    "Você brilha muito! ✨",
                    "Pronto para aprender algo novo? 🍎",
                    "Comer bem é super divertido! 🥦",
                    "Qual será sua descoberta de hoje? 🧐",
                    "Você é nota dez! 🌟",
                    "Que tal um jogo agora? 🎮"
            };

            int indice = new Random().nextInt(frases.length);
            tvFrase.setText(frases[indice]);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }
}