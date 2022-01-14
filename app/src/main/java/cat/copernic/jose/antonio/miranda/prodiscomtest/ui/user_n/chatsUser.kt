package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user_n

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.Users
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentChatsBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentChatsUserBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.LoginActivity
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message.ChatsCustomAdapter
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message.ContactsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserChats : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private var firebaseAuth: FirebaseAuth? = null
    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val fromUid = firebaseUser!!.email
    private val rootRef = FirebaseFirestore.getInstance()
    private val uidRef =
        fromUid?.let { rootRef.collection("users").document(it) }
    private lateinit var adapter: ChatsUserCustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        binding.btnReturnMensajes.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            //Tornem a la pantalla login i acabem la main activity
            activity?.finish()
            activity?.startActivity(Intent(this.activity, LoginActivity::class.java))
        }

        binding.btnAjustes.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.perfilUser,
                null
            )
        )

        binding.addContacts.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.allContacts2, null)
        )


        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val data = ArrayList<ContactsViewModel>()
        getContacts(data)
        adapter = ChatsUserCustomAdapter(data)
        binding.recyclerView.adapter = adapter

        authStateListener = FirebaseAuth.AuthStateListener { }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getContacts(data: ArrayList<ContactsViewModel>) {
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseUser != null) {
            uidRef?.get()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
//                        val fromUser = document.toObject(User::class.java)
                        val userContactsRef = fromUid?.let {
                            rootRef.collection("contactes").document(it)
                                .collection("userContacts")
                        }
                        userContactsRef?.get()?.addOnCompleteListener { t ->
                            if (t.isSuccessful) {
                                for (d in t.result!!) {
                                    val toUser = d.toObject(Users::class.java)
                                    data.add(ContactsViewModel(toUser.nombre!!))
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}