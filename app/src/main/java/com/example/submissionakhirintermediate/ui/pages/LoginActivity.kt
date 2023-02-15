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
import com.example.submissionakhirintermediate.data.model.UserModel
import com.example.submissionakhirintermediate.databinding.ActivityLoginBinding
import com.example.submissionakhirintermediate.ui.viewmodel.LoginViewModel
import com.example.submissionakhirintermediate.utility.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setMyButtonEnable()
        setCustomView()
        setupViewModel()
        playAnimation()
        setAction()

    }

    private fun setAction() {
        binding.customButtonLogin.setOnClickListener {
            userLogin()
        }

        binding.createAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finishAffinity()
        }
    }

    private fun userLogin() {
        val email = binding.customEditTextEmail.text.toString()
        val password = binding.customEditTextPass.text.toString()
        loginViewModel.loginUser(email, password).observe(this) {
            when (it) {
                is Result.Success -> {
                    showLoading(true)
                    val response = it.data
                    saveDataUser(
                        UserModel(
                        response.loginResult.name.toString(),
                        response.loginResult.token.toString(),
                        true
                    ))
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finishAffinity()
                }
                is Result.Loading -> showLoading(true)
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, resources.getString(R.string.gagal_log_in), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveDataUser(user: UserModel) {
        loginViewModel.saveUser(user)
    }

    private fun setupViewModel() {
        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private fun setCustomView() {
        // Email
        binding.customEditTextEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        // Pass
        binding.customEditTextPass.addTextChangedListener(object: TextWatcher {
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
        val result = binding.customEditTextEmail.text
        val result2 = binding.customEditTextPass.text

        binding.customButtonLogin.isEnabled = (result != null && result.toString()
            .isNotEmpty()) && (result2 != null && result2.toString().isNotEmpty())
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarLoading.visibility = View.VISIBLE
        } else {
            binding.progressBarLoading.visibility = View.GONE
        }
    }

    private fun playAnimation() {
        // Title & Image
        val titleLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val imgLogo = ObjectAnimator.ofFloat(binding.ivLogo, View.ALPHA, 1f).setDuration(500)

        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        // Email
        val textEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val inputEmail =
            ObjectAnimator.ofFloat(binding.emailTextField, View.ALPHA, 1f).setDuration(500)

        // Password
        val textPassword =
            ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val inputPassword =
            ObjectAnimator.ofFloat(binding.passTextField, View.ALPHA, 1f).setDuration(500)

        // Button & textView CreateAccount
        val btnLogin =
            ObjectAnimator.ofFloat(binding.customButtonLogin, View.ALPHA, 1f).setDuration(500)
        val createAccount =
            ObjectAnimator.ofFloat(binding.createAccount, View.ALPHA, 1f).setDuration(500)

        // playTogether
        val setEmail = AnimatorSet().apply {
            playTogether(textEmail, inputEmail)
        }
        val setPassword = AnimatorSet().apply {
            playTogether(textPassword, inputPassword)
        }

        AnimatorSet().apply {
            playSequentially(
                imgLogo, titleLogin, setEmail, setPassword, btnLogin, createAccount

            )
            start()
        }
    }

}