package cat.copernic.jose.antonio.miranda.prodiscomtest.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

//Estructura d'un missatge per a firebase
class Message(
    val messageText: String = "",
    val fromUid: String = "",
    @ServerTimestamp
    val sentAt: Date? = null
)