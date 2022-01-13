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
    lateinit var storageRef: StorageReference
    private var currentUser = Firebase.auth.currentUser
    private val currentMail = Firebase.auth.currentUser?.email
    private var currentUserMail: String = currentMail!!
    private var filename = "perfilImg-" + Firebase.auth.currentUser?.email
    private var dataLocal: Uri? = null
    private lateinit var getUserInfo: DocumentReference
    private var name = ""
    private var mail = ""
    private var telf = ""
    private var birth = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        binding.btnReturnPerfil.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.menu_principal,
                null
            )
        )
        2
        //Recuperem les dades del usuari i les mostrem per pantalla
        viewModel.getInfo(activity)
        displayInfo()

        //Carreguem l'imatge desde el firebase, si no hi ha cap image es carregara una per defecte.
        storageRef = FirebaseStorage.getInstance().getReference()
        storageRef.child("user_images/$filename").downloadUrl
            .addOnSuccessListener { url ->
                Glide.with(this)
                    .load(url.toString())
                    .circleCrop()
                    .into(binding.imgDisplayFoto)

            }.addOnFailureListener {
                binding.imgDisplayFoto.setImageResource(R.drawable.no_user)
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
            name = viewModel.nombre.value.toString()
            binding.txtDisplayCorreo.setText(viewModel.correo.value)
            mail = viewModel.correo.value.toString()
            binding.txtDisplayTelefono.setText(viewModel.telefono.value)
            telf = viewModel.telefono.value.toString()
            binding.txtDisplayNacimiento.setText(viewModel.nacimiento.value)
            birth = viewModel.nacimiento.value.toString()
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
        var changedName = false
        var changedMail = false
        var changedTelf = false
        var changedBirth = false
        var changedImage: Boolean
        if (dataLocal != null) {
            changedImage = true
            storageRef = FirebaseStorage.getInstance().getReference("user_images/$filename")
            storageRef.putFile(dataLocal!!)
                .addOnSuccessListener {
                    binding.imgDisplayFoto.setImageURI(dataLocal)
                }.addOnFailureListener {
                }
        } else {
            changedImage = false
        }
        if (name != binding.txtDisplayNombre.text.toString()) {
            changedName = true
        }
        if (mail != binding.txtDisplayCorreo.text.toString()) {
            changedMail = true
        }
        if (telf != binding.txtDisplayTelefono.text.toString()) {
            changedTelf = true
        }
        if (birth != binding.txtDisplayNacimiento.text.toString()) {
            changedBirth = true
        }
        if (changedMail) {
            if (changedName || changedTelf || changedBirth || changedImage) {
                currentUser?.updateEmail(viewModel.correo.value.toString())
                changeFields()
                Toast.makeText(requireContext(), R.string.changes_applied, Toast.LENGTH_LONG).show()
            } else {
                currentUser?.updateEmail(viewModel.correo.value.toString())
                Toast.makeText(requireContext(), R.string.mail_changed, Toast.LENGTH_LONG).show()
            }
        } else if (changedName || changedTelf || changedBirth || changedImage) {
            changeFields()
            Toast.makeText(requireContext(), R.string.changes_applied, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), R.string.any_change, Toast.LENGTH_LONG).show()
        }
    }

    private fun changeFields() {
        getUserInfo = FirebaseFirestore.getInstance().collection("users")
            .document(currentUserMail)
        getUserInfo.update("nombre",viewModel.nombre.value.toString(),
            "Telefono", viewModel.telefono.value.toString(),
            "Fecha", viewModel.nacimiento.value.toString()
        )
    }

}