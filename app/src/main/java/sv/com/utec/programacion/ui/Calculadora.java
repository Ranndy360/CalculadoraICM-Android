package sv.com.utec.programacion.ui;

import androidx.appcompat.app.AppCompatActivity;
import sv.com.utec.programacion.R;
import sv.com.utec.programacion.pojo.Conexion;
import sv.com.utec.programacion.util.Validar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.FontsContract;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.content.SharedPreferences;

public class Calculadora extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        Button btnCalcular = (Button) findViewById(R.id.btnCalcular);
        Button btnRegresar = (Button) findViewById(R.id.btnRegresar);
        btnCalcular.setOnClickListener(Calcular);
        btnRegresar.setOnClickListener(goRegistros);
    }

    private View.OnClickListener Calcular = new View.OnClickListener() {
        public void onClick(View v) {
            try
            {
                //Asignacion de Variables
                Double IMC=0.0, Peso=0.0, Altura=0.0;
                Integer ID_Estado=1;
                //Obteniendo el ID_Perfil
                SharedPreferences pref = getSharedPreferences("sesion",MODE_PRIVATE);
                String ID_Perfil=pref.getString("id",null);
                //String ID_Perfil="1";//Mientras no se puedan usar variables globales dejo para el unico de prueba

                EditText txtPeso, txtAltura;
                TextView lblValidar = findViewById(R.id.lblValidar);
                lblValidar.setVisibility(View.INVISIBLE);
                txtPeso=findViewById(R.id.txtPeso);
                txtAltura=findViewById(R.id.txtAltura);

                //Validacion de campos
                Validar validar = new Validar();

                boolean formVacio = false; //Para comprobar que no este vacio
                boolean formDouble = false;//Para comprobar que sean datos tipo double
                boolean formDatos = false;//Para comprobar que los datos sean realistad

                if(!validar.Vacio(txtPeso)){
                    if(!validar.Vacio(txtAltura)) {
                        formVacio=true;
                    }
                }

                if(!validar.isDouble(txtPeso.getText().toString())) {//Se comprueba si es un numero
                    if(!validar.isDouble(txtAltura.getText().toString())) {//se comprueba si es un numero
                        formDouble=true;
                    }
                }

                if(!validar.isPeso(txtPeso.getText().toString())) {//Aqui evitamos que el peso sea irrealista
                    if(!validar.isAltura(txtAltura.getText().toString())) {//Aqui evitamos que la altura sea irrealista
                        formDatos = true;
                    }
                }

                //Calculando IMC
                if(formVacio==false && formDouble==false && formDatos==false)
                {
                    lblValidar.setVisibility(View.VISIBLE);
                }
                else
                {
                    Peso = Double.parseDouble(txtPeso.getText().toString());
                    Altura = Double.parseDouble(txtAltura.getText().toString());
                    IMC = Peso/ Math.pow(Altura, 2);

                    //Evaluando el Indice IMC
                    if (IMC>=18.5&&IMC<=24.9)
                    {
                        ID_Estado=2;
                    }
                    else if(IMC>=25&&IMC<=26.9)
                    {
                        ID_Estado=3;
                    }
                    else if(IMC>=27&&IMC<=29.9)
                    {
                        ID_Estado=4;
                    }
                    else if(IMC>=30&&IMC<=34.9)
                    {
                        ID_Estado=5;
                    }
                    else if(IMC>=35&&IMC<=39.9)
                    {
                        ID_Estado=6;
                    }
                    else if(IMC>=40&&IMC<=49.9)
                    {
                        ID_Estado=7;
                    }
                    else if(IMC>=50)
                    {
                        ID_Estado=8;
                    }



                    //Definiendo datos a ingresar
                    java.util.Date Fecha = new Date();//Con esto se obtiene la fecha del dia de hoy

                    Map<String,String> Registro = new HashMap<>();
                    Registro.put("0",ID_Perfil);
                    Registro.put("1", Fecha.toString());
                    Registro.put("2", Altura.toString());
                    Registro.put("3", Peso.toString());
                    Registro.put("4", IMC.toString());
                    Registro.put("5", ID_Estado.toString());

                    //Insertando en la base de datos
                    Conexion con = new Conexion();
                    boolean resp = con.setData("INSERT INTO registro (ID_Perfil, Fecha, Altura, Peso, IMC, ID_Estado) VALUES(?,?,?,?,?,?)",Registro);



                    if(resp)
                        Toast.makeText(getApplicationContext(), "Registro Insertado", Toast.LENGTH_LONG).show();
                        txtAltura.setText("");
                        txtPeso.setText("");

                }


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error inesperado", Toast.LENGTH_LONG).show();
            }
        }
    };

    private View.OnClickListener goRegistros = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent (v.getContext(), Registros.class);
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
                intent = new Intent (Calculadora.this, Registros.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.id_item3:
                intent = new Intent (Calculadora.this, Perfil.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.id_item4:
                intent = new Intent (Calculadora.this, Calculadora.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.id_item5:

                SharedPreferences pref = getSharedPreferences("sesion",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                intent = new Intent (Calculadora.this, Home.class);
                startActivityForResult(intent, 0);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}

