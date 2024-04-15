package com.example.talki.ui.identification.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.talki.R
import com.example.talki.databinding.FragmentLoginBinding
import com.example.talki.ui.identification.IdentityViewModel
import com.example.talki.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<IdentityViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtnLogIn.setOnClickListener {
            Log.d("LoginFragment", "Успешный вход")
            binding.progress.visibility = View.GONE
            activity?.startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }

        binding.loginBtnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginForget.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
