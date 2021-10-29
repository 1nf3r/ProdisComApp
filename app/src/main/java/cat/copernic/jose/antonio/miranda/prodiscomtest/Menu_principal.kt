package cat.copernic.jose.antonio.miranda.prodiscomtest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentMenuPrincipalBinding

class Menu_principal : Fragment() {

    private var _binding: FragmentMenuPrincipalBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("Login", "Si entra")
        _binding = FragmentMenuPrincipalBinding.inflate(inflater, container, false)

        /*val binding = DataBindingUtil.inflate<FragmentTitleBinding>(
            inflater,
            R.layout.fragment_menu_principal, container, false
        ) */
        //return inflater.inflate(R.layout.fragment_menu_principal, container, false)
        return binding.root
    }


}