package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.logged.perfil

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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

private lateinit var viewModel: PerfilViewModel
class Perfil : Fragment() {
    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        binding.btnToEditPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.editPerfil, null))

        binding.btnReturnPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))

        displayInfo()

        return binding.root
    }

    private fun displayInfo(){
        getInfo()
        //binding.txtDisplayNombre.setText(viewModel.nombre.value)
        //binding.txtDisplayCorreo.setText(viewModel.correo.value)
        //binding.txtDisplayTelefono.setText(viewModel.telefono.value.toString())
        //binding.txtDisplayNacimiento.setText(viewModel.nacimiento.value)

    }

    fun getInfo() = runBlocking<Unit>{
        //Log.d("TAG",viewModel.dni.value!!)
        val getUserInfo = db.collection("users").document("12345678A")
        getUserInfo.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TAG", "Email: ${document.id} => ${document.data}")
                    binding.txtDisplayNombre.setText(document.getField<String>("Nombre"))
                    binding.txtDisplayCorreo.setText(document.getField<String>("email"))
                    binding.txtDisplayNacimiento.setText(document.getField<String>("informacion"))

                    /*viewModel.setInfo(document.getField<String>("Nombre")!!,
                        document.getField<String>("email")!!,
                        document.getField<String>("informacion")!!)*/

                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
        //delay(5000)
    }


}