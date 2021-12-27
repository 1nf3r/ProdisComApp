package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.grup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentCrearGrupBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate.CustomAdapter
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate.ValItemsViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class CrearGrup : Fragment() {
    private lateinit var binding: FragmentCrearGrupBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            binding = FragmentCrearGrupBinding.inflate(inflater, container, false)

            binding.btnReturnCrearGrup.setOnClickListener(
                Navigation.createNavigateOnClickListener(
                    R.id.menu_principal,
                    null
                )
            )

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<CrearGrupItemViewModel>()
        CoroutineScope(Dispatchers.Main).launch {
            getInfo(data)

            // This will pass the ArrayList to our Adapter
            val adapter = CrearGrupCustomAdapter(data)
            // Setting the Adapter with the recyclerview
            binding.recyclerView.adapter = adapter

            binding.btnCrearGrup.setOnClickListener(){
                CoroutineScope(Dispatchers.Main).launch {
                    crearGrup(adapter)

                }
            }
        }

            return binding.root
    }

    suspend private fun crearGrup(adapter: CrearGrupCustomAdapter) {
        val nameGroup = binding.etxtNomGrup.text.toString().lowercase()
        if(checkGroups(nameGroup)) {
            val groups = db.collection("grups")
            val users = hashMapOf(
                "Nombre" to nameGroup,
                "Descripción" to binding.etxtDescripicioGrup.text.toString(),
                "Email" to adapter.users
            )
            groups.document(nameGroup).set(users).addOnSuccessListener {
                Toast.makeText(activity, "Grup creat correctament", Toast.LENGTH_LONG).show()
            }
        }else {
            Toast.makeText(activity, "El nom del grup ja existeix", Toast.LENGTH_LONG).show()
        }
    }

    suspend fun getInfo(data:ArrayList<CrearGrupItemViewModel>){
        val getUserInfo = db.collection("users")
            .whereEqualTo("zValidado", true)
            .whereEqualTo("zAdmin",false)
        getUserInfo.get()
            .addOnSuccessListener { documents ->
                var contador = 0
                for (document in documents) {
                        data.add(
                            CrearGrupItemViewModel(
                                document.get("Nombre") as String,
                                document.get("email") as String,
                                document.get("DNI") as String
                            )
                        )
                        contador++
                }
                if(contador == 0)
                    Toast.makeText(activity, "Error al cercar usuaris", Toast.LENGTH_LONG).show()
            }.await()
    }

    private suspend fun checkGroups(name: String): Boolean {
        return db.collection("grups").whereEqualTo("Nombre", name)
            .get()
            .await().isEmpty
    }
}