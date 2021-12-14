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
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate.CustomAdapter
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate.ValItemsViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ContactsCustomAdapter(private val mList: List<ContactsViewModel>) : RecyclerView.Adapter<ContactsCustomAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_contacts_recycler, parent, false)
        //ViewHolder(view).listeners()

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        //holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.txtNom.text = ItemsViewModel.nom
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.chat)
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

}