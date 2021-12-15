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

class ContactsCustomAdapter(private val mList: List<ContactsViewModel>) : RecyclerView.Adapter<ContactsCustomAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val fromUid = firebaseUser!!.email
    private val rootRef = FirebaseFirestore.getInstance()
    private val uidRef =
        fromUid?.let { rootRef.collection("users").document(it) }
    private var firebaseAuth: FirebaseAuth? = null
    private var listOfToUsers = ArrayList<Users>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_contacts_recycler, parent, false)
            getContacts()

        Log.i("pasar2", listOfToUsers.size.toString())
        Log.i("pasar2", listOfToUsers.toString())
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        holder.txtNom.text = itemsViewModel.userName
        holder.itemView.setOnClickListener {
            //it.findNavController().navigate(R.id.chat)
            it.findNavController().navigate(ContactsDirections
                .actionContactsToChat(listOfToUsers.toTypedArray()))
            Log.i("pasar5", listOfToUsers.toString())
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
            uidRef?.get()?.addOnCompleteListener { task ->
                Log.i("Mensaje", "Entra2")
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        val userContactsRef = fromUid?.let {
                            rootRef.collection("contactes").document(it)
                                .collection("userContacts")
                        }
                        userContactsRef?.get()?.addOnCompleteListener { t ->
                            if (t.isSuccessful) {
                                val listOfRooms = ArrayList<String>()
                                var listOfToUsers2 = ArrayList<Users>()
                                for (d in t.result!!) {
                                    val toUser = d.toObject(Users::class.java)
                                    listOfToUsers2.add(toUser)
                                    listOfRooms.add(d.id)
                                }
                                Log.i( "pasar1", listOfToUsers2.toString())
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