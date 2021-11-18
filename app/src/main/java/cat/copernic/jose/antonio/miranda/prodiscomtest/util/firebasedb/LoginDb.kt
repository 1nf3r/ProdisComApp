package cat.copernic.jose.antonio.miranda.prodiscomtest.util.firebasedb


import cat.copernic.jose.antonio.miranda.prodiscomtest.data.UserFormData
import cat.copernic.jose.antonio.miranda.prodiscomtest.util.ErrorLogs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginDb {

    private val currentUser:  UserFormData = UserFormData()
    private val db = FirebaseFirestore.getInstance()
    private val errorLog: ErrorLogs = ErrorLogs()

    private fun loginWithEmail(email: String) :Boolean{
        var logSuccess = false
        var realPass = "Prodis"
        realPass += currentUser.passwd
        Firebase.auth.signInWithEmailAndPassword(email, realPass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    logSuccess = true
                }
            }
        return logSuccess
    }

    //TODO: Arreglar la insercion de activities en estas funciones
    private fun searchByDni(){
        if (currentUser.mail != null && currentUser.passwd != null) {
            db.collection("users").whereEqualTo("DNI", currentUser.dni)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
//                        errorLog.showError()
                    } else {
                        for (document in documents) {
                            loginWithEmail(document.getString("email").toString())
                        }
                    }

                }
        } else {
//            errorLog.showError()
        }
    }


}