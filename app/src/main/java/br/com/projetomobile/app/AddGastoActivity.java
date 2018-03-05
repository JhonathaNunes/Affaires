package br.com.projetomobile.app;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddGastoActivity extends AppCompatActivity implements OnClickListener {

    CategoriaDAO cDao = CategoriaDAO.getInstance();
    GastoDAO dao = GastoDAO.getInstance();

    private LocationManager locationManager;

    EditText txtDescricao;
    EditText txtValor;
    EditText txtData;
    EditText txtLocal;
    EditText txtCategoria;
    TextView titulo;
    Button btnGasto;
    Spinner spinner;
    Categoria categoria;

    String latitude;
    String longitude;

    private String urlBase = "http://maps.googleapis.com/maps/api/staticmap?size=400x400&sensor=true&markers=color:green|%s,%s";//String url para o mapa

    private WebView map;

    LocationListener listener;

    private DatePickerDialog datePickerDialog;

    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gasto);
        isGPSEnable();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        spinner = (Spinner) findViewById(R.id.categorias);
        txtDescricao = (EditText) findViewById(R.id.txtDescricao);
        txtValor = (EditText) findViewById(R.id.txtValor);
        txtData = (EditText) findViewById(R.id.txtData);
        txtLocal = (EditText) findViewById(R.id.txtLocal);
        txtCategoria = (EditText) findViewById(R.id.txtCategoriaSpinner);
        titulo = (TextView) findViewById(R.id.txtTituloAddGasto);
        btnGasto = (Button) findViewById(R.id.btnAddGasto);
        map = (WebView) findViewById(R.id.mapa);

        Intent intent = getIntent();
        int idUsuario = intent.getIntExtra("idUsuario", 0);

        final ArrayAdapter<Categoria> adapter = new ArrayAdapter<Categoria>
                (this, android.R.layout.simple_spinner_dropdown_item, cDao.mostrarCategorias(this, true, idUsuario));
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoria = adapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (intent.getBooleanExtra("viewMode", false)) {
            telaViewMode(intent.getIntExtra("idGasto", 0));
        }else{

            setDateTimeField();

            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = sdf.format(date);
            txtData.setText(dateString);


            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            long tempoAtualizacao = 999;
            float distancia = 999;

            while (true) {
                listener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                };
                break;
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, tempoAtualizacao, distancia, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, tempoAtualizacao, distancia, listener);
        }
    }

    public void novoGasto(View view) {
        if(txtDescricao.getText().toString().isEmpty()){
            txtDescricao.setError("Preencha o campo");
        }else if(txtValor.getText().toString().isEmpty()){
            txtValor.setError("Preencha o campo");
        }else if(txtData.getText().toString().isEmpty()){
            txtData.setError("Preencha o campo");
        }else if(txtLocal.getText().toString().isEmpty()){
            txtLocal.setError("Preencha o campo");
        }else {
            String valor = txtValor.getText().toString();
            Gasto gasto = new Gasto();

            String value = transformaValor(valor);

            Intent intent = getIntent();
            int idUsuario = intent.getIntExtra("idUsuario", 0);

            gasto.setDescricao(txtDescricao.getText().toString());
            gasto.setIdCategoria(categoria.getIdCategoria());
            gasto.setData(txtData.getText().toString());
            gasto.setLocal(txtLocal.getText().toString());
            gasto.setValor(Double.parseDouble(value));
            gasto.setIdUsuario(idUsuario);
            gasto.setLatitude(latitude);
            gasto.setLongitude(longitude);

            dao.inserirGastos(getApplicationContext(), gasto);

            intent = new Intent(getApplicationContext(), PrincipalActivity.class);
            intent.putExtra("idUsuario", idUsuario);
            finish();
            startActivity(intent);
        }
    }

    private String transformaValor(String valor){
        String novoValor = valor.replaceAll(",", ".");

        return novoValor;
    }

    private void setDateTimeField() {
        txtData.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtData.setText(dateFormat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        if(view == txtData) {
            datePickerDialog.show();
        }
    }

    public void telaViewMode(int idGasto){
        titulo.setText("Visualizar Gasto");
        btnGasto.setVisibility(View.GONE);

        /*Verifica se o aparelho esta conectado na internet para mostrar o mapa*/
        if (verificaConexao()){
            map.setVisibility(View.VISIBLE);
        }else{
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Sem conexão com a internet")
                    .setMessage("Nesta etapa o aplicativo necessita de acesso a internet para mostar o mapa, deseja habilitar a internet?")
                    .setNegativeButton("Não", null)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .show();

        }
        Gasto gasto = dao.obterGastoPorId(getApplicationContext(), idGasto);

        txtDescricao.setText(gasto.getDescricao());
        txtValor.setText(converteDouble(gasto.getValor()));
        txtData.setText(gasto.getData());
        txtLocal.setText(gasto.getLocal());
        txtCategoria.setText(cDao.obterCategoriaPorId(getApplicationContext(), gasto.getIdCategoria()).getDescricao());

        txtDescricao.setEnabled(false);
        txtValor.setEnabled(false);
        txtData.setEnabled(false);
        txtLocal.setEnabled(false);
        spinner.setVisibility(View.GONE);
        txtCategoria.setVisibility(View.VISIBLE);

        String url = String.format(urlBase, gasto.getLatitude(), gasto.getLongitude());//Põe a Latitute e Longitude na url
        map.loadUrl(url);//Insere a url na web view para ver o mapa
    }

    public String converteDouble(Double valor){
        Locale l = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(l);

        String dinheiro = nf.format(valor);
        return dinheiro;
    }

    /*Verifica se o GPS está ligado*/
    public void isGPSEnable(){
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("O GPS está desabilitado")
                    .setMessage("Nesta etapa o aplicativo necessita obter sua localização, deseja habilitar o GPS?")
                    .setNegativeButton("Não", null)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);//Abre as configurações de local
                            startActivity(intent);
                        }
                    })
                    .show();

        }
    }

    public  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

}