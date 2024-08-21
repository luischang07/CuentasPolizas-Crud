package com.example.cuentaspolizas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class PolizaView extends AppCompatActivity {

    TextView txtPoliza, txtFecha, txtCuenta, txtTipoMov, txtImporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recicler_view_poliza);

        ArrayList<Poliza> Poliza = (ArrayList<Poliza>) getIntent().getSerializableExtra("Poliza");

        txtPoliza = findViewById(R.id.Poliza2);
        txtFecha = findViewById(R.id.Fecha2);
        txtCuenta = findViewById(R.id.cuenta);
        txtTipoMov = findViewById(R.id.TIPOMOV2);
        txtImporte = findViewById(R.id.IMPORTE2);

        txtPoliza.setText(Poliza.get(0).getPoliza());
        txtFecha.setText(Poliza.get(0).getFecha());
        txtCuenta.setText(Poliza.get(0).getCuenta());
        txtTipoMov.setText(Poliza.get(0).getTipoMovimiento());
        txtImporte.setText(Poliza.get(0).getImporte());
    }
}