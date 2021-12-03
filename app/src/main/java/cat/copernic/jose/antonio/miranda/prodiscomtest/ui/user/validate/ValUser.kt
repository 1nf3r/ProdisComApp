package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentValUserBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate.CustomAdapter
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate.ValItemsViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class valUser : Fragment() {
    private var _binding: FragmentValUserBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private var nom = "1";
    private var correu = "1";
    private var dni = "1";
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentValUserBinding.inflate(inflater, container, false)

        binding.btnReturnValUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.usuarios, null))
        binding.btnValUserToHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))

        // getting the recyclerview by its id
        //val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)

        // this creates a vertical layout Manager
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ValItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        CoroutineScope(Dispatchers.Main).launch {
            getInfo(data)
            // This will pass the ArrayList to our Adapter
            val adapter = CustomAdapter(data)
            // Setting the Adapter with the recyclerview
            binding.recyclerView.adapter = adapter
        }

        return binding.root
    }

    suspend fun getInfo(data:ArrayList<ValItemsViewModel>){
        val getUserInfo = db.collection("users").whereEqualTo("zValidado",false)
        getUserInfo.get()
            .addOnSuccessListener { documents ->
                var contador = 0
                for (document in documents) {
                    data.add(ValItemsViewModel(
                        document.get("email") as String,
                        document.get("Nombre") as String,
                        document.get("DNI") as String,
                        document.get("Fecha") as String))
                    contador++
                }
                if(contador == 0)
                    Toast.makeText(activity, "Cap usuari pendent de validació", Toast.LENGTH_LONG).show()
            }.await()


    }
}
