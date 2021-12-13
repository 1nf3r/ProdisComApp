package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

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
    private val fromUid = firebaseUser!!.uid
    private val rootRef = FirebaseFirestore.getInstance()
    private val uidRef = rootRef.collection("users").document(fromUid) //FALLA AQUI TENGO QUE CAMBIAR EL ID DE USERS PARA QUE PILLE EMAIL

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
        CoroutineScope(Dispatchers.Main).launch {
/*            for (i in 1..5) {
                data.add(ContactsViewModel(i.toString()))
            }*/
            getContacts(data)
            val adapter = ContactsCustomAdapter(data)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.setOnClickListener {
                println("hola")
            }
        }

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {

            uidRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        val fromUser = document.toObject(Users::class.java)
                        val userContactsRef = rootRef.collection("contactes").document(fromUid)
                            .collection("userContacts")
                        userContactsRef.get().addOnCompleteListener { t ->
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


                                /*binding.recyclerView.adapter = arrayAdapter
                                binding.recyclerView.onItemClickListener = AdapterView.OnItemClickListener{ _, _, position, _ ->
                                    val intent = Intent(this, ChatActivity::class.java)
                                    intent.putExtra("fromUser", fromUser)
                                    intent.putExtra("toUser", listOfToUsers[position])
                                    intent.putExtra("roomId", "noRoomId")
                                    startActivity(intent)
                                    requireActivity().finish()
                                }*/

                            }
                        }
                    }
                }
            }
        }
        authStateListener = FirebaseAuth.AuthStateListener { }
        return binding.root
    }

    private suspend fun getContacts(data:ArrayList<ContactsViewModel>) {
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseUser != null) {
            Log.i("Mensaje", "Entra1")
            uidRef.get().addOnCompleteListener { task ->
                Log.i("Mensaje", "Entra2")
                if (task.isSuccessful) {
                    Log.i("Mensaje", "Entra3")
                    val document = task.result
                    if (document!!.exists()) {
                        Log.i("Mensaje", "Entra4")
                        val fromUser = document.toObject(Users::class.java)
                        val userContactsRef = rootRef.collection("contactes").document(fromUid)
                            .collection("userContacts")
                        userContactsRef.get().addOnCompleteListener { t ->
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
                                    listOfToUsersNames.add(toUser.userName)
                                    listOfToUsers.add(toUser)
                                    listOfRooms.add(d.id)

                                }
                            }
                        }
                    }
                }
            }.await()
        }
    }
}

