package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meu_app.db";
    private static final int DATABASE_VERSION = 3;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Construtor sem argumentos apenas para evitar erro em ferramentas que exigem
    public DBHelper() {
        super(null, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "senha TEXT NOT NULL, " +
                "sexo TEXT, " +
                "codigo_recuperacao TEXT)";
        db.execSQL(sql);

        String mensagens = "CREATE TABLE mensagens (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "email TEXT, " +
                "telefone TEXT, " +
                "comentario TEXT)";
        db.execSQL(mensagens);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE usuarios ADD COLUMN codigo_recuperacao TEXT");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE usuarios ADD COLUMN sexo TEXT");
        }
    }

    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String buscarNomePorEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome FROM usuarios WHERE email = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            String nome = cursor.getString(0);
            cursor.close();
            return nome;
        } else {
            cursor.close();
            return null;
        }
    }
    public void salvarMensagem(String nome, String email, String telefone, String comentario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("email", email);
        values.put("telefone", telefone);
        values.put("comentario", comentario);
        db.insert("mensagens", null, values);
    }
}