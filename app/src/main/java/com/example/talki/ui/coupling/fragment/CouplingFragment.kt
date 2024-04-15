package com.example.talki.ui.coupling.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.talki.databinding.FragmentCouplingBinding
import com.example.talki.ui.coupling.CouplingViewModel

class CouplingFragment : Fragment() {
    private var _binding: FragmentCouplingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val couplingViewModel =
            ViewModelProvider(this).get(CouplingViewModel::class.java)

        _binding = FragmentCouplingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}