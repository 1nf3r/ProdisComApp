package cat.copernic.jose.antonio.miranda.prodiscomtest.logged.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentBloqUserBinding

class bloqUser : Fragment() {
    private var _binding: FragmentBloqUserBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBloqUserBinding.inflate(inflater, container, false)
        //binding.btnAddUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.addUsuario, null))

        binding.btnReturnBloqUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.usuarios, null))
        binding.btnBloqUserToHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))

        return binding.root
    }
}