package cat.copernic.jose.antonio.miranda.prodiscomtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentPerfilBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentUsuariosBinding

class perfil : Fragment() {
    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        binding.btnToEditPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.editPerfil, null))

        binding.btnReturnPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))

        return binding.root
    }
}