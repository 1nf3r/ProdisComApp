package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.grup

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate.CustomAdapter
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate.ValItemsViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.annotation.concurrent.Immutable

class CrearGrupCustomAdapter(private val mList: List<CrearGrupItemViewModel>) : RecyclerView.Adapter<CrearGrupCustomAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    val users = ArrayList<String>()
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_crear_grup_recycler, parent, false)
        //ViewHolder(view).listeners()

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        //holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.txtCorreu.text = ItemsViewModel.correu
        holder.txtCheckBox.text = "Nom: "+ItemsViewModel.nom+"  "+"Dni: "+ ItemsViewModel.dni
        holder.txtCheckBox.setOnClickListener(){
            if(holder.txtCheckBox.isChecked){
                println(holder.txtCorreu.text.toString())
                users.add(holder.txtCorreu.text as String)
            }else if(!holder.txtCheckBox.isChecked){
                println("NO "+ holder.txtCorreu.text.toString())
                users.remove(holder.txtCorreu.text as String)
            }
        }
        /*holder.btnCrearGrup.setOnClickListener(){
            for(i in users){
                println(i)
            }
        }*/


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txtCorreu: TextView = itemView.findViewById(R.id.txtCrearGrupDisplayCorreu)
        val txtCheckBox: CheckBox = itemView.findViewById(R.id.select_user)

    }
}