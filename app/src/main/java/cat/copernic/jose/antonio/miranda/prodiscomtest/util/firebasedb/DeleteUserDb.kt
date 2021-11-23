package cat.copernic.jose.antonio.miranda.prodiscomtest.util.firebasedb

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class DeleteUserDb {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var getUserInfo: DocumentReference
    private var found = false

//    AGAFA LA INFORMACIO DE LA CONSULTA
    private fun getInfo(mailUser: String): Boolean {
        found = false
        getUserInfo = db.collection("users").document(mailUser)
        getUserInfo.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    printInfo(
                        document.get("email") as String,
                        document.get("Nombre") as String,
                        document.get("DNI") as String
                    )
                    found = true
                } else {
//                    notFoundError()
                }
            }
        return found
    }

    //IMPRIMEIX EL RESULTAT EN EL TXTVIEW
    private fun printInfo(mail: String, nom: String, dni: String) {
       /* binding.txResultMail.text = mail
        binding.txResultNom.text = nom
        binding.txResultDni.text = dni*/
    }

    //SI L'USUARI NO ES TROVA MOSTRA L'ALERT
    private fun notFoundError(activity: Activity) {
        val errorDis = AlertDialog.Builder(activity)
        errorDis.setTitle("Usuari no trovat")
        errorDis.setMessage("No existeix cap usuari amb aquestes dades")
        errorDis.setPositiveButton("Aceptar", null)
        errorDis.show()
    }

    //FUNCIO PER BORRAR L'USUARI NOMES UN COP
    private fun delUser() {
        getUserInfo.delete()
        found = false
    }

    //CONFIRMACIO PER A ELIMIAR L'USUARI
    private val positiveButtonClick = { dialog: DialogInterface, which: Int -> delUser() }
    private fun conDelUser(activity: Activity) {
        val delDis = AlertDialog.Builder(activity)
        delDis.setTitle("Eliminar Usuari")
        delDis.setMessage("Estas segur que vols eliminar l'usuari? ")
        delDis.setPositiveButton(
            "Confirmar",
            DialogInterface.OnClickListener(function = positiveButtonClick)
        )
        delDis.setNegativeButton("Cancelar", null)
        delDis.show()
    }



}