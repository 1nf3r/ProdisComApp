package cat.copernic.jose.antonio.miranda.prodiscomtest.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context

class ErrorLogs {

     fun showError(context: Context, title: String, message: String) {
        val errorDis = AlertDialog.Builder(context)
        errorDis.setTitle(title)
        errorDis.setMessage(message)
        errorDis.setPositiveButton("Aceptar", null)
        errorDis.show()
    }

}