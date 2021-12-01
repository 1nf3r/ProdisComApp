package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.jose.antonio.miranda.prodiscomtest.R

class CustomAdapter(private val mList: List<ValItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

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

        // sets the image to the imageview from our itemHolder class
        //holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.txtNom.text = ItemsViewModel.nom
        holder.txtCorreu.text = ItemsViewModel.correu
        holder.txtDni.text = ItemsViewModel.dni
        holder.txtFecha.text = ItemsViewModel.fecha
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        //val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val txtNom: TextView = itemView.findViewById(R.id.txtValUserDisplayNom)
        val txtCorreu: TextView = itemView.findViewById(R.id.txtValUserDisplayCorreu)
        val txtDni: TextView = itemView.findViewById(R.id.txtValUserDisplayDni)
        val txtFecha: TextView = itemView.findViewById(R.id.txtValUserDisplayFecha)
    }
}