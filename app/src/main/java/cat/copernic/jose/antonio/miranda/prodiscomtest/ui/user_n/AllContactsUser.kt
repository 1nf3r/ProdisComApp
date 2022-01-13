package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user_n

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentAllContactsBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message.AllContactsCustomAdapter
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message.AllContactsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AllContactsUser : Fragment() {
    private var _binding: FragmentAllContactsBinding? = null
    private val binding get() = _binding!!
    private var firebaseAuth: FirebaseAuth? = null
    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val rootRef = FirebaseFirestore.getInstance()
    private lateinit var adapter: AllContactsUserCustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllContactsBinding.inflate(inflater, container, false)
        binding.btnReturnMensajes.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.chatsUser,
                null
            )
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val data = ArrayList<AllContactsViewModel>()
        getContacts(data)
        adapter = AllContactsUserCustomAdapter(data)
        binding.recyclerView.adapter = adapter

        authStateListener = FirebaseAuth.AuthStateListener { }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getContacts(data: ArrayList<AllContactsViewModel>) {
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseUser != null) {
            rootRef.collection("users").get().addOnSuccessListener { documents ->
                for (document in documents) {
                    data.add(AllContactsViewModel(document.get("nombre") as String))
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}