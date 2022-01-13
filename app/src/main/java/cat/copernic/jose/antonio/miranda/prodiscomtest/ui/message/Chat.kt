package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.btnReturnMensajes.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.contacts
            )
        )


        rootRef = FirebaseFirestore.getInstance()
        //Agafem els arguments del fragment Contacts
        val fromUser = ChatArgs.fromBundle(requireArguments()).localUser
        val fromUidNull = fromUser?.email
        fromUid = fromUidNull!!
        val toUser = ChatArgs.fromBundle(requireArguments()).main
        var fromRooms = fromUser.rooms
        val toUidNull = toUser.email
        val toUid: String = toUidNull!!
        var toRooms = toUser.rooms
        var roomId = ChatArgs.fromBundle(requireArguments()).roomId

        //Establim el id de la room que esta en firebase al usuari actual
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

            //Actualitzem la informació de les col·leccions de users contactes i rooms amb els uid dels usuaris corresponents
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

            //Generació del missatge el room seleccionat
            val messageText = binding.txWriteMessage.text.toString()
            val message = Message(messageText, fromUid)
            rootRef!!.collection("missatges").document(roomId).collection("roomMessages")
                .add(message)
            binding.txWriteMessage.text.clear()
        }

        //Obtenim els missatges de la base de dades i els ordenem per l'hora que han sigut enviats
        val query = rootRef!!.collection("missatges").document(roomId).collection("roomMessages")
            .orderBy("sentAt", Query.Direction.ASCENDING)
        val options =
            FirestoreRecyclerOptions.Builder<Message>().setQuery(query, Message::class.java).build()

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = MessageAdapter(options)
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    //Carreguem el missatge en el textView
    inner class MessageViewHolder internal constructor(private val view: View) :
        RecyclerView.ViewHolder(view) {
        internal fun setMessage(message: Message) {
            val textView: TextView = view.findViewById(R.id.text_view)
            textView.text = message.messageText
        }
    }

    //Definim en que costat es pintara el missatge depenent de l'usuari que l'ha escrit
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
