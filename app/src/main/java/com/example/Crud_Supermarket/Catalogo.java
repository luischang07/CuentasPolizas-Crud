package com.example.cuentaspolizas;

import java.io.Serializable;
import java.util.ArrayList;

public class Catalogo implements Serializable {
    String Cuenta;
    String Nombre;
    int Cargo;
    int Abono;
    int Nivel;

    public Catalogo(String cuenta, String nombre, int cargo, int abono, int nivel) {
        Cuenta = cuenta;
        Nombre = nombre;
        Cargo = cargo;
        Abono = abono;
        Nivel = nivel;
    }

    public String getCuenta() {
        return Cuenta;
    }

    public String getNombre() {
        return Nombre;
    }
    public void setNombre(String nombre) {
        Nombre = nombre;
    }
    public int getCargo() {
        return Cargo;
    }
    public void setCargo(int cargo) {
        Cargo = cargo;
    }
    public int getAbono() {
        return Abono;
    }
    public void setAbono(int abono) {
        Abono = abono;
    }
    public int getNivel() {
        return Nivel;
    }
}
