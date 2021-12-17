package cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.UserFormData
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class RegisterViewModel : ViewModel() {

    private val registerModel = MutableLiveData<UserFormData>()


    private val db = FirebaseFirestore.getInstance()


    fun saveDB(nombre: String, dni: String, email: String) {
        val users = db.collection("users")
        val userInfo = hashMapOf(
            "Nombre" to nombre,
            "DNI" to dni.uppercase(),
            "email" to email,
            "Fecha" to SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date()),
            //"Telefono" to telefono,
            "zValidado" to false,
            "zBloqueado" to false,
            "zEliminado" to false,
            "zAdmin" to false
        )
        users.document(email).set(userInfo)
    }
}