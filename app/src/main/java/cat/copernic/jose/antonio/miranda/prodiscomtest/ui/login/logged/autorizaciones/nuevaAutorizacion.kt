package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.login.logged.autorizaciones

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentNuevaAutorizacionBinding

class nuevaAutorizacion : Fragment() {
    private var _binding: FragmentNuevaAutorizacionBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNuevaAutorizacionBinding.inflate(inflater, container, false)

        binding.btnEnviarNuevaAutorizacion.setOnClickListener(Navigation.createNavigateOnClickListener(
            R.id.autorizacion, null))
        //TODO Selecionar participantes

        binding.btnReturnNuevaAutorizacion.setOnClickListener(Navigation.createNavigateOnClickListener(
            R.id.autorizacion, null))
        binding.btnNuevaAutorizacionToHome.setOnClickListener(Navigation.createNavigateOnClickListener(
            R.id.menu_principal, null))



        return binding.root
    }
}