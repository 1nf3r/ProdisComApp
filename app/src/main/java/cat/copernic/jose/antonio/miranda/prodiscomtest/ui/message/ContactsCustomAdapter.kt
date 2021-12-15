package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

class ContactsCustomAdapter(private val mList: List<ContactsViewModel>) : RecyclerView.Adapter<ContactsCustomAdapter.ViewHolder>() {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private var firebaseAuth: FirebaseAuth? = null
    private var listOfToUsers = ArrayList<Users>()
    private var listOfToUserNames = ArrayList<String?>()
    private var listOfRooms = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_contacts_recycler, parent, false)
            getContacts()
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        holder.txtNom.text = itemsViewModel.Nombre
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(ContactsDirections
                .actionContactsToChat(listOfToUsers[position]))
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txtNom: TextView = itemView.findViewById(R.id.textView3)
    }

    private fun getContacts() {
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseUser != null) {
            val fromUid = firebaseUser.email
            val fromUidString = fromUid.toString()
            val rootRef = FirebaseFirestore.getInstance()
            val uidRef = rootRef.collection("users").document(fromUidString)
            uidRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        val fromUser = document.toObject(Users::class.java) //TENGO QUE PASAR COMO ARGUMENTO TAMBIEN
                        Log.i("ahorasi", fromUser.toString())
                        val userContactsRef = fromUid?.let {
                            rootRef.collection("contactes").document(it)
                                .collection("userContacts")
                        }
                        userContactsRef?.get()?.addOnCompleteListener { t ->
                            if (t.isSuccessful) {
                                var listOfToUsers2 = ArrayList<Users>()
                                for (d in t.result!!) {
                                    val toUser = d.toObject(Users::class.java)
                                    listOfToUserNames.add(toUser.Nombre)
                                    listOfToUsers2.add(toUser)
                                    listOfRooms.add(d.id)
                                }
//                                Log.i( "pasar1", listOfToUsers2.toString())
                                setListOfToUsers(listOfToUsers2)
                            }
                        }
                    }
                }
            }
        }
    }

    fun setListOfToUsers(user: ArrayList<Users>){
        this.listOfToUsers = user
    }
}