package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentDelUserBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class delUser : Fragment() {
    private var _binding: FragmentDelUserBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = Firebase.auth.currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDelUserBinding.inflate(inflater, container, false)

        binding.btnReturnDelUser.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.usuarios,
                null
            )
        )
        binding.btnDelUserToHome.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.menu_principal,
                null
            )
        )

        getInfo()

        return binding.root
    }

    private fun getInfo() {
        val getUserInfo = db.collection("users").document(currentUser?.email!!)
        getUserInfo.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    printInfo(
                        document.get("email") as String,
                        document.get("Nombre") as String
                    )
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }

    }

    private fun printInfo(mail: String, nom: String) {
        binding.txResultMail.text = mail
        binding.txResultNom.text = nom
    }
}