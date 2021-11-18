package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.logged.perfil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentPerfilBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel.PerfilViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

private lateinit var viewModel: PerfilViewModel
public class Perfil : Fragment() {
    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        binding.btnToEditPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.editPerfil, null))
        binding.btnReturnPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))
        viewModel.getInfo()
        displayInfo()

        return binding.root
    }

    @DelicateCoroutinesApi
    private fun displayInfo(){
        GlobalScope.async(Dispatchers.Main) {
            delay(500)
            binding.txtDisplayNombre.setText(viewModel.nombre.value)
            binding.txtDisplayCorreo.setText(viewModel.correo.value)
            //binding.txtDisplayTelefono.setText(viewModel.telefono.value.toString())
            binding.txtDisplayNacimiento.setText(viewModel.nacimiento.value)


        }
    }

    private fun getInfo() = runBlocking<Unit>{
        //Log.d("TAG",viewModel.dni.value!!)
        val currentUser = auth.currentUser?.email
        val getUserInfo = db.collection("users").whereEqualTo("email",currentUser)
        getUserInfo.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    //Log.d("TAG", "Nombre: ${document.id} => ${document.data}")
                    binding.txtDisplayNombre.setText(document.getField<String>("Nombre"))
                    binding.txtDisplayCorreo.setText(document.getField<String>("email"))
                    binding.txtDisplayNacimiento.setText(document.getField<String>("informacion"))
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
        //delay(500)
    }


}