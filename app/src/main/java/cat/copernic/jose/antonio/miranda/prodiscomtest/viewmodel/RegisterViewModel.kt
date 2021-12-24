package cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class RegisterViewModel : ViewModel() {

    private var map :Map<String, Boolean> = mapOf("room1" to true)
    private val db = FirebaseFirestore.getInstance()

    fun saveDB(nombre: String, dni: String, email: String, apellido: String, telefono: String) {

        val users = db.collection("users")
        val userInfo = hashMapOf(
            "Nombre" to nombre,
            "Apellido" to apellido,
            "DNI" to dni.uppercase(),
            "email" to email,
            "Fecha" to SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date()),
            "Telefono" to telefono,
            "rooms" to map,
            "zValidado" to false,
            "zBloqueado" to false,
            "zEliminado" to false,
            "zAdmin" to false
        )
        users.document(email).set(userInfo)
    }
}