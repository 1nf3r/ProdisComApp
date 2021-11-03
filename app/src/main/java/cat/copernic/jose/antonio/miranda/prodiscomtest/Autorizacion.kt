package cat.copernic.jose.antonio.miranda.prodiscomtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentAutorizacionBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentMenuPrincipalBinding

class Autorizacion : Fragment() {

    private var _binding: FragmentAutorizacionBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAutorizacionBinding.inflate(inflater, container, false)

        binding.btnToNuevaAutorizacion.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nuevaAutorizacion, null))
        binding.btnToResultadosAutorizaciones.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.resultadosAutorizaciones, null))

        binding.btnReturnAutorizaciones.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))



        return binding.root
    }


}