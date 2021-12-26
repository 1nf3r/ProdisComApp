package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CustomAdapter(private val mList: List<ValItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_val_user_recycler, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.txtNom.text = ItemsViewModel.nom
        holder.txtCorreu.text = ItemsViewModel.correu
        holder.txtDni.text = ItemsViewModel.dni
        holder.txtFecha.text = ItemsViewModel.fecha
        holder.btnValidar.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                updateValidated(holder.txtCorreu.text as String)

                holder.cardView.visibility = View.GONE
                holder.itemView.visibility = View.GONE
                val params = holder.itemView.layoutParams
                params.height = 0
                params.width = 0
                holder.itemView.layoutParams = params
                holder.itemView.visibility = View.VISIBLE
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txtNom: TextView = itemView.findViewById(R.id.txtValUserDisplayNom)
        val txtCorreu: TextView = itemView.findViewById(R.id.txtValUserDisplayCorreu)
        val txtDni: TextView = itemView.findViewById(R.id.txtValUserDisplayDni)
        val txtFecha: TextView = itemView.findViewById(R.id.txtValUserDisplayFecha)
        val btnValidar: Button = itemView.findViewById(R.id.btnValidate)

        val cardView: CardView = itemView.findViewById(R.id.valCard)
    }

    private suspend fun updateValidated(email : String){
        val update = db.collection("users").document(email)
        update.update("zValidado",true).await()
    }
}