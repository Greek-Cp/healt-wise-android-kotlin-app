package com.dicoding.asclepius.view.webview

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityWebviewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.webview.webViewClient = WebViewClient()
        binding.webview.settings.javaScriptEnabled = true
        val url = intent.getStringExtra("url")
        if (url != null) {
            binding.webview.loadUrl(url)
        }
    }
}
