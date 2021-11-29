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

        val media = "https://firebasestorage.googleapis.com/v0/b/prodiscom-ij.appspot.com/" +
                    "o/pikachu.jpeg?alt=media&token=44aef3bc-05e2-4e23-9857-ba55da5805e6"

        Glide.with(this)
            .load(media)
            .into(binding.imgDisplayFoto)


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
            //binding.txtDisplayTelefono.setText(viewModel.telefono.value.toString())
            binding.txtDisplayNacimiento.setText(viewModel.nacimiento.value)


        }
    }

    private fun getInfo() = runBlocking<Unit> {
        //Log.d("TAG",viewModel.dni.value!!)
        val currentUser = auth.currentUser?.email
        val getUserInfo = db.collection("users").whereEqualTo("email", currentUser)
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

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            Log.i("HOLA", "HOLA1")
            val dataLocal = result.data?.data
            //setImageUri només funciona per rutes locals, no a internet
            binding.imgDisplayFoto.setImageURI(dataLocal)
            Log.i("HOLA", "HOLA2")
            // Get the data from an ImageView as bytes
            binding.imgDisplayFoto.isDrawingCacheEnabled = true
            binding.imgDisplayFoto.buildDrawingCache()
            Log.i("HOLA", "HOLA3")
            val bitmap = (binding.imgDisplayFoto.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            storageRef = FirebaseStorage.getInstance().reference
            var uploadTask = storageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                Log.i("HOLA", "failed")
            }.addOnSuccessListener { taskSnapshot ->
                Log.i("HOLA", "success")
            }
        }
    }

    private fun obrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }
}