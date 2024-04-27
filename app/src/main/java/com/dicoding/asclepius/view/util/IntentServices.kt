package com.dicoding.asclepius.view.util

import android.content.Context
import android.content.Intent
import com.dicoding.asclepius.view.webview.WebViewActivity

fun openWebPage(context: Context, url: String) {
    val intent = Intent(context, WebViewActivity::class.java).apply {
        putExtra("url", url)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}

