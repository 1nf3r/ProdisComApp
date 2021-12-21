package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.Message
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.Users
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentChatBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class Chat : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private var rootRef: FirebaseFirestore? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatBinding.inflate(inflater, container, false)

        rootRef = FirebaseFirestore.getInstance()
        val fromUser = ChatArgs.fromBundle(requireArguments()).localUser
        val fromUidNull = fromUser?.email
        val fromUid: String = fromUidNull!!
        val toUser = ChatArgs.fromBundle(requireArguments()).main
        var fromRooms = fromUser?.rooms
        val toUidNull = toUser.email
        val toUid: String = toUidNull!!
        var toRooms = toUser.rooms
        var roomId = ChatArgs.fromBundle(requireArguments()).roomId

//        Log.i("HOLA", fromUser.toString())
//        Log.i("HOLA1", fromUid)
//        Log.i("HOLA2", toUser.toString())
//        Log.i("HOLA3", toUid)

        if (roomId == "noRoomId") {
            roomId = rootRef!!.collection("messages").document().id
            if (fromRooms != null) {
                for ((key, _) in fromRooms) {
                    if (toRooms != null) {
                        if (toRooms.containsKey(key)) {
                            roomId = key
                        }
                    }
                }
            }
        }
        binding.btnSentMessage.setOnClickListener {
            if (fromRooms == null) {
                fromRooms = mutableMapOf()
            }
            fromRooms!![roomId] = true
            fromUser.rooms = fromRooms
            rootRef!!.collection("users").document(fromUid).set(fromUser, SetOptions.merge())
            rootRef!!.collection("contactes").document(toUid).collection("userContacts")
                .document(fromUid).set(fromUser, SetOptions.merge())
            rootRef!!.collection("rooms").document(toUid).collection("userRooms").document(roomId)
                .set(fromUser, SetOptions.merge())

            if (toRooms == null) {
                toRooms = mutableMapOf()
            }
            toRooms!![roomId] = true
            toUser.rooms = toRooms
            rootRef!!.collection("users").document(toUid).set(toUser, SetOptions.merge())
            rootRef!!.collection("contactes").document(fromUid).collection("userContacts")
                .document(toUid).set(toUser, SetOptions.merge())
            rootRef!!.collection("rooms").document(fromUid).collection("userRooms").document(roomId)
                .set(toUser, SetOptions.merge())


            val messageText = binding.txWriteMessage.text.toString()
            val message = Message(messageText, fromUid)
            rootRef!!.collection("missatges").document(roomId).collection("roomMessages")
                .add(message)
            binding.txWriteMessage.text.clear()
        }


        return binding.root
    }
}

//TODO Grupos : Chat
//TODO Hacer Notificaciones
//TODO Realizar Observer
//TODO Mejorar MVVM
//TODO Arreglar Perfil.kt
//TODO Mejorar los temas de la app
//TODO Mejorar los contactos de forma automatica
//TODO Mejorar informacion del usuario
//TODO Hacer que el login no se quite despues de cerrar la app
//TODO Que el user normal vaya a contactos directamente

