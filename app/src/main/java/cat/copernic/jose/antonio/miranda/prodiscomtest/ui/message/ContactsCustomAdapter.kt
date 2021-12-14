package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.Users
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate.CustomAdapter
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate.ValItemsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ContactsCustomAdapter(private val mList: List<ContactsViewModel>) : RecyclerView.Adapter<ContactsCustomAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val fromUid = firebaseUser!!.email
    private val rootRef = FirebaseFirestore.getInstance()
    private val uidRef =
        fromUid?.let { rootRef.collection("users").document(it) }
    private var firebaseAuth: FirebaseAuth? = null
    private val listOfToUsers = ArrayList<Users>()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_contacts_recycler, parent, false)
        //ViewHolder(view).listeners()

        GlobalScope.launch {
            getContacts()
        }
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        //holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.txtNom.text = ItemsViewModel.userName
        holder.itemView.setOnClickListener {
            //it.findNavController().navigate(R.id.chat)
            it.findNavController().navigate(ContactsDirections.actionContactsToChat(listOfToUsers.toTypedArray()))

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

    private suspend fun getContacts() {
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
                                val listOfToUsers = ArrayList<Users>()
                                val listOfRooms = ArrayList<String>()
                                for (d in t.result!!) {
                                    val toUser = d.toObject(Users::class.java)
                                    listOfToUsers.add(toUser)
                                    listOfRooms.add(d.id)
                                }
                            }
                        }
                    }
                }
            }?.await()
        }
    }

}