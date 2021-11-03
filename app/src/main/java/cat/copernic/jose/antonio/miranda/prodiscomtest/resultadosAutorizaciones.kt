package cat.copernic.jose.antonio.miranda.prodiscomtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentNuevaAutorizacionBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentResultadosAutorizacionesBinding

class resultadosAutorizaciones : Fragment() {
    private var _binding: FragmentResultadosAutorizacionesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultadosAutorizacionesBinding.inflate(inflater, container, false)

        //binding.btnEnviarNuevaAutorizacion.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.autorizacion, null))

        binding.btnReturnResultadosAutorizacion.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.autorizacion, null))
        binding.btnResultadosAutorizacionToHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))



        return binding.root
    }
}