package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.logged

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentMenuPrincipalBinding


class Menu_principal : Fragment() {

    private var _binding: FragmentMenuPrincipalBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuPrincipalBinding.inflate(inflater, container, false)
        binding.btnToPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.perfil, null))
        binding.btnToAjustes.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.ajustes, null))
        binding.btnToUsuarios.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.usuarios, null))
        binding.btnToAutorizaciones.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.autorizacion, null))
        binding.btnToGrup.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.grup, null))
        binding.btnToSalir.setOnClickListener {
            getActivity()?.onBackPressed();
        }

        return binding.root
    }


}