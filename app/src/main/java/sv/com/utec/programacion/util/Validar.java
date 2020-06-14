package sv.com.utec.programacion.util;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

public class Validar {

    //metodo para validar si es un valor numerico
    public  boolean isNumeric(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
    }

    //metodo para validar si es un email
    public  boolean isEmail(String cadena) {
        boolean resultado;
        if (Patterns.EMAIL_ADDRESS.matcher(cadena).matches()) {
            resultado = true;
        } else {
            resultado = false;
        }

        return resultado;
    }

    //metodo para validar si editext esta vacio
    public  boolean Vacio(EditText campo){
        String dato = campo.getText().toString().trim();
        if(TextUtils.isEmpty(dato)){
            campo.setError("Campo Requerido");
            campo.requestFocus();
            return true;
        }
        else{
            return false;
        }
    }

    //metodo para validar si es un valor numerico con decimales
    public  boolean isDouble(String cadena) {
        boolean resultado;
        try {
            Double.parseDouble(cadena);
            resultado = true;
        } catch (NumberFormatException nfe) {
            resultado = false;
        }
        return resultado;
    }

    //metodo para validar si el peso no es excesivo
    public  boolean isPeso(String cadena) {
        boolean resultado;
        try
        {
            if( Double.parseDouble(cadena)<635 && Double.parseDouble(cadena) >20) //el record de peso de una persona humana es de 635 Kg
            {
                resultado = true;
            }
            else
            {
                resultado = false;
            }
        }
        catch (Exception e)
        {
            resultado = false;
        }

        return resultado;
    }

    //metodo para validar si la altura no es excesiva
    public  boolean isAltura(String cadena) {
        boolean resultado;
        try
        {
            if(Double.parseDouble(cadena)<2.72 && Double.parseDouble(cadena)>0.60) //el record de altura humana es de 2..72 y el hombres mas pequeño mide 0.70 mt
            {
                resultado = true;
            }
            else
            {
                resultado = false;
            }
        }
        catch (Exception e)
        {
            resultado = false;
        }

        return resultado;
    }



}
