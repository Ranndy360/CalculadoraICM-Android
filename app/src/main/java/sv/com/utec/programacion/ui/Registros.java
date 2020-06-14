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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Registros extends AppCompatActivity {
    ListView lvRegistro;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);
        lvRegistro =(ListView) findViewById(R.id.lvRegistro);


        //Ir a calculadora
        Button btnCalculadora = (Button)findViewById(R.id.btnCalculadora);
        btnCalculadora.setOnClickListener(goCalculadora);

        consultarRegistros();

    }

    private void consultarRegistros() {
        try {
            //Obteniendo el ID_Perfil
            SharedPreferences pref = getSharedPreferences("sesion",MODE_PRIVATE);
            String ID_Perfil=pref.getString("id",null);
            //String ID_Perfil="1";//Mientras no se puedan usar variables globales dejo para el unico de prueba
            ArrayList<String> listaRegistro = new ArrayList<>();

            Map<String,String> registro = new HashMap<>();
            registro.put("0",ID_Perfil);

            Conexion con = new Conexion();
            ResultSet resp = con.getData("SELECT R.Fecha, R.Altura, R.Peso, R.IMC, E.Nombre_Estado FROM registro R JOIN estado E ON R.ID_Estado = E.ID_Estado WHERE R.ID_Perfil=?",registro);

            while(resp.next()){
                listaRegistro.add(0,"Registro "+resp.getString("R.Fecha"));
                listaRegistro.add(1,"Altura: "+resp.getString("R.Altura"));
                listaRegistro.add(2,"Peso: "+resp.getString("R.Peso"));
                listaRegistro.add(3,"IMC: "+resp.getString("R.IMC"));
                listaRegistro.add(4,"Resultado: "+resp.getString("E.Nombre_Estado"));
                listaRegistro.add(5," ");

            }
            ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listaRegistro);
            lvRegistro.setAdapter(adaptador);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error inesperado",Toast.LENGTH_LONG).show();
        }
    }



    private View.OnClickListener goCalculadora = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent (v.getContext(), Calculadora.class);
            startActivityForResult(intent, 0);
        }
    };


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
                intent = new Intent (Registros.this, Registros.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.id_item3:
                intent = new Intent (Registros.this, Perfil.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.id_item4:
                intent = new Intent (Registros.this, Calculadora.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.id_item5:

                SharedPreferences pref = getSharedPreferences("sesion",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                intent = new Intent (Registros.this, Home.class);
                startActivityForResult(intent, 0);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
