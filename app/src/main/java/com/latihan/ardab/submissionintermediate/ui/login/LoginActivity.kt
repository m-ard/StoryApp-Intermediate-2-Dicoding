package com.latihan.ardab.submissionintermediate.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.latihan.ardab.submissionintermediate.R
import com.latihan.ardab.submissionintermediate.databinding.ActivityLoginBinding
import com.latihan.ardab.submissionintermediate.ui.main.MainActivity
import com.latihan.ardab.submissionintermediate.ui.signup.SignUpActivity
import com.latihan.ardab.submissionintermediate.viewmodel.LoginViewModel
import com.latihan.ardab.submissionintermediate.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val loginViewModel: LoginViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        playAnimation()
        setupAction()

        binding.haveAccountTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        val haveAccountTextView = ObjectAnimator.ofFloat(binding.haveAccountTextView, View.ALPHA, 1f).setDuration(300)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(300)
        val ivLogin = ObjectAnimator.ofFloat(binding.ivLogin, View.ALPHA, 1f).setDuration(300)
        val tvLoginEmail = ObjectAnimator.ofFloat(binding.tvLoginEmail, View.ALPHA, 1f).setDuration(300)
        val edLoginEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(300)
        val tvEdLoginPassword = ObjectAnimator.ofFloat(binding.tvEdLoginPassword, View.ALPHA, 1f).setDuration(300)
        val edLoginPassword = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(300)
        val signin = ObjectAnimator.ofFloat(binding.signinButton, View.ALPHA, 1f).setDuration(300)
        val copyrightTextView = ObjectAnimator.ofFloat(binding.copyrightTextView, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(tvLogin, ivLogin, tvLoginEmail, edLoginEmail, tvEdLoginPassword, edLoginPassword, haveAccountTextView, signin, copyrightTextView)
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupAction() {
        binding.signinButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error = getString(R.string.input_name)
                }
                password.isEmpty() -> {
                    binding.edLoginPassword.error = getString(R.string.input_password)
                }
                password.length < 8 -> {
                    binding.edLoginPassword.error = getString(R.string.label_validation_password)
                }

                else -> {
                    loginViewModel.logIn(email, password).observe(this){ result ->
                        val data = result.loginResult
                        if (data != null) {
                            loginViewModel.saveUser(data.name, data.userId, data.token)
                            if (result.error) {
                                if (result.message == "400") {
                                    val builder = AlertDialog.Builder(this)
                                    builder.setTitle(R.string.info)
                                    builder.setMessage(R.string.label_invalid_email)
                                    builder.setIcon(R.drawable.logo_failed)
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        alertDialog.dismiss()
                                    }, 2000)
                                }
                                if (result.message == "401") {
                                    val builder = AlertDialog.Builder(this)
                                    builder.setTitle(R.string.info)
                                    builder.setMessage(R.string.login_user_not_found)
                                    builder.setIcon(R.drawable.logo_failed)
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        alertDialog.dismiss()
                                    }, 2000)
                                }
                            } else {
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle(R.string.info)
                                builder.setMessage(result.message)
                                builder.setIcon(R.drawable.ic_success)
                                val alertDialog: AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                                Handler(Looper.getMainLooper()).postDelayed({
                                    alertDialog.dismiss()
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                }, 2000)
                            }
                        }

                        if (result.message == "") {
                            showLoading(true)
                        } else {
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }
}