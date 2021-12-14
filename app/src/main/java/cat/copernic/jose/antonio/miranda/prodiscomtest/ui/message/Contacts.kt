package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.Users
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentContactsBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate.ValItemsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Contacts : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private var firebaseAuth: FirebaseAuth? = null
    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val fromUid = firebaseUser!!.email
    private val rootRef = FirebaseFirestore.getInstance()
    private val uidRef =
        fromUid?.let { rootRef.collection("users").document(it) } //FALLA AQUI TENGO QUE CAMBIAR EL ID DE USERS PARA QUE PILLE EMAIL
    private lateinit  var adapter: ContactsCustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        binding.btnReturnMensajes.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.mensajes,
                null
            )
        )
        binding.btnMensajesToHome.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.menu_principal,
                null
            )
        )


        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val data = ArrayList<ContactsViewModel>()
            getContacts(data)
            adapter = ContactsCustomAdapter(data)
            binding.recyclerView.adapter = adapter

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {

            uidRef?.get()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        val fromUser = document.toObject(Users::class.java)
                        val userContactsRef = fromUid?.let {
                            rootRef.collection("contactes").document(it)
                                .collection("userContacts")
                        }
                        userContactsRef?.get()?.addOnCompleteListener { t ->
                            if (t.isSuccessful) {
                                val listOfToUsersNames = ArrayList<String>()
                                val listOfToUsers =
                                    ArrayList<Users>()
                                val listOfRooms = ArrayList<String>()
                                for (d in t.result!!) {
                                    val toUser =
                                        d.toObject(Users::class.java)
                                    listOfToUsersNames.add(toUser.userName)
                                    listOfToUsers.add(toUser)
                                    listOfRooms.add(d.id)

                                }
                            }
                        }
                    }
                }
            }
        }
        authStateListener = FirebaseAuth.AuthStateListener { }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getContacts(data:ArrayList<ContactsViewModel>) {
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseUser != null) {
            Log.i("Mensaje", "Entra1")
            uidRef?.get()?.addOnCompleteListener { task ->
                Log.i("Mensaje", "Entra2")
                if (task.isSuccessful) {
                    Log.i("Mensaje", "Entra3")
                    val document = task.result
                    if (document!!.exists()) {
                        Log.i("Mensaje", "Entra4")
                        val fromUser = document.toObject(Users::class.java)
                        val userContactsRef = fromUid?.let {
                            rootRef.collection("contactes").document(it)
                                .collection("userContacts")
                        }
                        userContactsRef?.get()?.addOnCompleteListener { t ->
                            Log.i("Mensaje", "Entra5")
                            if (t.isSuccessful) {
                                Log.i("Mensaje", "Entra6")
                                val listOfToUsersNames = ArrayList<String>()
                                val listOfToUsers = ArrayList<Users>()
                                val listOfRooms = ArrayList<String>()
                                for (d in t.result!!) {
                                    Log.i("Mensaje", "Entra7")
                                    val toUser = d.toObject(Users::class.java)
                                    data.add(ContactsViewModel(toUser.userName))
                                    println(data)
                                    adapter.notifyDataSetChanged()
                                    listOfToUsersNames.add(toUser.userName)
                                    listOfToUsers.add(toUser)
                                    listOfRooms.add(d.id)
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

