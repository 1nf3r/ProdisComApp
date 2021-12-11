package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentContactsBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user.validate.CustomAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Contacts : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private var firebaseAuth: FirebaseAuth? = null
    private var authStateListener: FirebaseAuth.AuthStateListener? = null

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
//            getInfo(data)
            for (i in 1..5) {
            data.add(ContactsViewModel(i.toString()))
            }
            // This will pass the ArrayList to our Adapter
            val adapter = ContactsCustomAdapter(data)
            // Setting the Adapter with the recyclerview
            binding.recyclerView.adapter = adapter
            binding.recyclerView.setOnClickListener {
                println("hola")
            }
        }

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val fromUid = firebaseUser.uid
            val rootRef = FirebaseFirestore.getInstance()
            val uidRef = rootRef.collection("users").document(fromUid)
            uidRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        val fromUser = document.toObject(User::class.java)
                        val userContactsRef = rootRef.collection("contacts").document(fromUid)
                            .collection("userContacts")
                        userContactsRef.get().addOnCompleteListener { t ->
                            if (t.isSuccessful) {
                                val listOfToUsersNames = ArrayList<String>()
                                val listOfToUsers =
                                    ArrayList<cat.copernic.jose.antonio.miranda.prodiscomtest
                                    .data.User>()
                                val listOfRooms = ArrayList<String>()
                                for (d in t.result!!) {
                                    val toUser =
                                        d.toObject(
                                            cat.copernic.jose.antonio.miranda.prodiscomtest
                                                .data.User::class.java
                                        )
                                    listOfToUsersNames.add(toUser.userName)
                                    listOfToUsers.add(toUser)
                                    listOfRooms.add(d.id)

                                }
                                val arrayAdapter = context?.let {
                                    ArrayAdapter(
                                        it,
                                        android.R.layout.simple_list_item_1,
                                        listOfToUsersNames
                                    )
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

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btnReturnMensajes -> {
                binding.btnReturnMensajes.setOnClickListener(
                    Navigation.createNavigateOnClickListener(
                        R.id.menu_principal,
                        null
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return super.onOptionsItemSelected(item)
    }*/


}