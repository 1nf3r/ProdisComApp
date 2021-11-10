package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.logged.grup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentAutorizacionBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentGrupBinding

class Grup : Fragment() {

    private lateinit var binding: FragmentGrupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGrupBinding.inflate(inflater, container, false)
        return binding.root
    }

}