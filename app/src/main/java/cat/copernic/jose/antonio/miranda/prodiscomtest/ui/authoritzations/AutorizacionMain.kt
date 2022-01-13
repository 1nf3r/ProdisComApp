package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.authoritzations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentAutorizacionBinding

class AutorizacionMain : Fragment() {

    private lateinit var binding: FragmentAutorizacionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAutorizacionBinding.inflate(inflater, container, false)

        binding.btnToNuevaAutorizacion.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nuevaAutorizacion, null))
        binding.btnToResultadosAutorizaciones.setOnClickListener(Navigation.createNavigateOnClickListener(
            R.id.resultadosAutorizaciones, null))

        binding.btnReturnAutorizaciones.setOnClickListener(Navigation.createNavigateOnClickListener(
            R.id.menu_principal, null))



        return binding.root
    }


}