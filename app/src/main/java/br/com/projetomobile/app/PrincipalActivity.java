package br.com.projetomobile.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class PrincipalActivity extends AppCompatActivity {

    CategoriaDAO dao = CategoriaDAO.getInstance();
    GastoDAO gastoDAO = GastoDAO.getInstance();
    Spinner spinner;
    ArrayAdapter<Gasto> adapterGasto;
    ListView lstGastos;
    TextView txtTotal;
    Categoria categoria = new Categoria();
    int position;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        spinner = (Spinner)findViewById(R.id.spinner1);
        lstGastos = (ListView)findViewById(R.id.list_view);
        txtTotal = (TextView)findViewById(R.id.txtTotal);

        Intent intent = getIntent();
        idUsuario = intent.getIntExtra("idUsuario", 0);

        final ArrayAdapter<Categoria> adapter = new ArrayAdapter<Categoria>
                (this, android.R.layout.simple_spinner_dropdown_item,  dao.mostrarCategorias(this, false, idUsuario));

        /*Adciona as categorias no spinner*/
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                /*Limpa o adapter*/
                adapterGasto.clear();
                Categoria categoria2 = adapter.getItem(position);
                /*Preenche a lista com algum parametro*/
                adapterGasto.addAll(
                gastoDAO.mostrarGastos(getApplicationContext(), categoria2.getIdCategoria(), idUsuario));

                txtTotal.setText("Total: "+gastoDAO.mostrarGastosTotais(getApplicationContext(), categoria2.getIdCategoria(), idUsuario));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        categoria = adapter.getItem(position);

        adapterGasto = new GastoAdapter(this, gastoDAO.mostrarGastos(getApplicationContext(), categoria.getIdCategoria(), idUsuario));
        lstGastos.setAdapter(adapterGasto);

        lstGastos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), AddGastoActivity.class);
                intent.putExtra("viewMode", true);
                intent.putExtra("idGasto", adapterGasto.getItem(i).getIdGasto());

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.addCategory){
            Intent intent = new Intent(this, CategoriaActivity.class);
            intent.putExtra("idUsuario", idUsuario);
            startActivity(intent);
        }

        if (id == R.id.logout){
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if (id == R.id.editarUser){
            Intent intent = new Intent(this, CadUsuarioActivity.class);
            intent.putExtra("updateMode", true);
            intent.putExtra("idUsuario", idUsuario);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void adcionarGasto(View view) {
        Intent intent = new Intent(getApplicationContext(), AddGastoActivity.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);
    }
}
