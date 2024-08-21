package com.example.cuentaspolizas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.EditText;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


public class activity_crud_polizas extends AppCompatActivity implements View.OnClickListener {
    Button btnGrabarP, btnLimpiarP;
    EditText txtPoliza;
    EditText[][] editTexts;
    BaseDeDatos conexion;
    SQLiteDatabase BD;
    String sql="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_polizas);

        try {
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
        } catch (Exception e) {
            Rutinas.mensajeDialog(e.getMessage(), this);
        }

        editTexts = new EditText[5][3];

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        // renglon de encabezado

        TableRow headerRow = new TableRow(this);
        headerRow.setGravity(Gravity.CENTER);

        for (int i = 0; i < 3; i++) {
            TextView headerTextView = new TextView(this);
            headerTextView.setText(i == 0 ? "CUENTA" : (i == 1 ? "CARGO" : "ABONO"));
            headerTextView.setGravity(Gravity.CENTER);

            // Set layout parameters with weight
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0, // Width
                    ViewGroup.LayoutParams.WRAP_CONTENT, // Altura
                    1f // Establecer el peso del diseño en 1
            );

            headerTextView.setLayoutParams(params);
            headerRow.addView(headerTextView);
        }
        // Añadir Fila al Encazado a la tabla
        tableLayout.addView(headerRow);
        // Añadir 5 Edittext a la tabla
        for (int i = 0; i < 5; i++) {
            // Create a new row
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);
            tableRow.setPadding(20, 20, 20, 0);

            // Add 3 EditTexts to each row
            for (int j = 0; j < 3; j++) {
                EditText editText = new EditText(this);
                editText.setLayoutParams(new TableRow.LayoutParams(
                        0, //width
                        ViewGroup.LayoutParams.WRAP_CONTENT, //altura
                        1f // Establecer el peso del diseño en 1
                ));
                tableRow.addView(editText);

                editTexts[i][j] = editText;
//              Limite de caracteres
                if (j == 0) {
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                }
            }
            tableLayout.addView(tableRow);
        }

        txtPoliza = findViewById(R.id.txtPoliza);

        // Botones
        btnLimpiarP = findViewById(R.id.btnLimpiarP);
        btnGrabarP = findViewById(R.id.btnGrabarP);

        btnLimpiarP.setOnClickListener(this);
        btnGrabarP.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        sql="";
        if (v == btnLimpiarP) {
            Limpiar();
        }
        if (v == btnGrabarP) {
            try {
                int cont = 0;
                if (TextUtils.isEmpty(txtPoliza.getText().toString())) {
                    Rutinas.mensajeDialog("Se requiere el numero de poliza", this);
                    return;
                }
                try {
                    Integer.parseInt(txtPoliza.getText().toString());
                } catch (Exception e) {
                    Rutinas.mensajeDialog("El numero de poliza debe ser numerico", this);
                    return;
                }

//          validar que la poliza no exista
                Cursor c = BD.rawQuery("SELECT PolizaID FROM POLIZA WHERE POLIZAID = '" + txtPoliza.getText().toString() + "'", null);
                if (c.moveToFirst()) {
                    Rutinas.mensajeDialog("La poliza ya existe", this);
                    return;
                }
                for (int i = 0; i < 5; i++) {
                    if (!TextUtils.isEmpty(editTexts[i][0].getText().toString())) {
                        cont++;
                    }
                }
                if (cont < 2) {
                    Rutinas.mensajeDialog("Inserte al menos 2 movimientos", this);
                    return;
                }

                for (int i = 0; i < 5; i++) {
                    if (TextUtils.isEmpty(editTexts[i][0].getText().toString())) {
                        continue;
                    }
                    if (!TextUtils.isEmpty(editTexts[i][1].getText().toString()) && !TextUtils.isEmpty(editTexts[i][2].getText().toString())){
                        if (Integer.parseInt(editTexts[i][1].getText().toString()) != 0 && Integer.parseInt(editTexts[i][2].getText().toString()) != 0) {
                            Rutinas.mensajeDialog("Solo debe haber un cargo o un abono", this);
                            return;
                        }
                    }
                    int nivelCuenta = AccountConfiguration.getAccountType(editTexts[i][0].getText().toString());
                    if (nivelCuenta != AccountConfiguration.TYPE_SUBSUBCUENTA) {
                        Rutinas.mensajeDialog("La cuenta debe ser de nivel 3", this);
                        return;
                    }
//              Validar si existe su cuenta nivel 1
                    c = BD.rawQuery("SELECT CUENTA FROM CATALOGO WHERE Cuenta like '" + editTexts[i][0].getText().toString().substring(0, 2) + "%'", null);
                    if (!c.moveToFirst()) {
                        Rutinas.mensajeDialog("La cuenta nivel 1 No existe", this);
                        return;
                    }
//              Validar si existe su cuenta nivel 2
                    c = BD.rawQuery("SELECT Cuenta FROM CATALOGO WHERE Cuenta like '" + editTexts[i][0].getText().toString().substring(0, 4) + "%'", null);
                    if (!c.moveToFirst()) {
                        Rutinas.mensajeDialog("La cuenta nivel 2 No existe", this);
                        return;
                    }
                }
//                Validar si existe la cuenta nivel 3
                for (int i = 0; i < 5; i++) {
                    if (TextUtils.isEmpty(editTexts[i][0].getText().toString())) {
                        continue;
                    }
                    c = BD.rawQuery("SELECT Cuenta FROM CATALOGO WHERE Cuenta = '" + editTexts[i][0].getText().toString() + "'", null);
                    if (!c.moveToFirst()) {
                        Rutinas.mensajeDialog("La cuenta"+ editTexts[i][0].getText().toString() +" nivel 3 No existe", this);
                        return;
                    }
                }

//                validar que el conjunto de todos los cargos y abonos sean iguales y sacar el importe
                int sumaCargos = 0;
                int sumaAbonos = 0;
                for (int i = 0; i < 5; i++) {
                    if (TextUtils.isEmpty(editTexts[i][0].getText().toString()) || TextUtils.isEmpty(editTexts[i][1].getText().toString()) || TextUtils.isEmpty(editTexts[i][2].getText().toString())) {
                        continue;
                    }
                    sumaCargos += Integer.parseInt(editTexts[i][1].getText().toString());
                    sumaAbonos += Integer.parseInt(editTexts[i][2].getText().toString());
                }
                if (sumaCargos != sumaAbonos) {
                    Rutinas.mensajeDialog("La suma de los cargos debe ser igual a la suma de los abonos", this);
                    return;
                }

                sql = "INSERT INTO POLIZA (PolizaID, Cuenta, TipoMov, importe) VALUES ('" + txtPoliza.getText().toString() + "', '";
//                grabar en la base de datos
                for (int i = 0; i < 5; i++) {
                    if (TextUtils.isEmpty(editTexts[i][0].getText().toString())) {
                        continue;
                    }
//                    tipo de movimiento
                    int tipoMov = 0;
                    if (!TextUtils.isEmpty(editTexts[i][1].getText().toString())) {
                        tipoMov = 1;
                    } else if (!TextUtils.isEmpty(editTexts[i][2].getText().toString())) {
                        tipoMov = 2;
                    }
                    String cuenta = editTexts[i][0].getText().toString();
                    String cuentaMayor = cuenta.substring(0, 2) + "0000";
                    String subCuenta = cuenta.substring(0, 4) + "00";

                    BD.beginTransaction();
                    if (tipoMov == 1)
                        insertarRegistroEnPoliza(sql, cuenta, tipoMov, editTexts[i][1].getText().toString());
                    else
                        insertarRegistroEnPoliza(sql, cuenta, tipoMov, editTexts[i][2].getText().toString());

                    updateCatalogo(subCuenta, tipoMov, editTexts[i][1].getText().toString());
                    updateCatalogo(cuentaMayor, tipoMov, editTexts[i][1].getText().toString());

                    BD.setTransactionSuccessful();
                    BD.endTransaction();
                }
                Rutinas.mensajeDialog("Poliza grabada", this);
                Limpiar();
            } catch (Exception e) {
                Rutinas.mensajeDialog(e.toString(), this);
                return;
            }
        }
    }

    private void insertarRegistroEnPoliza(String sql, String cuenta, int tipoMov, String importe) {
        String sqlInsert = sql + cuenta + "', " + tipoMov + ", " + importe + ")";
        BD.execSQL(sqlInsert);
    }
    private void updateCatalogo(String cuenta, int tipoMov, String importe) {
        String sqlUpdate = "UPDATE CATALOGO SET ";
        if (tipoMov == 1) {
            sqlUpdate += "CARGO = " + importe + ", ";
        } else {
            sqlUpdate += "ABONO = " + importe + ", ";
        }
        sqlUpdate += "WHERE CUENTA = '" + cuenta + "'";
        BD.execSQL(sqlUpdate);
    }

    private void Limpiar () {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++)
                editTexts[i][j].setText("");
        }
    }
}