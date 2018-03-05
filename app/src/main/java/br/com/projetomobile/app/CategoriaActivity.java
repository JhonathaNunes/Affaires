package br.com.projetomobile.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class CategoriaActivity extends AppCompatActivity {
    CategoriaDAO dao = CategoriaDAO.getInstance();

    EditText txtCategoria;
    ArrayAdapter<Categoria> adapter;

    int idUsuario;
    ListView lstCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        txtCategoria = (EditText)findViewById(R.id.txtCategoria);
        lstCategorias = (ListView) findViewById(R.id.list_view);

        adapter = new ArrayAdapter<Categoria>(this, R.layout.list_view_item, dao.mostrarCategorias(this, false, idUsuario));

        lstCategorias.setAdapter(adapter);

        Intent intent = getIntent();
        idUsuario = intent.getIntExtra("idUsuario", 0);
    }

    public void adcionarCategoria(View view) {
        Categoria categoria = new Categoria();

        categoria.setDescricao(txtCategoria.getText().toString());
        categoria.setIdUsuario(idUsuario);

        dao.adcionarCategoria(getApplicationContext(), categoria);

        abrirMain();
    }

    public void abrirMain(){
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        intent.putExtra("idUsuario", idUsuario);
        finish();
        startActivity(intent);
    }

}
