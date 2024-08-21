package com.example.cuentaspolizas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class CrudCuentas extends AppCompatActivity implements View.OnClickListener {
    ArrayList <Catalogo> catalogoList;
    Button btnGrabar, btnBorrar, btnModificar, btnConsultarCatalogo, btnLimpiar, btnRecuperar, btnBack;
    EditText txtNumCuenta, txtNombre, txtCargo, txtAbono;
    BaseDeDatos conexion;
    SQLiteDatabase BD;
    AccountConfiguration acConf;
    int nivelCuenta;
    String sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_cuentas);

        btnGrabar = findViewById(R.id.btnGrabar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnModificar = findViewById(R.id.btnModificar);
        btnConsultarCatalogo = findViewById(R.id.btnConsultarCatalogo);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        btnRecuperar = findViewById(R.id.btnRecuperar);
        btnBack = findViewById(R.id.btnBack);

        txtNumCuenta = findViewById(R.id.txtNumCuenta);
        txtNombre = findViewById(R.id.txtPoliza);
        txtCargo = findViewById(R.id.txtCargo);
        txtAbono = findViewById(R.id.txtAbono);

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

        btnGrabar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnConsultarCatalogo.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        btnRecuperar.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        sql= "";
        if(v == btnLimpiar) {
            Limpiar();
            return;
        }
//      Volver al mainActivity
        if(btnBack == v){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return;
        }
        if (v == btnGrabar) {
            try {
            if (datosFaltantes(4)){
                return;
            }
            sql = "SELECT CUENTA FROM CATALOGO WHERE CUENTA = '" + txtNumCuenta.getText().toString()+"'";
            Cursor cursor= BD.rawQuery(sql,null);
            if(cursor.getCount()>0){
                Rutinas.mensajeDialog("LA CUENTA "+txtNumCuenta.getText().toString()+", YA EXISTE ", this);
                txtNumCuenta.requestFocus();
                return;
            }
//          Validar la cadena
            nivelCuenta = acConf.getAccountType(txtNumCuenta.getText().toString());
            if (nivelCuenta == -1) {
                Rutinas.mensajeDialog("La cuenta "+txtNumCuenta.getText().toString()+" no es valida", this);
                txtNumCuenta.requestFocus();
                return;
            }
//          validar cargo y abono
            try {
                if (Integer.parseInt(txtCargo.getText().toString()) != 0 || Integer.parseInt(txtAbono.getText().toString()) != 0) {
                    Rutinas.mensajeDialog("El cargo y abono no pueden ser diferentes de cero", this);
                    txtCargo.requestFocus();
                    return;
                }
            }catch (Exception e){
                Rutinas.mensajeDialog("El cargo y abono deben ser numericos", this);
                txtCargo.requestFocus();
                return;
            }
            if (nivelCuenta == AccountConfiguration.TYPE_SUBCUENTA) {
//              Validar si existe su cuenta nivel 1
                sql = "SELECT CUENTA FROM CATALOGO WHERE CUENTA LIKE '"+ txtNumCuenta.getText().toString().substring(0, 2)+"%'";
                cursor = BD.rawQuery(sql, null);
                if (cursor.getCount() == 0) {
                    Rutinas.mensajeDialog("LA CUENTA DE NIVEL 1 NO EXISTE", this);
                    txtNumCuenta.requestFocus();
                    return;
                }
            }
            if (nivelCuenta == AccountConfiguration.TYPE_SUBSUBCUENTA) {
//              Validar si existe su cuenta nivel 1 y 2
                sql = "SELECT CUENTA FROM CATALOGO WHERE CUENTA LIKE '"+ txtNumCuenta.getText().toString().substring(0, 2)+"%'";
                cursor = BD.rawQuery(sql, null);
                if (cursor.getCount() == 0) {
                    Rutinas.mensajeDialog("LA CUENTA DE NIVEL 1 NO EXISTE", this);
                    txtNumCuenta.requestFocus();
                    return;
                }
                sql = "SELECT CUENTA FROM CATALOGO WHERE CUENTA LIKE '"+ txtNumCuenta.getText().toString().substring(0, 4)+"%'";
                cursor = BD.rawQuery(sql, null);
                if (cursor.getCount() == 0) {
                    Rutinas.mensajeDialog("LA CUENTA DE NIVEL 2 NO EXISTE", this);
                    txtNumCuenta.requestFocus();
                    return;
                }
            }
            String sql = "INSERT INTO CATALOGO VALUES('" + txtNumCuenta.getText().toString() + "','" + txtNombre.getText().toString() + "'," + txtCargo.getText().toString() + "," + txtAbono.getText().toString() + "," + nivelCuenta + ")";
            BD.execSQL(sql);
            Rutinas.mensajeDialog("Se grabo correctamente", this);
            } catch (Exception e) {
                Rutinas.mensajeDialog("Error al grabar"+e.getMessage(), this);
            }
            return;
        }
        if (v == btnBorrar) {
            if (datosFaltantes(1)) {
                return;
            }
//          Validar la cadena y obtener el nivel de la cuenta
            nivelCuenta = acConf.getAccountType(txtNumCuenta.getText().toString());
            if (nivelCuenta == -1) {
                Rutinas.mensajeDialog("La cuenta "+txtNumCuenta.getText().toString()+" no es valida", this);
                txtNumCuenta.requestFocus();
                return;
            }
//          Buscar la cuenta en el catalogo
            sql = "SELECT CUENTA FROM CATALOGO WHERE CUENTA = '" + txtNumCuenta.getText().toString()+"'";
            Cursor cursor= BD.rawQuery(sql,null);
            if(cursor.getCount()==0){
                Rutinas.mensajeDialog("LA CUENTA"+txtNumCuenta.getText().toString()+", NO EXISTE ", this);
                txtNumCuenta.requestFocus();
                return;
            }

//          Buscar la cuenta si no tiene movimientos
            sql = "SELECT CARGO, ABONO FROM CATALOGO WHERE CUENTA = '" + txtNumCuenta.getText().toString()+"'";
            cursor= BD.rawQuery(sql,null);
            cursor.moveToFirst();
            if(cursor.getInt(0)!=0 && cursor.getInt(1)!=0){
                Rutinas.mensajeDialog("LA CUENTA "+txtNumCuenta.getText().toString()+" TIENE MOVIMIENTOS, NO SE PUEDE BORRAR", this);
                txtNumCuenta.requestFocus();
                return;
            }
            if (nivelCuenta == AccountConfiguration.TYPE_CUENTA) {
//              Buscar la cuenta si no tiene movimientos
                cursor= BD.rawQuery(sql,null);
                cursor.moveToFirst();
                if(cursor.getInt(0)==0 && cursor.getInt(1)==0){
                    borrar(nivelCuenta);
                    txtNumCuenta.requestFocus();
                    return;
                }
//              Buscar si la cuenta tiene subcuentas
                sql = "SELECT Count(*) FROM CATALOGO WHERE CUENTA LIKE '" + txtNumCuenta.getText().toString().substring(0, 2) + "%'";
                cursor = BD.rawQuery(sql, null);
                if (cursor.getCount() > 1) {
                    Rutinas.mensajeDialog("LA CUENTA "+txtNumCuenta.getText().toString()+" TIENE SUBCUENTAS, NO SE PUEDE BORRAR", this);
                    txtNumCuenta.requestFocus();
                    return;
                }
                borrar(nivelCuenta);
                return;
            }
            if (nivelCuenta == AccountConfiguration.TYPE_SUBCUENTA) {
//              Buscar si la cuenta tiene subsubcuentas
                sql = "SELECT Count(*) FROM CATALOGO WHERE CUENTA LIKE '" + txtNumCuenta.getText().toString().substring(0, 4) + "%'";
                cursor = BD.rawQuery(sql, null);
                if (cursor.getCount() > 1) {
                    Rutinas.mensajeDialog("LA CUENTA "+txtNumCuenta.getText().toString()+" TIENE SUBCUENTAS, NO SE PUEDE BORRAR", this);
                    txtNumCuenta.requestFocus();
                    return;
                }
                borrar(nivelCuenta);
                return;
            }
            if (nivelCuenta == AccountConfiguration.TYPE_SUBSUBCUENTA) {
                borrar(nivelCuenta);
                return;
            }
        }
//      Para modificar solo se puede modificar el nombre de la cuenta.
        if (v == btnModificar) {
            if (datosFaltantes(1)) {
                return;
            }
            if (!txtCargo.getText().toString().isEmpty() || !txtAbono.getText().toString().isEmpty()) {
                Rutinas.mensajeDialog("Solo se puede modificar el nombre de la cuenta", this);
                txtNombre.requestFocus();
                return;
            }
//          Validar la cadena
            nivelCuenta = acConf.getAccountType(txtNumCuenta.getText().toString());
            if (nivelCuenta == -1) {
                Rutinas.mensajeDialog("La cuenta "+txtNumCuenta.getText().toString()+" no es valida", this);
                txtNumCuenta.requestFocus();
                return;
            }
//          Buscar la cuenta en el catalogo
            sql = "SELECT CUENTA FROM CATALOGO WHERE CUENTA = '" + txtNumCuenta.getText().toString()+"'";
            Cursor cursor= BD.rawQuery(sql,null);
            if(cursor.getCount()==0){
                Rutinas.mensajeDialog("LA CUENTA"+txtNumCuenta.getText().toString()+", NO EXISTE ", this);
                txtNumCuenta.requestFocus();
                return;
            }
            sql = "UPDATE CATALOGO SET NOMBRE = '" + txtNombre.getText().toString() + "' WHERE CUENTA = '" + txtNumCuenta.getText().toString()+"'";
            try {
                BD.execSQL(sql);
                Rutinas.mensajeToast("Se modifico correctamente el nombre de la cuenta", this);
            } catch (Exception e) {
                Rutinas.mensajeDialog("Error al modificar", this);
            }
        }
        if (v == btnRecuperar){
            if (datosFaltantes(1)) {
                return;
            }

//          Validar la cadena
            nivelCuenta = acConf.getAccountType(txtNumCuenta.getText().toString());
            if (nivelCuenta == -1) {
                Rutinas.mensajeDialog("La cuenta " + txtNumCuenta.getText().toString() + " no es valida", this);
                txtNumCuenta.requestFocus();
                return;
            }

//          Buscar la cuenta en el catalogo
            sql = "SELECT CUENTA FROM CATALOGO WHERE CUENTA = '" + txtNumCuenta.getText().toString() + "'";
            Cursor cursor = BD.rawQuery(sql, null);
            if (cursor.getCount() == 0) {
                Rutinas.mensajeDialog("LA CUENTA " + txtNumCuenta.getText().toString() + ", NO EXISTE ", this);
                txtNumCuenta.requestFocus();
                return;
            }

            sql = "SELECT NOMBRE, CARGO, ABONO FROM CATALOGO WHERE CUENTA = '" + txtNumCuenta.getText().toString() + "'";
            cursor = BD.rawQuery(sql, null);
            cursor.moveToFirst();
            txtNombre.setText(cursor.getString(0));
            txtCargo.setText(cursor.getString(1));
            txtAbono.setText(cursor.getString(2));
            return;
        }
        if (v == btnConsultarCatalogo) {
            if (txtNumCuenta.getText().toString().isEmpty()) {
                sql = "SELECT * FROM CATALOGO ORDER BY SUBSTR(CUENTA, 1, 2), SUBSTR(CUENTA, 3, 2), SUBSTR(CUENTA, 5)";
            } else {
                nivelCuenta = acConf.getAccountType(txtNumCuenta.getText().toString());
                if (nivelCuenta == -1) {
                    Rutinas.mensajeDialog("La cuenta " + txtNumCuenta.getText().toString() + " no es valida", this);
                    txtNumCuenta.requestFocus();
                    return;
                }

                String baseSql = "SELECT * FROM CATALOGO WHERE CUENTA LIKE '";

                if (nivelCuenta == AccountConfiguration.TYPE_CUENTA) {
                    sql = baseSql + txtNumCuenta.getText().toString().substring(0, 2);
                } else if (nivelCuenta == AccountConfiguration.TYPE_SUBCUENTA) {
                    sql = baseSql + txtNumCuenta.getText().toString().substring(0, 4);
                } else if (nivelCuenta == AccountConfiguration.TYPE_SUBSUBCUENTA) {
                    sql = baseSql + txtNumCuenta.getText().toString();
                }
                sql += "%' ORDER BY SUBSTR(CUENTA, 1, 2), SUBSTR(CUENTA, 3, 2), SUBSTR(CUENTA, 5)";
            }

            catalogoList = new ArrayList<>();
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
    private boolean datosFaltantes (int numeroDatosAValidar) {
        switch (numeroDatosAValidar){
            case 1:
                if (txtNumCuenta.getText().toString().isEmpty()) {
                    Rutinas.mensajeDialog("Falta el numero de cuenta", this);
                    txtNumCuenta.requestFocus();
                    return true;
                }
                return false;
            case 4:
                if (txtNumCuenta.getText().toString().isEmpty()) {
                    Rutinas.mensajeDialog("Falta el numero de cuenta", this);
                    txtNumCuenta.requestFocus();
                    return true;
                }
                if (txtNombre.getText().toString().isEmpty()) {
                    Rutinas.mensajeDialog("Falta el nombre de la cuenta", this);
                    txtNombre.requestFocus();
                    return true;
                }
                if (txtCargo.getText().toString().isEmpty()) {
                    Rutinas.mensajeDialog("Falta el cargo de la cuenta", this);
                    txtCargo.requestFocus();
                    return true;
                }
                if (txtAbono.getText().toString().isEmpty()) {
                    Rutinas.mensajeDialog("Falta el abono de la cuenta", this);
                    txtAbono.requestFocus();
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
    private void borrar(int nivelCuenta){
        if (nivelCuenta == AccountConfiguration.TYPE_CUENTA) {
            sql = "DELETE FROM CATALOGO WHERE CUENTA LIKE '" + txtNumCuenta.getText().toString().substring(0, 2) + "%'";
            BD.execSQL(sql);
            Rutinas.mensajeDialog("CUENTA "+txtNumCuenta.getText().toString()+" y SUBCUENTAS ELIMINADA EXITOSAMENTE", this);
            Limpiar();
            return;
        }
        if (nivelCuenta == AccountConfiguration.TYPE_SUBCUENTA) {
            sql = "DELETE FROM CATALOGO WHERE CUENTA LIKE '" + txtNumCuenta.getText().toString().substring(0, 4) + "%'";
            BD.execSQL(sql);
            Rutinas.mensajeDialog("CUENTAS "+txtNumCuenta.getText().toString()+" y SUBCUENTAS ELIMINADA EXITOSAMENTE", this);
            Limpiar();
            return;
        }
        if (nivelCuenta == AccountConfiguration.TYPE_SUBSUBCUENTA) {
            sql = "DELETE FROM CATALOGO WHERE CUENTA = '" + txtNumCuenta.getText().toString() + "'";
            BD.execSQL(sql);
            Rutinas.mensajeToast("CUENTA "+txtNumCuenta.getText().toString()+" ELIMINADA EXITOSAMENTE", this);
            Limpiar();
            return;
        }
    }
    private void Limpiar () {
        txtNumCuenta.setText("");
        txtNombre.setText("");
        txtCargo.setText("");
        txtAbono.setText("");
        txtNumCuenta.requestFocus();
    }
}