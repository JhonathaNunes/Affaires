package br.com.projetomobile.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by 16254855 on 06/09/2017.
 */

public class GastoDAO {

    private static  GastoDAO instance;

    public static GastoDAO getInstance(){

        if (instance == null){
            instance = new GastoDAO();
        }
        return instance;
    }

    public ArrayList mostrarGastos(Context context, int idCategoria, int idUsuario){
        ArrayList<Gasto> lstGastos = new ArrayList<>();
        String sql;

        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        if (idCategoria == 1){
            sql = "SELECT * FROM tblGastos WHERE idUsuario = "+idUsuario+";";
        }else{
            sql = "SELECT * FROM tblGastos WHERE (idCategoria = " + idCategoria +" AND idUsuario = " + idUsuario +");";
        }

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()){
            Gasto gasto = new Gasto();

            gasto.setIdGasto(cursor.getInt(0));
            gasto.setIdUsuario(cursor.getInt(1));
            gasto.setIdCategoria(cursor.getInt(2));
            gasto.setValor(cursor.getDouble(3));
            gasto.setDescricao(cursor.getString(4));
            gasto.setData(cursor.getString(5));
            gasto.setLocal(cursor.getString(6));

            lstGastos.add(gasto);
        }

        cursor.close();

        return lstGastos;
    }

    public void inserirGastos(Context context, Gasto gasto){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("idUsuario", gasto.getIdUsuario());
        values.put("idCategoria", gasto.getIdCategoria());
        values.put("valor", gasto.getValor());
        values.put("descricao", gasto.getDescricao());
        values.put("data", gasto.getData());
        values.put("localizacao", gasto.getLocal());
        values.put("latitude", gasto.getLatitude());
        values.put("longitude", gasto.getLongitude());

        db.insert("tblGastos", null, values);
    }

    public Gasto obterGastoPorId(Context context, int id){
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        String sql = "SELECT * FROM tblGastos WHERE _idGasto=?;";

        Cursor cursor = db.rawQuery(sql, new String[]{id + ""});
        cursor.moveToFirst();

        Gasto gasto = new Gasto();

        gasto.setIdGasto(cursor.getInt(0));
        gasto.setIdUsuario(cursor.getInt(1));
        gasto.setIdCategoria(cursor.getInt(2));
        gasto.setValor(cursor.getDouble(3));
        gasto.setDescricao(cursor.getString(4));
        gasto.setData(cursor.getString(5));
        gasto.setLocal(cursor.getString(6));
        gasto.setLatitude(cursor.getString(7));
        gasto.setLongitude(cursor.getString(8));

        return gasto;
    }

    public String mostrarGastosTotais(Context context, int idCategoria, int idUsuario){
       SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        Cursor cursor;

        String sql;

        if (idCategoria == 1){
            sql = "SELECT valor FROM tblGastos WHERE idUsuario = "+idUsuario+";";
            cursor = db.rawQuery(sql, null);
        }else{
            sql = "SELECT valor FROM tblGastos where (idCategoria = ? AND idUsuario = "+idUsuario+");";
            cursor = db.rawQuery(sql, new String[]{idCategoria + ""});
        }




        double valorTotal = 0;

        while (cursor.moveToNext()){
            double xpto = cursor.getDouble(0);
            valorTotal=valorTotal+ xpto;
        }

        Locale l = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(l);

        String valorReal = nf.format(valorTotal);

        return valorReal;
    }
}
