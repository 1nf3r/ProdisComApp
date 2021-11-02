package cat.copernic.jose.antonio.miranda.prodiscomtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentDelUserBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentUsuariosBinding

/*// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [delUser.newInstance] factory method to
 * create an instance of this fragment.
 */*/
class delUser : Fragment() {
    private var _binding: FragmentDelUserBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDelUserBinding.inflate(inflater, container, false)
        /*binding.btnAddUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.addUsuario, null))
        binding.btnDelUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.delUser, null))
        binding.btnModUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.modUser, null))
        binding.btnValUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.valUser, null))
        binding.btnBloqUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.bloqUser, null))
        binding.btnReturnUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))*/
        //binding.btnUserToHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))

        return binding.root
    }

/*// TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_del_user, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment delUser.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            delUser().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}