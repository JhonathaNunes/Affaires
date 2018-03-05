package br.com.projetomobile.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CadUsuarioActivity extends AppCompatActivity {
    UsuarioDao dao = UsuarioDao.getInstance();

    EditText nome;
    EditText usuario;
    EditText senha;

    TextView txtTitulo;
    Button btnSalvar;
    Button btnDelete;

    boolean updateMode = false;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_usuario);

        nome = (EditText)findViewById(R.id.txtNome);
        usuario = (EditText)findViewById(R.id.txtnewUsuario);
        senha = (EditText)findViewById(R.id.txtnewSenha);

        Intent intent = getIntent();

        updateMode = intent.getBooleanExtra("updateMode", false);

        if (updateMode){
            idUsuario = intent.getIntExtra("idUsuario", 0);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
            getSupportActionBar().setHomeButtonEnabled(true); //Ativa o Botão
            getSupportActionBar().setTitle("Voltar");

            Usuario u = dao.obterUsuarioporID(getApplicationContext(), idUsuario);

            nome.setText(u.getNome());
            usuario.setText(u.getUsuario());
            senha.setText(u.getSenha());

            txtTitulo = (TextView)findViewById(R.id.txtTituloJanela);
            btnSalvar =(Button)findViewById(R.id.btnCadastro);
            btnDelete = (Button)findViewById(R.id.btnDelete);

            txtTitulo.setText("Atualizar Usuario");
            btnSalvar.setText("Atualizar");
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    public void cadastrarUsuario(View view) {
        if (updateMode){
            Usuario u = new Usuario();

            u.setIdUsuario(idUsuario);
            u.setNome(nome.getText().toString());
            u.setUsuario(usuario.getText().toString());
            u.setSenha(senha.getText().toString());

            dao.atualizaUsuario(getApplicationContext(), u);

            Toast.makeText(this, "Usuario Atualizado", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, PrincipalActivity.class);
            intent.putExtra("idUsuario", idUsuario);
            startActivity(intent);
            finish();
        }else{
            if(nome.getText().toString().isEmpty()){
                nome.setError("Preencha o campo");
            }else if(usuario.getText().toString().isEmpty()){
                usuario.setError("Preencha o campo");
            }else if(senha.getText().toString().isEmpty()){
                senha.setError("Preencha o campo");
            }else if (dao.autenticaUsuario(getApplicationContext(), usuario.getText().toString()).getUsuario() == null){
                Usuario u = new Usuario();

                u.setNome(nome.getText().toString());
                u.setUsuario(usuario.getText().toString());
                u.setSenha(senha.getText().toString());

                dao.inserirContato(this, u);

                Toast.makeText(this, "Usuario adcionado", Toast.LENGTH_SHORT).show();

                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "O usuário já existe", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deletarUsuario(View view) {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Deseja deletar este usuario?")
                .setMessage("Deletar um usuário apagará todos os gastos desse usuário e é um processo ireverssível")
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dao.deletarUsuario(getApplicationContext(), idUsuario);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, MainActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finish();
                break;
            default:break;
        }
        return true;
    }
}
