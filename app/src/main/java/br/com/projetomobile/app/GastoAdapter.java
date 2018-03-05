package br.com.projetomobile.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by 16254855 on 27/09/2017.
 */

public class GastoAdapter extends ArrayAdapter<Gasto> {
    public GastoAdapter(Context context, ArrayList<Gasto> list){

        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_view_gastos, null);

        }

        Gasto item = getItem(position);

        TextView txtDescGasto = (TextView) v.findViewById(R.id.txtDescGasto);
        TextView txtValor = (TextView)v.findViewById(R.id.txtValorGasto);

        //Converte o double para o formato de real
        Locale l = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(l);

        String valor = nf.format(item.getValor());

        txtDescGasto.setText(item.getDescricao());
        txtValor.setText(valor);

        return v;
    }
}
