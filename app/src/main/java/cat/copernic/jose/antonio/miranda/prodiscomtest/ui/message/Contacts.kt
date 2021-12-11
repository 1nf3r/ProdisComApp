package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentContactsBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentMensajesBinding
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.rpc.context.AttributeContext

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
/*                                binding.recyclerView.adapter = arrayAdapter
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