package com.dicoding.asclepius.view.nav

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asLiveData
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityNavBinding
import com.dicoding.asclepius.view.MainActivity
import com.dicoding.asclepius.view.healt.FragmentHealthNews
import com.dicoding.asclepius.view.home.FragmentHome
import dataStore

class NavActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        SettingAppPreferences.getInstance(this.dataStore).getThemeSetting().asLiveData().observe(this, { isDarkMode ->
            val mode = if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        })

        binding.idNavMain.menu.getItem(1).isEnabled = false
        binding.idBtnScan.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.idNavMain.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_user -> {
                    // Navigate to FragmentHome
                    val fragment = FragmentHome()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.commit()
                    true
                }
                R.id.nav_berita_user -> {
                    // Navigate to FragmentHealthNews
                    val fragment = FragmentHealthNews()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.commit()
                    true
                }
                else -> false
            }
        }
    }
}