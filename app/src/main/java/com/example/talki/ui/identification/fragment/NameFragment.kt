package com.example.talki.ui.identification.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.talki.databinding.FragmentNameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NameFragment : Fragment() {
    private lateinit var binding: FragmentNameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        setupListeners()

    }

    private fun initUI() {
        // Инициализация UI элементов (если нужно)
    }

    private fun setupListeners() {
        // Установка обработчиков событий (если нужно)
    }
}
