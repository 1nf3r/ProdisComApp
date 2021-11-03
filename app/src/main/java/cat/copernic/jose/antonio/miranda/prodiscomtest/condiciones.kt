package cat.copernic.jose.antonio.miranda.prodiscomtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentAjustesBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentCondicionesBinding


class condiciones : Fragment() {
    private var _binding: FragmentCondicionesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCondicionesBinding.inflate(inflater, container, false)

        //binding.btnToCondiciones.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.condiciones, null))

        binding.btnReturnCondiciones.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.ajustes, null))
        binding.btnCondicionesToHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))



        return binding.root
    }
}