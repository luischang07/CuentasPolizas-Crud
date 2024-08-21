# Cuentas Polizas
Suponga que Ud. le han propuesto que desarrolle una aplicación móvil que permita registrar los movimientos contables de la empresa “Exacta”.
La empresa “Exacta” utiliza un catálogo de cuentas a 6 dígitos, donde:

•	Los dos primero representa la cuenta.
•	Los dos siguiente la sub cuenta.
•	Los dos siguiente la sub sub cuenta.

Ejemplos:
```
090000 Bancos
   090100 Bancos nacionales
       090101 Banorte
       090102 Banejercito
       090103 Bancoppel
    090200 Bancos extranjeros
       090201 BBVA
       090201 Banamex
100000 Cajas
    100100 Caja chica
        100101 Direccion        
        100102 Gerencia        
    100200 Caja  
         100201 Cobro
```         
Debe definir en SQLite las tablas que se muestran a continuación:

![tabla catalogo](https://github.com/user-attachments/assets/9f84e1bf-703a-4fa7-9c19-e2f7c9d2d2aa)
![tabla Poliza](https://github.com/user-attachments/assets/47df4865-f41c-49af-866a-f3e8884ce8ee)

**Se desea que desarrolle una aplicación Android que permita:**

1.	Realizar CRUD del catálogo de cuentas.
3.	Registro de pólizas.
4.	Consultas.

**En la opción 1.**

Para registrar una subsub cuenta debe estar registrada de sub cuenta y para poder registrar una sub debe estar registrada l cuenta.
Para borrar una cuenta no debe tener registradas subcuenta o subsub cuenta y además el cargo y abono debe estar en cero.
Permitir borrar todas las cuentas de mayor y sus subcuentas y subsubcuentas  no hayan tenido movimientos ( cargo y abono sea cero).

Para actualiza solo podrá modificar nombre de la cuenta.

Para consultar, proporcione la cuenta y muestre la información de la cuenta.

**En la opción 2.**

Podrá capturar una póliza que permita un mínimo de dos movimientos contable (cuentas de subsub) y un máximo de 5, la póliza debe cuadrar en sus cargos y abonos. Debe de afectar todas las cuentas de donde depende la subsub cuenta.

**En la opción 1.**

Podrá proporcionar una cuenta y mostrar todas las sus y sub sub cuentas que dependan de ella mostrando el nombre, cargo y abono.

Podrá proporcionar el número de póliza y mostrala
