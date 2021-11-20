package cat.copernic.jose.antonio.miranda.prodiscomtest.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context

class ErrorLogs {

     fun showError(context: Context) {
        val errorDis = AlertDialog.Builder(context)
        errorDis.setTitle("Inici de Sessió fallat")
        errorDis.setMessage("DNI o Contrasenya incorrectes!!!")
        errorDis.setPositiveButton("Aceptar", null)
        errorDis.show()
    }

}