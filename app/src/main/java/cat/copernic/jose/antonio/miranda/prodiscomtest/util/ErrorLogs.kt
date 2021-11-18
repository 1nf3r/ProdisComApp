package cat.copernic.jose.antonio.miranda.prodiscomtest.util

import android.app.Activity
import android.app.AlertDialog

class ErrorLogs {

     fun showError(activity: Activity) {
        val errorDis = AlertDialog.Builder(activity)
        errorDis.setTitle("Inici de Sessió fallat")
        errorDis.setMessage("DNI o Contrasenya incorrectes!!!")
        errorDis.setPositiveButton("Aceptar", null)
        errorDis.show()
    }

}