package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.grup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentGrupBinding

class GrupMain : Fragment() {

    private lateinit var binding: FragmentGrupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGrupBinding.inflate(inflater, container, false)

        binding.btnReturnGrup.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.menu_principal,
                null
            )
        )

       /* binding.btnToCrearGrup.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.CrearGrup,
                null
            )
        )*/

        return binding.root
    }

}