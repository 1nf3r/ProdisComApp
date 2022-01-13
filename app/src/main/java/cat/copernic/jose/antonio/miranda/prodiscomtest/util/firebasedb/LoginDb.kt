package cat.copernic.jose.antonio.miranda.prodiscomtest.util.firebasedb


import android.util.Log
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.UserFormData
import cat.copernic.jose.antonio.miranda.prodiscomtest.util.ErrorLogs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginDb {

    private val currentUser: UserFormData = UserFormData()
    private val db = FirebaseFirestore.getInstance()
    private var mail: String = ""
    private var showError = false


    fun loginWithEmail(email: String, passwd: String): Boolean {
        var logSuccess = false
        var realPass = "Prodis"
        realPass += passwd
        Firebase.auth.signInWithEmailAndPassword(email, realPass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    logSuccess = true
                }
            }
        return logSuccess
    }

    fun searchByDni(dni: String, passwd: String): Boolean {

        if (dni.isNotEmpty() && passwd.isNotEmpty()) {
            Log.i("login", "entra")
            db.collection("users").whereEqualTo("DNI", dni)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Log.i("login", "Deberia entrar")
                        showError = true //FALLA AQUI
                    } else {
                        for (document in documents) {
//                            mail = document.getString("email").toString() //FALLA AQUI
                            Log.i("login", "yas")
                            loginWithEmail(
                                document.getString("email").toString(),
                                passwd
                            )
                        }
                    }
                }
        } else {

            showError = true
        }
        return showError
    }

    fun getMail(): String {
        return mail
    }
}