package br.com.projetomobile.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    UsuarioDao dao = UsuarioDao.getInstance();
    EditText usuarioField;
    EditText senhaField;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioField = (EditText)findViewById(R.id.txtUsuario);
        senhaField = (EditText)findViewById(R.id.txtSenha);

    }

    public void login(View view) {
        verificaSenha();
    }

    public void abrirCadastro(View view) {
        Intent intent = new Intent(this, CadUsuarioActivity.class);
        startActivity(intent);
    }

    public void verificaSenha(){
        if (!(usuarioField.getText().toString().isEmpty()) && !(senhaField.getText().toString().isEmpty())){
            String usuario = usuarioField.getText().toString();
            String senha = senhaField.getText().toString();

            Usuario user = dao.autenticaUsuario(this, usuario);

            intent = new Intent(getApplicationContext(), PrincipalActivity.class);
            intent.putExtra("idUsuario", user.getIdUsuario());

            if (senha.equals(user.getSenha())){
                abrirApp();
                Toast.makeText(getApplicationContext(), "Bem-Vindo " + user.getNome(), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Usuário ou Senha incorreto", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void abrirApp(){
        finish();
        startActivity(intent);
    }
}
