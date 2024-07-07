package com.unmsm.shaveupapp.ui.forgotpassword

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterLanguage.LanguageManager
import com.unmsm.shaveupapp.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    companion object {
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.updateLocale(this, LanguageManager.getSelectedLanguage(this))
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        emailFocusListener()
        binding.btnSendEmail.setOnClickListener {
            val email = binding.tietEmail.text.toString()

            auth.sendPasswordResetEmail(email.trim()).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, getString(R.string.email_sent_success), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, getString(R.string.email_sent_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
        updateTexts()

    }

    private fun updateTexts() {
        binding.tvLogo.text = getString(R.string.change_password)
        binding.tilEmail.hint = getString(R.string.Email)
        binding.btnSendEmail.text = getString(R.string.change_password)
    }

    private fun emailFocusListener() {
        binding.tietEmail.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                binding.tilEmail.error = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.tietEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return getString(R.string.invalid_email)
        }
        return null
    }
}