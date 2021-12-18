package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.logged.perfil

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentPerfilBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel.PerfilViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*


private lateinit var viewModel: PerfilViewModel

class Perfil : Fragment() {
    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth
    lateinit var storageRef: StorageReference
    private var filename = "perfilImg-" + Firebase.auth.currentUser?.email
    private var dataLocal: Uri? = null
    private lateinit var getUserInfo: DocumentReference
    private var found = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        binding.btnReturnPerfil.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                cat.copernic.jose.antonio.miranda.prodiscomtest.R.id.menu_principal,
                null
            )
        )

        viewModel.getInfo(activity)

        displayInfo()

        storageRef = FirebaseStorage.getInstance().getReference()
        storageRef.child("user_images/$filename").downloadUrl
            .addOnSuccessListener { url ->
                Glide.with(this)
                    .load(url.toString())
                    .into(binding.imgDisplayFoto)

            }.addOnFailureListener {
                binding.imgDisplayFoto.setImageResource(cat.copernic.jose.antonio.miranda.prodiscomtest.R.drawable.no_user)
            }

        binding.imgDisplayFoto.setOnClickListener {
            obrirGaleria()
        }

        binding.btnGuardarPerf.setOnClickListener {
            guardarInfo()
        }


        return binding.root
    }

    @DelicateCoroutinesApi
    private fun displayInfo() {
        GlobalScope.async(Dispatchers.Main) {
            delay(500)
            binding.txtDisplayNombre.setText(viewModel.nombre.value)
            binding.txtDisplayCorreo.setText(viewModel.correo.value)
            binding.txtDisplayNacimiento.setText(viewModel.nacimiento.value)


        }
    }


    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            dataLocal = result.data?.data!!
            if (dataLocal != null) {
                binding.imgDisplayFoto.setImageURI(dataLocal)
            }

        }
    }

    private fun obrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    private fun guardarInfo() {
        if (dataLocal != null) {
            storageRef = FirebaseStorage.getInstance().getReference("user_images/$filename")
            storageRef.putFile(dataLocal!!)
                .addOnSuccessListener {
                    binding.imgDisplayFoto.setImageURI(dataLocal)
                }.addOnFailureListener {
                }
        } else {
            Toast.makeText(requireContext(), R.string.any_change, Toast.LENGTH_LONG).show()
        }
        if (true){
            /*
        val users = db.collection("users")
        val userInfo = hashMapOf(
            "Nombre" to nombre,
            "Apellido" to apellido,
            "DNI" to dni.uppercase(),
            "email" to email,
            "Fecha" to SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date()),
            "Telefono" to telefono,
            "zValidado" to false,
            "zBloqueado" to false,
            "zEliminado" to false,
            "zAdmin" to false
        )
        users.document(email).set(userInfo)*/
        } 

    }


}