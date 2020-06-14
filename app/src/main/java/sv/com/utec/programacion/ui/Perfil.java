package sv.com.utec.programacion.ui;

import androidx.appcompat.app.AppCompatActivity;
import sv.com.utec.programacion.R;
import sv.com.utec.programacion.pojo.Conexion;
import sv.com.utec.programacion.util.Validar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Perfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        //carga datos del perfil
        cargaPerfil();

        Button btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(postGuardar);
    }

    private View.OnClickListener postGuardar = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                //asignando campos
                EditText txtNombre, txtApellido, txtCorreo, txtNacimiento;
                String rdSexo;
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rdbSexo);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton rdbSexo = findViewById(selectedId);
               rdSexo = rdbSexo.getText().toString();

                txtNombre = findViewById(R.id.txtNombre);
                txtApellido = findViewById(R.id.txtApellido);
                txtCorreo = findViewById(R.id.txtCorreo);
                txtNacimiento = findViewById(R.id.txtNacimiento);

                //validacion de campos
                Validar validar = new Validar();
                boolean formValido = false;
                if(!validar.Vacio(txtApellido)){
                    if(!validar.Vacio(txtCorreo)) {
                        if(!validar.Vacio(txtNacimiento)) {
                            if (!validar.Vacio(txtNombre)) {
                                if (!validar.isEmail(txtNombre.getText().toString())) {
                                    formValido = true;
                                }
                            }
                        }
                    }
                }
                //Ingreso a la base de datos
                if(!formValido) {
                    Toast.makeText(getApplicationContext(),"Verifique, campos invalidos",Toast.LENGTH_LONG).show();
                }else {
                    //toma id usuario logueado
                    SharedPreferences pref = getSharedPreferences("sesion",MODE_PRIVATE);
                    String idUsuario = pref.getString("id",null);
                    //preparamos los datos para insertarlo
                    Map<String,String> usuario = new HashMap<>();
                    usuario.put("ID_User",idUsuario);
                    usuario.put("Email", txtCorreo.getText().toString());

                    Conexion con = new Conexion();
                    ResultSet resp = con.getData("select * from usuario where Email = ? and  ID_User <> ?",usuario);

                    //valida que el correo sea unico
                    if(resp.next()){
                        Toast.makeText(getApplicationContext(),"Correo ya fue registrado",Toast.LENGTH_LONG).show();
                    }else{

                        Map<String,String> usuarioData = new HashMap<>();
                        usuarioData.put("ID_User",idUsuario);
                        usuarioData.put("Email", txtCorreo.getText().toString());
                        //actualiza correo de usuario
                        boolean usuarioUp = con.setData("UPDATE usuario\n" +
                                                            "SET  Email = ? \n" +
                                                            "WHERE ID_User = ?",usuarioData);

                        Map<String,String> perfil = new HashMap<>();
                        perfil.put("5",idUsuario);
                        perfil.put("3", txtNacimiento.getText().toString());
                        perfil.put("4", rdSexo);
                        perfil.put("2", txtApellido.getText().toString());
                        perfil.put("1", txtNombre.getText().toString());
                        //actualiza datos de la persona
                        boolean perfilUpt = con.setData("UPDATE appimc.perfil\n" +
                                                            "SET Nombre = ? , Apellido = ?  , Fecha_Birth = ? , Sexo= ?  WHERE ID_User= ? ",perfil);
                        if(usuarioUp && perfilUpt)
                            Toast.makeText(getApplicationContext(),"Datos actualizados",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(),"Error en acutalizacion",Toast.LENGTH_LONG).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error inesperado",Toast.LENGTH_LONG).show();
            }
        }
    };

    private void cargaPerfil(){
        try {
            //asignando campos
            EditText txtNombre, txtApellido, txtCorreo, txtNacimiento;
            RadioButton rdbMasculino, rdbFemenino;
            TextView lblBienvenida= findViewById(R.id.lblBienvenida);

            txtNombre = findViewById(R.id.txtNombre);
            txtApellido = findViewById(R.id.txtApellido);
            txtCorreo = findViewById(R.id.txtCorreo);
            txtNacimiento = findViewById(R.id.txtNacimiento);
            rdbMasculino = findViewById(R.id.rdbMasculino);
            rdbFemenino = findViewById(R.id.rdbFemenino);


                //toma id usuario logueado
                SharedPreferences pref = getSharedPreferences("sesion",MODE_PRIVATE);
                String idUsuario = pref.getString("id",null);
                //preparamos los datos para insertarlo
                Map<String,String> usuario = new HashMap<>();
                usuario.put("ID_User",idUsuario);

                Conexion con = new Conexion();
                ResultSet usuarioData = con.getData("select * from usuario where  ID_User = ?",usuario);
                ResultSet perfilData = con.getData("select * from perfil where  ID_User = ?",usuario);

                //asgina los valores al formulario
                if(usuarioData.next() && perfilData.next()){
                    txtApellido.setText(perfilData.getString("Apellido"));
                    txtNombre.setText(perfilData.getString("Nombre"));
                    txtNacimiento.setText(perfilData.getString("Fecha_Birth"));

                    if(perfilData.getString("Sexo")=="Masculino")
                        rdbMasculino.setChecked(true);
                    else
                        rdbFemenino.setChecked(true);
                    txtCorreo.setText(usuarioData.getString("Email"));
                    lblBienvenida.setText("Bienvenido "+perfilData.getString("Nombre")+" "+perfilData.getString("Apellido"));
                }else{
                    Toast.makeText(getApplicationContext(),"No se encontro usuario",Toast.LENGTH_LONG).show();
                }



        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error inesperado",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        // gracias a la id, sabemos que item es el que se oprime, en este caso usamos un switch
        switch (item.getItemId())
        {
            case R.id.id_item2:
                intent = new Intent (Perfil.this, Registros.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.id_item3:
                intent = new Intent (Perfil.this, Perfil.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.id_item4:
                intent = new Intent (Perfil.this, Calculadora.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.id_item5:

                SharedPreferences pref = getSharedPreferences("sesion",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                intent = new Intent (Perfil.this, Home.class);
                startActivityForResult(intent, 0);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}
