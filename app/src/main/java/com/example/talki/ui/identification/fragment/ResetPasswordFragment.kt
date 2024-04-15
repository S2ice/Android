package com.example.talki.ui.identification.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.talki.R
import com.example.talki.databinding.FragmentResetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}