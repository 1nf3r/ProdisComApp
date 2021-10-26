package cat.copernic.jose.antonio.miranda.prodiscomtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentAddUsuarioBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentDelUserBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentUsuariosBinding
class delUser : Fragment() {
    private var _binding: FragmentDelUserBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDelUserBinding.inflate(inflater, container, false)
        //binding.btnAddUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.addUsuario, null))

        binding.btnReturnDelUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.usuarios, null))
        binding.btnDelUserToHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))

        return binding.root
    }
}