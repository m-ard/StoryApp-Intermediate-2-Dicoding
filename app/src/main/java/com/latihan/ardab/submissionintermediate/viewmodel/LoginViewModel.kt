package com.latihan.ardab.submissionintermediate.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.latihan.ardab.submissionintermediate.data.remote.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel (private val repo: StoryRepository) : ViewModel() {

    fun getUser() = repo.getUser()

    fun saveUser(userName: String, userId: String, userToken: String) {
        viewModelScope.launch {
            repo.saveUser(userName,userId,userToken)
        }
    }

    fun logIn(email: String, password: String) = repo.login(email, password)

    fun logOut() {
        viewModelScope.launch {
            repo.logout()
        }
    }
}