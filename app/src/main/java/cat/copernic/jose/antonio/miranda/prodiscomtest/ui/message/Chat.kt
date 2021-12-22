package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.Message
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.Users
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentChatBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

class Chat : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private var rootRef: FirebaseFirestore? = null
    private var fromUid: String = ""
    private var adapter: MessageAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatBinding.inflate(inflater, container, false)

        rootRef = FirebaseFirestore.getInstance()
        val fromUser = ChatArgs.fromBundle(requireArguments()).localUser
        val fromUidNull = fromUser?.email
        fromUid = fromUidNull!!
        val toUser = ChatArgs.fromBundle(requireArguments()).main
        var fromRooms = fromUser.rooms
        val toUidNull = toUser.email
        val toUid: String = toUidNull!!
        var toRooms = toUser.rooms
        var roomId = ChatArgs.fromBundle(requireArguments()).roomId

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

        val query = rootRef!!.collection("missatges").document(roomId).collection("roomMessages")
            .orderBy("sentAt", Query.Direction.ASCENDING)
        val options =
            FirestoreRecyclerOptions.Builder<Message>().setQuery(query, Message::class.java).build()
        Log.i("mensajetest" , options.toString())
        adapter = MessageAdapter(options)
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    inner class MessageViewHolder internal constructor(private val view: View) :
        RecyclerView.ViewHolder(view) {
        internal fun setMessage(message: Message) {
            val textView: TextView = view.findViewById<TextView>(R.id.text_view)
            textView.text = message.messageText
        }
    }

    inner class MessageAdapter internal constructor(options: FirestoreRecyclerOptions<Message>) :
        FirestoreRecyclerAdapter<Message, MessageViewHolder>(options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
          return if (viewType == R.layout.fragment_chat_remoto) {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_chat_remoto, parent, false)
                MessageViewHolder(view)
            } else {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_chat_local, parent, false)
                MessageViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
            holder.setMessage(model)
        }

        override fun getItemViewType(position: Int): Int {
            return if (fromUid != getItem(position).fromUid) {
                R.layout.fragment_chat_remoto
            } else {
                R.layout.fragment_chat_local
            }
        }

        override fun onDataChanged() {
            binding.recyclerView.layoutManager?.scrollToPosition(itemCount - 1)

        }
    }

    override fun onStart() {
        super.onStart()
        if (adapter != null) {
            adapter!!.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) {
            adapter!!.stopListening()
        }
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
//TODO Idiomas
//TODO Modo Horizontal
//TODO Recuperar contrasenya