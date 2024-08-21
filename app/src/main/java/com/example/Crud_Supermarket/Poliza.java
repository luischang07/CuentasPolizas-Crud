package com.example.cuentaspolizas;

import java.io.Serializable;

public class Poliza implements Serializable {
    String Poliza;
    String Fecha;
    String Cuenta;
    int TipoMovimiento;
    int Importe;

    public Poliza(String poliza, String fecha, String cuenta, int tipoMovimiento, int importe) {
        Poliza = poliza;
        Fecha = fecha;
        Cuenta = cuenta;
        TipoMovimiento = tipoMovimiento;
        Importe = importe;
    }
    public String getPoliza() {
        return Poliza;
    }
    public void setPoliza(String poliza) {
        Poliza = poliza;
    }
    public String getFecha() {
        return Fecha;
    }
    public void setFecha(String fecha) {
        Fecha = fecha;
    }
    public String getCuenta() {
        return Cuenta;
    }
    public int getTipoMovimiento() {
        return TipoMovimiento;
    }
    public int getImporte() {
        return Importe;
    }
    public void setImporte(int importe) {
        Importe = importe;
    }
}
