package com.example.submissionakhirintermediate.ui.pages

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.submissionakhirintermediate.R
import com.example.submissionakhirintermediate.utility.Result
import com.example.submissionakhirintermediate.databinding.ActivityRegisterBinding
import com.example.submissionakhirintermediate.ui.viewmodel.RegisterViewModel
import com.example.submissionakhirintermediate.utility.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupViewModel()
        setMyButtonEnable()
        setCustomView()
        playAnimation()
        setAction()
    }

    private fun setAction() {
        binding.haveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.customButtonRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name = binding.customEditTextName.text.toString()
        val email = binding.customEditTextEmail.text.toString()
        val password = binding.customEditTextPass.text.toString()
        registerViewModel.registerUser(name, email, password).observe(this) {
            when (it) {
                is Result.Success -> {
                    showLoading(true)
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
                is Result.Loading -> showLoading(true)
                is Result.Error -> {
                    Toast.makeText(this, resources.getString(R.string.gagal_register), Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }
    }

    private fun setupViewModel() {
        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        registerViewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
    }

    private fun setCustomView() {
        // Name
        binding.customEditTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        // Email
        binding.customEditTextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        // Password
        binding.customEditTextPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun setMyButtonEnable() {
        val result = binding.customEditTextName.text
        val result2 = binding.customEditTextEmail.text
        val result3 = binding.customEditTextPass.text

        binding.customButtonRegister.isEnabled = (result != null && result.toString()
            .isNotEmpty()) && (result2 != null && result2.toString()
            .isNotEmpty()) && (result3 != null && result3.toString().isNotEmpty())
    }

    private fun playAnimation() {
        // Title & Image
        val titleRegister =
            ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)
        val imglogo = ObjectAnimator.ofFloat(binding.ivLogo, View.ALPHA, 1f).setDuration(500)

        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        // Name
        val textName = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val inputName =
            ObjectAnimator.ofFloat(binding.nameRegTextField, View.ALPHA, 1f).setDuration(500)

        // Email
        val textEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val inputEmail =
            ObjectAnimator.ofFloat(binding.emailRegTextField, View.ALPHA, 1f).setDuration(500)

        // Password
        val textPassword =
            ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val inputPassword =
            ObjectAnimator.ofFloat(binding.passRegTextField, View.ALPHA, 1f).setDuration(500)

        // Button & textView CreateAccount
        val btnRegister =
            ObjectAnimator.ofFloat(binding.customButtonRegister, View.ALPHA, 1f).setDuration(500)
        val loginAccount =
            ObjectAnimator.ofFloat(binding.haveAccount, View.ALPHA, 1f).setDuration(500)

        val setName = AnimatorSet().apply {
            playTogether(textName, inputName)
        }

        val setEmail = AnimatorSet().apply {
            playTogether(textEmail, inputEmail)
        }

        val setPassword = AnimatorSet().apply {
            playTogether(textPassword, inputPassword)
        }

        AnimatorSet().apply {
            playSequentially(
                imglogo, titleRegister, setName, setEmail, setPassword, btnRegister, loginAccount
            )
            start()
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarLoading.visibility = View.VISIBLE
        } else {
            binding.progressBarLoading.visibility = View.GONE
        }
    }

}