package cat.copernic.jose.antonio.miranda.prodiscomtest.logged.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentUsuariosBinding

class Usuarios : Fragment() {
    private var _binding: FragmentUsuariosBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuariosBinding.inflate(inflater, container, false)
        binding.btnAddUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.addUsuario, null))
        binding.btnDelUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.delUser, null))
        binding.btnModUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.modUser, null))
        binding.btnValUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.valUser, null))
        binding.btnBloqUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.bloqUser, null))
        binding.btnReturnUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))
        //binding.btnUserToHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))

        return binding.root
    }

}