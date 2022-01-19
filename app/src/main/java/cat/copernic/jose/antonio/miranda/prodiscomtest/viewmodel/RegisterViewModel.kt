package cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class RegisterViewModel : ViewModel() {

    private var random = (10000000..100000000).random()
    private var map: Map<String, Boolean> = mapOf(random.toString() to true)
    private val db = FirebaseFirestore.getInstance()

  suspend fun saveDB(nombre: String, dni: String, email: String, apellido: String, telefono: String) {

        val users = db.collection("users")
        val contactes = db.collection("contactes")
        val rooms = db.collection("rooms")
        val userInfo = hashMapOf(
            "nombre" to nombre,
            "Apellido" to apellido,
            "DNI" to dni.uppercase(),
            "email" to email,
            "Fecha" to SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date()),
            "Telefono" to telefono,
            "rooms" to map,
            "zValidado" to true, //Sistema de validacion desactivado
            "zBloqueado" to false,
            "zEliminado" to false,
            "zAdmin" to false
        )
        val roomInfo = hashMapOf(
            "rooms" to map
        )

        val contactInfo = hashMapOf(
            "email" to email,
            "nombre" to nombre,
            "rooms" to map
        )

        users.document(email).set(userInfo).await()
        //contactes.document(email).collection("userContacts").document(email).set(contactInfo)
        rooms.document(email).collection("userRooms").document("room1").set(roomInfo).await()
    }
}