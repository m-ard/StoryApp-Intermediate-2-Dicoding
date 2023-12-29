package com.latihan.ardab.submissionintermediate.viewmodel


import androidx.lifecycle.ViewModel
import com.latihan.ardab.submissionintermediate.data.remote.StoryRepository

class SignUpViewModel(private val repo: StoryRepository) : ViewModel() {

    fun signUp(name: String, email: String, password: String) = repo.signup(name, email, password)
}