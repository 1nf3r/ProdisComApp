package cat.copernic.jose.antonio.miranda.prodiscomtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentEditPerfilBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentUsuariosBinding


class editPerfil : Fragment() {
    private var _binding: FragmentEditPerfilBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPerfilBinding.inflate(inflater, container, false)
        binding.btnGuardarPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.perfil, null))

        binding.btnReturnEditPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.perfil, null))
        binding.btnEditePerfilToHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))

        return binding.root
    }
}