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

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //regresar a login
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(goLogin);
        //registrar usuario
        Button btnRegistar = (Button)findViewById(R.id.btnRegistrar);
        btnRegistar.setOnClickListener(registrar);
    }

    private View.OnClickListener registrar = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                //asignando campos
                EditText txtUsuario, txtCorreo, txtClave;
                TextView lblValidacion = findViewById(R.id.lblValidacion);
                lblValidacion.setVisibility(View.INVISIBLE);
                txtUsuario = findViewById(R.id.txtUsuario);
                txtCorreo = findViewById(R.id.txtCorreo);
                txtClave = findViewById(R.id.txtClave);

                //validacion de campos
                Validar validar = new Validar();
                boolean formValido = false;
                if(!validar.Vacio(txtUsuario)){
                    if(!validar.Vacio(txtCorreo)) {
                        if(!validar.isEmail(txtCorreo.toString())) {
                            if (!validar.Vacio(txtClave)) {
                                formValido = true;
                            }
                        }
                    }
                }

                //Ingreso a la base de datos
                if(!formValido)
                    lblValidacion.setVisibility(View.VISIBLE);
                else {
                    //Comprobando si el usuario ya existe
                    Map<String,String> usuario = new HashMap<>();
                    usuario.put("0",txtUsuario.getText().toString());
                    usuario.put("1", txtCorreo.getText().toString());

                    Conexion con = new Conexion();
                    ResultSet resp = con.getData("SELECT * FROM usuario WHERE Nombre_User = ? OR  Email = ?",usuario);

                    if(resp.next()){
                        lblValidacion.setText("Usuario/Correo ya registrado");
                        lblValidacion.setVisibility(View.VISIBLE);
                    }
                    else{
                        //Hacemos el insert
                        String pwd = Base64.encodeToString(txtClave.getText().toString().getBytes(),Base64.DEFAULT);
                        pwd = pwd.replaceAll("\n","");

                        Map<String,String> nuevo = new HashMap<>();
                        nuevo.put("0",txtUsuario.getText().toString());
                        nuevo.put("1", txtCorreo.getText().toString());
                        nuevo.put("2", pwd);
                        boolean resp2 = con.setData("INSERT INTO usuario (Nombre_User,Email,Pass) VALUES(?,?,?)",nuevo);
                        if(resp2) {

                            Map<String,String> usuarioData = new HashMap<>();
                            usuarioData.put("1", txtUsuario.getText().toString());

                            ResultSet rs = con.getData("SELECT * FROM usuario WHERE Nombre_User = ?",usuarioData);

                            if(rs.next()){
                                Map<String,String> perfil = new HashMap<>();
                                perfil.put("1", rs.getString("ID_User"));
                                boolean nuevoPerfil = con.setData("INSERT INTO perfil (ID_User) VALUES(?)",perfil);
                                if(nuevoPerfil)
                                    Toast.makeText(getApplicationContext(), "Usuario creado", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getApplicationContext(), "No se ha podido registrar", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error inesperado",Toast.LENGTH_LONG).show();
            }
        }
    };

    private View.OnClickListener goLogin = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent (v.getContext(), Home.class);
            startActivityForResult(intent, 0);
//            Toast.makeText(this,"texto",Toast.LENGTH_LONG).show();
        }
    };
}
