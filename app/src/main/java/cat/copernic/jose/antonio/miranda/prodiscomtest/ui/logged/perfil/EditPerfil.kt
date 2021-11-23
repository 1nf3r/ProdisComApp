package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.logged.perfil

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentEditPerfilBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel.PerfilViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*


private lateinit var viewModel: PerfilViewModel
class editPerfil : Fragment() {
    private var _binding: FragmentEditPerfilBinding? = null
    private val binding get() = _binding!!

/*    private val per = Perfil()
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[PerfilViewModel::class.java]

        _binding = FragmentEditPerfilBinding.inflate(inflater, container, false)
        binding.btnGuardarPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.perfil, null))

        binding.btnReturnEditPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.perfil, null))
        binding.btnEditePerfilToHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))

        viewModel.getInfo()
        displayInfo()
        //viewModel.setInfo("","",0,"")
        val media = "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_960_720.jpg"
        if (media !== null) {
            Glide.with(this)
                .load(media)
                .into(binding.iVPerfilFoto)
        } else {
            binding.iVPerfilFoto.setImageResource(R.drawable.adduser_edittext)
        }

        return binding.root
    }

    private fun displayInfo(){
        GlobalScope.async(Dispatchers.Main) {
            delay(500)
            binding.etxtNom.setText(viewModel.nombre.value)
            binding.etxtCorreu.setText(viewModel.correo.value)
            //binding.txtDisplayTelefono.setText(viewModel.telefono.value.toString())
            binding.etxtNaixement.setText(viewModel.nacimiento.value)
        }
    }



    /*private fun getInfo() = runBlocking<Unit>{
        val currentUser = auth.currentUser?.email
        val getUserInfo = db.collection("users").whereEqualTo("email",currentUser)
        getUserInfo.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    //Log.d("TAG", "Email: ${document.id} => ${document.data}")
                    binding.etxtNom.setText(document.getField<String>("Nombre"))
                    binding.etxtCorreu.setText(document.getField<String>("email"))
                    binding.etxtNaixement.setText(document.getField<String>("informacion"))

                    /*viewModel.setInfo(document.getField<String>("Nombre")!!,
                        document.getField<String>("email")!!,
                        document.getField<String>("informacion")!!)*/

                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
        delay(500)
    }*/
}