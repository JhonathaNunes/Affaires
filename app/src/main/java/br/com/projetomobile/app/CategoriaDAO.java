package br.com.projetomobile.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by 16254855 on 04/09/2017.
 */

public class CategoriaDAO {

    private static CategoriaDAO instance;

    public static CategoriaDAO getInstance(){

        if (instance == null){
            instance = new CategoriaDAO();
        }
        return instance;
    }

    public ArrayList mostrarCategorias(Context context, Boolean selecionaveis, int idUsuario){
        ArrayList<Categoria> lstCategorias = new ArrayList<>();
        String sql;

        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        if (selecionaveis){
            sql = "SELECT * FROM tblCategorias WHERE _idCategoria != 1 AND _idCategoria <= 5 OR idUsuario = "+ idUsuario + ";";
        }else{
            sql = "SELECT * FROM tblCategorias WHERE _idCategoria <= 5 OR idUsuario = "+ idUsuario + ";";
        }

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()){
            Categoria categoria = new Categoria();

            categoria.setIdCategoria(cursor.getInt(0));
            categoria.setDescricao(cursor.getString(1));

            lstCategorias.add(categoria);
        }
        cursor.close();

        return lstCategorias;
    }

    public void adcionarCategoria(Context context, Categoria categoria){

        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("descricao", categoria.getDescricao());
        values.put("idUsuario", categoria.getIdUsuario());

        db.insert("tblCategorias", null, values);
    }

    public Categoria obterCategoriaPorId(Context context, int idCategoria){
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        String sql = "SELECT * FROM tblCategorias WHERE _idCategoria=?;";

        Cursor cursor = db.rawQuery(sql, new String[]{idCategoria + ""});
        cursor.moveToFirst();

        Categoria categoria = new Categoria();

        categoria.setIdCategoria(cursor.getInt(0));
        categoria.setDescricao(cursor.getString(1));
        categoria.setIdUsuario(cursor.getInt(2));

        return categoria;
    }
}
