package com.example.cuentaspolizas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.io.Serializable;
public class BaseDeDatos extends SQLiteOpenHelper {
    static int version=3;
    String catalogo = "CREATE TABLE CATALOGO (CUENTA TEXT PRIMARY KEY, NOMBRE TEXT, CARGO INTEGER, ABONO INTEGER, NIVEL INTEGER )";
    String poliza = "CREATE TABLE POLIZA (POLIZAID TEXT, FECHA DATE DEFAULT CURRENT_DATE, CUENTA TEXT, TIPOMOV INTEGER, IMPORTE INTEGER, FOREIGN KEY (CUENTA) REFERENCES CATALOGO(CUENTA), PRIMARY KEY (Poliza, Cuenta))";
    public BaseDeDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(catalogo);
        db.execSQL(poliza);
        System.out.println("constrtuctor");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CATALOGO");
        db.execSQL(catalogo);
        System.out.println("actualizacion");
    }
}

