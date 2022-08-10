package com.zefip.profitov_test

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.zefip.profitov_test.databinding.FragmentPostWebpageBinding

class PostWebPageFragment(private val url: String) : Fragment() {

    private var _binding: FragmentPostWebpageBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostWebpageBinding.inflate(inflater, container, false)
        binding.webviewPostUrl.webViewClient = WebViewClient()
        binding.webviewPostUrl.settings.javaScriptEnabled = true
        binding.webviewPostUrl.loadUrl(url)
        return binding.root
    }
}