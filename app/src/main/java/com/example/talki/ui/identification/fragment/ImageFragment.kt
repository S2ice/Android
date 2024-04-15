package com.example.talki.ui.identification.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.talki.R
import com.example.talki.ui.identification.ImageViewModel

class ImageFragment : Fragment() {

    companion object {
        fun newInstance() = ImageFragment()
    }

    private val viewModel: ImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }
}