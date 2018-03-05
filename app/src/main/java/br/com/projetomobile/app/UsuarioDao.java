package br.com.projetomobile.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 16254855 on 30/08/2017.
 */

public class UsuarioDao {
    private static  UsuarioDao instance;
    public  static  UsuarioDao getInstance(){
        if (instance == null){
            instance = new UsuarioDao();
        }
        return instance;
    }

    public void inserirContato(Context context, Usuario usuario){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("nome",  usuario.getNome());
        values.put("usuario", usuario.getUsuario());
        values.put("senha", usuario.getSenha());

        db.insert("tblUsuario", null, values);
    }

    public Usuario autenticaUsuario(Context context, String usrName){
        Usuario usuario = new Usuario();

        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        String sql = "SELECT * FROM tblUsuario Where usuario=?";

        Cursor cursor = db.rawQuery(sql, new String[]{usrName});

        if(cursor.getCount()>0){
            cursor.moveToFirst();

            usuario.setIdUsuario(cursor.getInt(0));
            usuario.setUsuario(cursor.getString(1));
            usuario.setSenha(cursor.getString(2));
            usuario.setNome(cursor.getString(3));

            cursor.close();
        }

        return usuario;
    }

    public void atualizaUsuario(Context context, Usuario usuario){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("nome",  usuario.getNome());
        values.put("usuario", usuario.getUsuario());
        values.put("senha", usuario.getSenha());

        db.update("tblUsuario", values, "_idUsuario=?", new String[]{usuario.getIdUsuario()+""});
    }

    public Usuario obterUsuarioporID (Context context, int idUsuario){
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        String sql = "SELECT * FROM tblUsuario WHERE _idUsuario = ?";

        Cursor cursor = db.rawQuery(sql,  new String[]{idUsuario+""});
        cursor.moveToFirst();

        Usuario usuario = new Usuario();

        usuario.setIdUsuario(cursor.getInt(0));
        usuario.setNome(cursor.getString(3));
        usuario.setUsuario(cursor.getString(1));
        usuario.setSenha(cursor.getString(2));

        return usuario;
    }

    public void deletarUsuario(Context context, int idUsuario) {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        db.delete("tblUsuario", "_idUsuario=?", new String[]{idUsuario+""});
        db.delete("tblGastos", "idUsuario=?", new String[]{idUsuario+""});
        db.delete("tblCategorias", "idUsuario=?", new String[]{idUsuario+""});
    }
}

