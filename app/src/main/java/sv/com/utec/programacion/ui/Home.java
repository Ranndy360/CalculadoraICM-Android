package sv.com.utec.programacion.ui;

import androidx.appcompat.app.AppCompatActivity;
import sv.com.utec.programacion.R;
import sv.com.utec.programacion.pojo.Conexion;
import sv.com.utec.programacion.util.Validar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button btnRegistro = (Button)findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(goRegistro);

        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(goHome);
    }
    private View.OnClickListener goRegistro = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent (v.getContext(), Registro.class);
            startActivityForResult(intent, 0);
        }
    };

    private View.OnClickListener goHome = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                //asignando campos
                EditText txtUsuario, txtClave;
                TextView lblValidacion = findViewById(R.id.msgValidacion);
                lblValidacion.setVisibility(View.INVISIBLE);
                txtUsuario = findViewById(R.id.usuario);
                txtClave = findViewById(R.id.clave);

                //validacion de campos
                Validar validar = new Validar();
                boolean formValido = false;
                if(!validar.Vacio(txtUsuario)){
                    if(!validar.Vacio(txtClave)) {
                        formValido = true;
                    }
                }
                //Ingreso a la base de datos
                if(!formValido) {
                    lblValidacion.setText("Campos invalidos");
                    lblValidacion.setVisibility(View.VISIBLE);
                }else {
                    String pwd = Base64.encodeToString(txtClave.getText().toString().getBytes(),Base64.DEFAULT);
                    pwd = pwd.replaceAll("\n","");
                    //preparamos los datos para insertarlo
                    Map<String,String> usuario = new HashMap<>();
                    usuario.put("Nombre_User",txtUsuario.getText().toString());
                    usuario.put("Pass", pwd);

                    Conexion con = new Conexion();
                    ResultSet resp = con.getData("select * from usuario where Pass = ? and  Nombre_User = ?",usuario);

                    if(resp.next()){
                        SharedPreferences pref = getSharedPreferences("sesion",MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("id",resp.getString("ID_User"));
                        editor.commit();
                        Intent intent = new Intent (v.getContext(), Perfil.class);
                        startActivityForResult(intent, 0);
                    }else{
                        lblValidacion.setText("Credenciales invalidas");
                        lblValidacion.setVisibility(View.VISIBLE);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error inesperado",Toast.LENGTH_LONG).show();
            }
        }
    };
}
