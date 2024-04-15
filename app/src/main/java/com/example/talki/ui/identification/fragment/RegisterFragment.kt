package com.example.talki.ui.identification.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.talki.databinding.FragmentRegisterBinding
import com.example.talki.ui.identification.adapter.ViewPagerFragmentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(){

    private lateinit var mViewPager: ViewPager2
    private lateinit var btnBack: FrameLayout
    private lateinit var progress: ProgressBar
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewPager = binding.viewPager
        mViewPager.adapter = ViewPagerFragmentAdapter(requireActivity())
        mViewPager.offscreenPageLimit = 1

        mViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Действия при выборе новой страницы (если нужно)
            }
        })
    }
}
