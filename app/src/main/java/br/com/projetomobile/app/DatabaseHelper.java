package br.com.projetomobile.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 16254855 on 28/08/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String NOME_DO_BANCO="financas.db";
    private static final int VERSAO=1;

    public DatabaseHelper (Context context) {
        super(context, NOME_DO_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE tblUsuario(_idUsuario INTEGER primary key," +
                " usuario TEXT, senha TEXT, nome TEXT);";

        db.execSQL(sql);

        sql = "CREATE TABLE tblCategorias(_idCategoria INTEGER primary key," +
                " descricao TEXT, idUsuario INTEGER);";

        db.execSQL(sql);

        sql = "CREATE TABLE tblGastos(_idGasto INTEGER primary key," +
                "idUsuario  INTEGER not null, idCategoria INTEGER not null," +
                "valor REAL, descricao TEXT, data TEXT, localizacao TEXT, latitude TEXT, longitude TEXT);";

         db.execSQL(sql);

        sql = "INSERT INTO tblCategorias(descricao) VALUES ('Todas')";

        db.execSQL(sql);

        sql = "INSERT INTO tblCategorias(descricao) VALUES ('Lazer')";

        db.execSQL(sql);

        sql = "INSERT INTO tblCategorias(descricao) VALUES ('Transporte')";

        db.execSQL(sql);

        sql = "INSERT INTO tblCategorias(descricao) VALUES ('Alimentação')";

        db.execSQL(sql);

        sql = "INSERT INTO tblCategorias(descricao) VALUES ('Outros')";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
