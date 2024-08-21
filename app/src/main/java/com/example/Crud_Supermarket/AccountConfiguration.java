package com.example.cuentaspolizas;

public class AccountConfiguration {

    // Define constants for the account type levels
    public static final int TYPE_CUENTA = 1;
    public static final int TYPE_SUBCUENTA = 2;
    public static final int TYPE_SUBSUBCUENTA = 3;

    // Definir Tipos de Cuenta
    public static int getAccountType(String accountCode) {
        if (isValidAccountCode(accountCode)) {
            String[] parts = splitAccountCode(accountCode);

            if (parts[1].equals("00") && parts[2].equals("00")) {
                return TYPE_CUENTA;
            } else if (!parts[1].equals("00") && parts[2].equals("00")) {
                return TYPE_SUBCUENTA;
            } else {
                return TYPE_SUBSUBCUENTA;
            }
        }

        return -1; // Tipo Invalido
    }

    // validaciones
    private static boolean isValidAccountCode(String accountCode) {
        //accountCode != null
        return accountCode.length() == 6 && !accountCode.startsWith("00");
    }

    // Dividir la cadena
    private static String[] splitAccountCode(String accountCode) {
        return new String[]{
                accountCode.substring(0, 2),
                accountCode.substring(2, 4),
                accountCode.substring(4, 6)
        };
    }
}
