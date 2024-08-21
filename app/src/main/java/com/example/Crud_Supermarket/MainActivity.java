package com.example.cuentaspolizas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/* Tópicos Avanzados de Programación
* Proyecto Moviles
* Horario: 09:00 - 10:00
* Profesor: DR. Clemente García Gerardo
* Alumno: Acosta Chang Luis Xavier
* Fecha: 08/12/2023
* */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnCuentas, btnRegistroPolizas, btnConsultaPolizas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCuentas = findViewById(R.id.btnCuentas);
        btnRegistroPolizas = findViewById(R.id.btnRegistroPolizas);
        btnConsultaPolizas = findViewById(R.id.btnConsultas);

        btnCuentas.setOnClickListener(this);
        btnRegistroPolizas.setOnClickListener(this);
        btnConsultaPolizas.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == btnCuentas) {
            startActivity(new Intent(this, CrudCuentas.class));
        }
        if (v == btnRegistroPolizas) {
            startActivity(new Intent(this, activity_crud_polizas.class));
        }
        if (v == btnConsultaPolizas) {
            startActivity(new Intent(this, activity_consulta_polizas.class));
        }
    }
}
