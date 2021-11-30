package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.logged.perfil

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentPerfilBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel.PerfilViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

private lateinit var viewModel: PerfilViewModel

class Perfil : Fragment() {
    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth
    lateinit var storageRef: StorageReference
    private var filename = "perfilImg-"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        binding.btnToEditPerfil.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.editPerfil,
                null
            )
        )
        binding.btnReturnPerfil.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.menu_principal,
                null
            )
        )


        viewModel.getInfo()
        displayInfo()


        storageRef = FirebaseStorage.getInstance().getReference("user_images/$filename")
        Log.i("HELLO", storageRef.child("user_images/$filename").toString())
        storageRef.child("user_images/$filename").downloadUrl
                .addOnSuccessListener { url ->
                Glide.with(this)
                    .load(url.toString())
                    .into(binding.imgDisplayFoto)

            }.addOnFailureListener {
                binding.imgDisplayFoto.setImageResource(R.drawable.no_user)
            }




        binding.btnGaleria.setOnClickListener {
            obrirGaleria()
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
            val dataLocal = result.data?.data
            if (dataLocal != null) {
                storageRef.putFile(dataLocal)
                    .addOnSuccessListener {
                        binding.imgDisplayFoto.setImageURI(dataLocal)
                    }.addOnFailureListener {
                    }
            }

        }
    }

    private fun obrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    private fun imgNameFormater() {

    }
}