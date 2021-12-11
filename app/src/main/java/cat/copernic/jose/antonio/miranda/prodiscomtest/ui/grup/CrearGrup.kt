package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.grup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentCrearGrupBinding


class CrearGrup : Fragment() {
    private lateinit var binding: FragmentCrearGrupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            binding = FragmentCrearGrupBinding.inflate(inflater, container, false)

            /*binding.btnReturnGrup.setOnClickListener(
                Navigation.createNavigateOnClickListener(
                    R.id.menu_principal,
                    null
                )
            )*/

            return binding.root
    }
}