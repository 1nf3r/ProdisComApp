package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AllContactsCustomAdapter(private val mList: List<AllContactsViewModel>) :
    RecyclerView.Adapter<AllContactsCustomAdapter.ViewHolder>() {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    val fromUid = firebaseUser?.email
    val fromUidString = fromUid.toString()
    private var firebaseAuth: FirebaseAuth? = null
    private var listOfToUsers = ArrayList<Users>()
    private val rootRef = FirebaseFirestore.getInstance()
    private var fromUserData: Users? = null
    private var roomId = "noRoomId"
    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_all_contacts_recycler, parent, false)
        getAllContacts()
        getCurrentUser()
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        holder.txtNom.text = itemsViewModel.Nombre
        when (view.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> holder.txtNom.setTextColor(Color.parseColor("#FFFFFF"))
            Configuration.UI_MODE_NIGHT_NO -> holder.txtNom.setTextColor(Color.parseColor("#000000"))
            Configuration.UI_MODE_NIGHT_UNDEFINED -> holder.txtNom.setTextColor(Color.parseColor("#FFFFFF"))
        }
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(AllContactsDirections
                .actionAllContactsToChat(listOfToUsers[position], fromUserData, roomId, holder.txtNom.text.toString() ))
            addContacts(listOfToUsers[position])
        }
    }

    // retorna el numero de items en la llista
    override fun getItemCount(): Int {
        return mList.size
    }

    // Soste les vistes per afegir-la a imatge i text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txtNom: TextView = itemView.findViewById(R.id.textView5)
    }

//Retorna tots els contactes registrats en l'aplicació
    private fun getAllContacts() {
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseUser != null) {
            rootRef.collection("users").get().addOnSuccessListener { documents ->
                for (document in documents) {
                    listOfToUsers.add(
                        Users(
                            document.get("email") as String,
                            document.get("nombre") as String,
                            document.get("rooms") as MutableMap<String, Any>
                        )
                    )
                }
            }
        }
    }

    //Afegeix el contacte seleccionat a la llista de rooms per enviar un missatge
    private fun addContacts(userToAdd: Users) {
        val userContactsAddInfo = hashMapOf(
            "rooms" to userToAdd.rooms,
            "email" to userToAdd.email,
            "nombre" to userToAdd.nombre
        )
        rootRef.collection("contactes").document(fromUidString)
            .collection("userContacts").document(userToAdd.email.toString())
            .set(userContactsAddInfo)
    }

    private fun getCurrentUser() {
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseUser != null) {
            val rootRef = FirebaseFirestore.getInstance()
            val uidRef = rootRef.collection("users").document(fromUidString)
            uidRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        val fromUser = document.toObject(Users::class.java)
                        fromUserData = fromUser
                    }
                }
            }
        }
    }
}
