package com.example.cuentaspolizas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class activity_consulta_polizas extends AppCompatActivity implements View.OnClickListener {
    Button btnConsultarPoliza, btnConsultarCatalogo2;
    EditText txtPoliza;
    BaseDeDatos conexion;
    SQLiteDatabase BD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_polizas);

        btnConsultarPoliza = findViewById(R.id.btnConsultarPoliza);
        btnConsultarCatalogo2 = findViewById(R.id.btnConsultarCatalogo2);
        txtPoliza = findViewById(R.id.txtPoliza);

        conexion = new BaseDeDatos(this, "CONTA", null, BaseDeDatos.version);
        if (conexion == null) {
            Rutinas.mensajeDialog("No se ha conectado a la BD", this);
            return;
        }
        BD = conexion.getWritableDatabase();
        if (BD == null) {
            Rutinas.mensajeDialog("La BD no se ha establecido para lectura y escritura", this);
            return;
        }
        Rutinas.mensajeToast("conexion exitosa", this);

        btnConsultarPoliza.setOnClickListener(this);
        btnConsultarCatalogo2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == btnConsultarPoliza){
            try {
                if (TextUtils.isEmpty(txtPoliza.getText().toString())) {
                    Rutinas.mensajeDialog("Debe capturar la poliza", this);
                    return;
                }
                String sql = "SELECT * FROM POLIZA WHERE POLIZAID = '" + txtPoliza.getText().toString() + "'";
                Cursor cursor = BD.rawQuery(sql, null);
                cursor.moveToFirst();
                if (cursor.getCount() == 0) {
                    Rutinas.mensajeDialog("No se encontró la poliza", this);
                    return;
                }
                Intent intent = new Intent(this, PolizaView.class);
                intent.putExtra("Poliza", new Poliza(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4)));

                startActivity(intent);
            }catch (Exception e){
                Rutinas.mensajeDialog("Error: " + e.getMessage(), this);
                return;
            }




        }
        if(v == btnConsultarCatalogo2){
            String sql = "SELECT * FROM CATALOGO ORDER BY SUBSTR(CUENTA, 1, 2), SUBSTR(CUENTA, 3, 2), SUBSTR(CUENTA, 5)";
            ArrayList<Catalogo> catalogoList = new ArrayList<>();
            Cursor cursor = BD.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                catalogoList.add(new Catalogo(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4)));
                cursor.moveToNext();
            }
            Rutinas.mensajeToast("Se encontraron " + catalogoList.size() + " registros", this);
            Intent intent = new Intent(this, ReciclerView.class);
            intent.putExtra("catalogoList", catalogoList);
            startActivity(intent);
            return;
        }
    }
}
