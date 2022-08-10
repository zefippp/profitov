package com.zefip.profitov_test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zefip.profitov_test.databinding.FragmentPostTextBinding

class PostTextFragment(private val text: String) : Fragment() {

    private var _binding: FragmentPostTextBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostTextBinding.inflate(inflater, container, false)
        binding.textviewPostText.text = text
        return binding.root
    }
}