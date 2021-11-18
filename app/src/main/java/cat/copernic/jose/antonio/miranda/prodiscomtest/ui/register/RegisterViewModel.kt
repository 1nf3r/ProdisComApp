package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

    private val registerModel = MutableLiveData<RegisterFormData>()


    private val db = FirebaseFirestore.getInstance()


    fun saveDB(nombre: String, dni: String, email: String, info: String) {
        val users = db.collection("users")
        val userInfo = hashMapOf(
            "Nombre" to nombre,
            "DNI" to dni,
            "email" to email,
            "informacion" to info
        )
        users.document(email).set(userInfo)
    }
}